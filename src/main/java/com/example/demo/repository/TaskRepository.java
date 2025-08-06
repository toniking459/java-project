package com.example.demo.repository;

import com.example.demo.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(Long id);
    List<Task> findByUserId(Long userId);
    List<Task> findByUserIdAndPending(Long userId);
    List<Task> findAll();
    void markDeleted(Long id);

    void clear();


} 