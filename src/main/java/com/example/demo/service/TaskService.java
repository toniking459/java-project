package com.example.demo.service;

import com.example.demo.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task createTask(Task task);
    List<Task> getAllTasks(Long userId);
    List<Task> getPendingTasks(Long userId);
    void deleteTask(Long taskId);
    Optional<Task> findById(Long id);
} 