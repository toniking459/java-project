package com.example.demo.service.impl;

import com.example.demo.config.RabbitMQConfig;
import com.example.demo.dto.TaskCreatedEvent;
import com.example.demo.service.MessageService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQMessageService implements MessageService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQMessageService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishTaskCreatedEvent(TaskCreatedEvent event) {
        System.out.println("Publishing task created event: " + event.getTaskId());
        try {
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.TASK_CREATED_EXCHANGE,
                RabbitMQConfig.TASK_CREATED_ROUTING_KEY,
                event
            );
            System.out.println("Task created event published successfully");
        } catch (Exception e) {
            System.err.println("Error publishing task created event: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 