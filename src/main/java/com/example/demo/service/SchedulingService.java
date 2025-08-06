package com.example.demo.service;

import com.example.demo.dto.TaskCreatedEvent;
import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.model.Notification;
import com.example.demo.repository.JpaTaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class SchedulingService {

    private static final Logger logger = LoggerFactory.getLogger(SchedulingService.class);
    
    public SchedulingService() {
        logger.info("SchedulingService создан! Планировщик должен работать.");
    }

    @Autowired
    private JpaTaskRepository taskRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private MessageService messageService;


    @Scheduled(fixedRate = 30000)
    @Transactional
    public void checkOverdueTasks() {
        logger.info("Запуск проверки просроченных задач в {}", LocalDateTime.now());
        
        LocalDateTime now = LocalDateTime.now();
        List<Task> overdueTasks = taskRepository.findOverdueTasks(now);
        
        logger.info("Найдено {} просроченных задач", overdueTasks.size());
        
        for (Task task : overdueTasks) {
            if (task.getStatus() != TaskStatus.OVERDUE) {

                processOverdueTask(task);

                createOverdueNotificationAsync(task);
            }
        }
    }
    

    @CacheEvict(value = {"tasks", "pendingTasks", "task"}, allEntries = true)
    public void processOverdueTask(Task task) {
        logger.info("Обработка просроченной задачи ID: {}", task.getId());
        
        try {

            Task freshTask = taskRepository.findById(task.getId()).orElse(null);
            if (freshTask != null) {
                freshTask.setStatus(TaskStatus.OVERDUE);
                Task savedTask = taskRepository.save(freshTask);
                logger.info("Задача ID: {} помечена как просроченная. Новый статус: {}", 
                    savedTask.getId(), savedTask.getStatus());
                

                Task verifyTask = taskRepository.findById(task.getId()).orElse(null);
                if (verifyTask != null) {
                    logger.info("ПРОВЕРКА: Задача ID: {} имеет статус: {}", 
                        verifyTask.getId(), verifyTask.getStatus());
                }
            }
        } catch (Exception e) {
            logger.error("Ошибка при обработке просроченной задачи ID: {}", task.getId(), e);
        }
    }


    @Async
    @Transactional
    public CompletableFuture<Void> processOverdueTaskAsync(Task task) {
        logger.info("Асинхронная обработка просроченной задачи ID: {}", task.getId());
        
        try {

            task.setStatus(TaskStatus.OVERDUE);
            taskRepository.save(task);
            

            createOverdueNotificationAsync(task);
            
            logger.info("Задача ID: {} помечена как просроченная", task.getId());
        } catch (Exception e) {
            logger.error("Ошибка при обработке просроченной задачи ID: {}", task.getId(), e);
        }
        
        return CompletableFuture.completedFuture(null);
    }


    @Async
    public CompletableFuture<Void> createOverdueNotificationAsync(Task task) {
        logger.info("Создание уведомления о просроченной задаче ID: {}", task.getId());
        
        try {

            TaskCreatedEvent overdueEvent = new TaskCreatedEvent();
            overdueEvent.setTaskId(task.getId());
            overdueEvent.setUserId(task.getUserId());
            overdueEvent.setTitle(task.getTitle());
            overdueEvent.setDescription("Задача '" + task.getTitle() + "' просрочена!");
            overdueEvent.setCreationDate(LocalDateTime.now());
            overdueEvent.setStatus("OVERDUE");


            messageService.publishTaskCreatedEvent(overdueEvent);
            
            logger.info("Событие о просроченной задаче отправлено через RabbitMQ для пользователя ID: {}", task.getUserId());
        } catch (Exception e) {
            logger.error("Ошибка при отправке события о просроченной задаче ID: {}", task.getId(), e);
        }
        
        return CompletableFuture.completedFuture(null);
    }


    @Async
    public CompletableFuture<Void> processBulkTaskOperationsAsync(List<Task> tasks) {
        logger.info("Начало массовой обработки {} задач", tasks.size());
        
        try {
            for (Task task : tasks) {
    
                Thread.sleep(100);
                logger.debug("Обработана задача ID: {}", task.getId());
            }
            
            logger.info("Завершена массовая обработка {} задач", tasks.size());
        } catch (InterruptedException e) {
            logger.error("Прервана массовая обработка задач", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("Ошибка при массовой обработке задач", e);
        }
        
        return CompletableFuture.completedFuture(null);
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupOldNotifications() {
        logger.info("Запуск ежедневной очистки старых уведомлений в {}", LocalDateTime.now());
        
        try {
    
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
    
            
            logger.info("Завершена очистка старых уведомлений");
        } catch (Exception e) {
            logger.error("Ошибка при очистке старых уведомлений", e);
        }
    }
}