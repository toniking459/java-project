package com.example.demo.dto;

import java.time.LocalDateTime;

public class TaskCreatedEvent {
    private Long taskId;
    private Long userId;
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private String status;

    public TaskCreatedEvent() {}

    public TaskCreatedEvent(Long taskId, Long userId, String title, String description, LocalDateTime creationDate, String status) {
        this.taskId = taskId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.status = status;
    }

    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
} 