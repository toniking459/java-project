package com.example.demo.service.impl;

import com.example.demo.dto.TaskCreatedEvent;
import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.JpaTaskRepository;
import com.example.demo.service.MessageService;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("postgres")
public class PostgresTaskServiceImpl implements TaskService {
    private final JpaTaskRepository taskRepository;
    private final MessageService messageService;

    @Autowired
    public PostgresTaskServiceImpl(JpaTaskRepository taskRepository, MessageService messageService) {
        this.taskRepository = taskRepository;
        this.messageService = messageService;
    }

    @Override
    @CacheEvict(value = {"tasks", "pendingTasks"}, allEntries = true)
    public Task createTask(Task task) {
        Task savedTask = taskRepository.save(task);
        
        TaskCreatedEvent event = new TaskCreatedEvent(
            savedTask.getId(),
            savedTask.getUserId(),
            savedTask.getTitle(),
            savedTask.getDescription(),
            savedTask.getCreationDate(),
            savedTask.getStatus().toString()
        );
        
        System.out.println("About to publish task created event for task: " + savedTask.getId());
        messageService.publishTaskCreatedEvent(event);
        System.out.println("Task created event published for task: " + savedTask.getId());
        
        return savedTask;
    }

    @Override
    @Cacheable(value = "tasks", key = "#userId")
    public List<Task> getAllTasks(Long userId) {
        return taskRepository.findByUserIdAndDeletedFalse(userId);
    }

    @Override
    @Cacheable(value = "pendingTasks", key = "#userId")
    public List<Task> getPendingTasks(Long userId) {
        return taskRepository.findByUserIdAndStatusAndDeletedFalse(userId, TaskStatus.PENDING);
    }

    @Override
    @CacheEvict(value = {"tasks", "pendingTasks"}, allEntries = true)
    public void deleteTask(Long taskId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        taskOpt.ifPresent(task -> {
            task.setDeleted(true);
            taskRepository.save(task);
        });
    }

    @Override
    @Cacheable(value = "task", key = "#id")
    public Optional<Task> findById(Long id) {
        return taskRepository.findByIdAndDeletedFalse(id);
    }
} 