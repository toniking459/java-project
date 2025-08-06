package com.example.demo.service;

import com.example.demo.dto.TaskCreatedEvent;

public interface MessageService {
    void publishTaskCreatedEvent(TaskCreatedEvent event);
} 