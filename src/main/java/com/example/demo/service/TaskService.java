package com.example.demo.service;

import com.example.demo.model.Task;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface TaskService {
    Task createTask(Task task);
    List<Task> getAllTasks(Long userId);
    List<Task> getPendingTasks(Long userId);
    void deleteTask(Long taskId);
    Optional<Task> findById(Long id);
    

    default CompletableFuture<Task> createTaskAsync(Task task) {
        return CompletableFuture.completedFuture(createTask(task));
    }
    
    default CompletableFuture<List<Task>> getAllTasksAsync(Long userId) {
        return CompletableFuture.completedFuture(getAllTasks(userId));
    }
    
    default CompletableFuture<Void> bulkUpdateTasksAsync(List<Task> tasks) {
        return CompletableFuture.completedFuture(null);
    }
} 