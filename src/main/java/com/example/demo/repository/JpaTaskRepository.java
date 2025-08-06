package com.example.demo.repository;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
    
    @Query("SELECT t FROM Task t WHERE t.targetDate < :currentTime AND t.status != :overdueStatus AND t.deleted = false")
    List<Task> findOverdueTasks(@Param("currentTime") LocalDateTime currentTime, @Param("overdueStatus") TaskStatus overdueStatus);
    
    default List<Task> findOverdueTasks(LocalDateTime currentTime) {
        return findOverdueTasks(currentTime, TaskStatus.OVERDUE);
    }
} 