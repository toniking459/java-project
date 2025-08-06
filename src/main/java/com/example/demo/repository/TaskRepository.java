package com.example.demo.repository;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(Long id);
    List<Task> findByUserId(Long userId);
    List<Task> findByUserIdAndPending(Long userId);
    List<Task> findAll();
    void markDeleted(Long id);

    void clear();


} 