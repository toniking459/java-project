package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;
    
    @Column(name = "target_date")
    private LocalDateTime targetDate;
    
    @Column(nullable = false)
    private boolean deleted = false;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    public Task() {}

    public Task(Long id, Long userId, String title, String description, LocalDateTime creationDate, LocalDateTime targetDate, boolean deleted, TaskStatus status) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.targetDate = targetDate;
        this.deleted = deleted;
        this.status = status;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
    public LocalDateTime getTargetDate() { return targetDate; }
    public void setTargetDate(LocalDateTime targetDate) { this.targetDate = targetDate; }
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
} 