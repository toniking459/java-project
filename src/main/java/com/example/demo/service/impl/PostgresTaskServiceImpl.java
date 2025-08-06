package com.example.demo.service.impl;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.JpaTaskRepository;
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

    @Autowired
    public PostgresTaskServiceImpl(JpaTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    @CacheEvict(value = {"tasks", "pendingTasks"}, allEntries = true)
    public Task createTask(Task task) {
        return taskRepository.save(task);
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