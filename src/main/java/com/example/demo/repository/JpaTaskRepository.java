package com.example.demo.repository;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile({"h2", "postgres"})
public interface JpaTaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
    List<Task> findByUserIdAndStatus(Long userId, TaskStatus status);
    List<Task> findByUserIdAndDeletedFalse(Long userId);
    List<Task> findByUserIdAndStatusAndDeletedFalse(Long userId, TaskStatus status);
    Optional<Task> findByIdAndDeletedFalse(Long id);
} 