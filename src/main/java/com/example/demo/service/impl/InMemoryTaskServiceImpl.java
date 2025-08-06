package com.example.demo.service.impl;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.service.TaskService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Profile("in-memory")
public class InMemoryTaskServiceImpl implements TaskService {
    private final Map<Long, Task> tasks = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Task createTask(Task task) {
        task.setId(idCounter.getAndIncrement());
        task.setCreationDate(LocalDateTime.now());
        task.setStatus(TaskStatus.PENDING);
        task.setDeleted(false);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public List<Task> getAllTasks(Long userId) {
        return tasks.values().stream()
                .filter(task -> task.getUserId().equals(userId) && !task.isDeleted())
                .toList();
    }

    @Override
    public List<Task> getPendingTasks(Long userId) {
        return tasks.values().stream()
                .filter(task -> task.getUserId().equals(userId) 
                        && !task.isDeleted() 
                        && task.getStatus() == TaskStatus.PENDING)
                .toList();
    }

    @Override
    public void deleteTask(Long taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            task.setDeleted(true);
            tasks.put(taskId, task);
        }
    }

    @Override
    public Optional<Task> findById(Long id) {
        Task task = tasks.get(id);
        return task != null && !task.isDeleted() ? Optional.of(task) : Optional.empty();
    }
} 