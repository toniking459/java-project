package com.example.demo.service;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.model.Notification;
import com.example.demo.repository.JpaTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchedulingServiceTest {

    @Mock
    private JpaTaskRepository taskRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private SchedulingService schedulingService;

    private Task overdueTask;
    private Task normalTask;

    @BeforeEach
    void setUp() {
        overdueTask = new Task();
        overdueTask.setId(1L);
        overdueTask.setUserId(100L);
        overdueTask.setTitle("Просроченная задача");
        overdueTask.setDescription("Описание просроченной задачи");
        overdueTask.setCreationDate(LocalDateTime.now().minusDays(5));
        overdueTask.setTargetDate(LocalDateTime.now().minusDays(1));
        overdueTask.setStatus(TaskStatus.PENDING);
        overdueTask.setDeleted(false);

        normalTask = new Task();
        normalTask.setId(2L);
        normalTask.setUserId(200L);
        normalTask.setTitle("Обычная задача");
        normalTask.setDescription("Описание обычной задачи");
        normalTask.setCreationDate(LocalDateTime.now().minusDays(1));
        normalTask.setTargetDate(LocalDateTime.now().plusDays(1));
        normalTask.setStatus(TaskStatus.PENDING);
        normalTask.setDeleted(false);
    }

    @Test
    void testCheckOverdueTasks_FindsOverdueTasks() {

        List<Task> overdueTasks = Arrays.asList(overdueTask);
        when(taskRepository.findOverdueTasks(any(LocalDateTime.class))).thenReturn(overdueTasks);
        when(taskRepository.save(any(Task.class))).thenReturn(overdueTask);
        when(notificationService.createNotification(any(Notification.class))).thenReturn(new Notification());


        schedulingService.checkOverdueTasks();


        verify(taskRepository).findOverdueTasks(any(LocalDateTime.class));
        verify(taskRepository).save(overdueTask);
    }

    @Test
    void testCheckOverdueTasks_NoOverdueTasks() {

        when(taskRepository.findOverdueTasks(any(LocalDateTime.class))).thenReturn(Arrays.asList());


        schedulingService.checkOverdueTasks();


        verify(taskRepository).findOverdueTasks(any(LocalDateTime.class));
        verify(taskRepository, never()).save(any(Task.class));
        verify(notificationService, never()).createNotification(any(Notification.class));
    }

    @Test
    void testProcessOverdueTaskAsync_UpdatesTaskStatus() throws Exception {

        when(taskRepository.save(any(Task.class))).thenReturn(overdueTask);
        when(notificationService.createNotification(any(Notification.class))).thenReturn(new Notification());


        CompletableFuture<Void> result = schedulingService.processOverdueTaskAsync(overdueTask);
        result.get();


        verify(taskRepository).save(overdueTask);
        assert overdueTask.getStatus() == TaskStatus.OVERDUE;
    }

    @Test
    void testCreateOverdueNotificationAsync_CreatesNotification() throws Exception {

        when(notificationService.createNotification(any(Notification.class))).thenReturn(new Notification());


        CompletableFuture<Void> result = schedulingService.createOverdueNotificationAsync(overdueTask);
        result.get();


        verify(notificationService).createNotification(argThat(notification -> 
            notification.getUserId().equals(overdueTask.getUserId()) &&
            notification.getMessage().contains(overdueTask.getTitle()) &&
            notification.getType().equals("OVERDUE_TASK") &&
            !notification.isRead()
        ));
    }

    @Test
    void testProcessBulkTaskOperationsAsync_ProcessesAllTasks() throws Exception {

        List<Task> tasks = Arrays.asList(overdueTask, normalTask);


        CompletableFuture<Void> result = schedulingService.processBulkTaskOperationsAsync(tasks);
        result.get();

 - проверяем что метод завершился без ошибок
        assert result.isDone();
        assert !result.isCompletedExceptionally();
    }

    @Test
    void testCheckOverdueTasks_SkipsAlreadyOverdueTasks() {

        overdueTask.setStatus(TaskStatus.OVERDUE);
        List<Task> overdueTasks = Arrays.asList(overdueTask);
        when(taskRepository.findOverdueTasks(any(LocalDateTime.class))).thenReturn(overdueTasks);


        schedulingService.checkOverdueTasks();


        verify(taskRepository).findOverdueTasks(any(LocalDateTime.class));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void testCleanupOldNotifications_ExecutesWithoutErrors() {

        schedulingService.cleanupOldNotifications();

 - проверяем что метод выполнился без исключений

    }
}