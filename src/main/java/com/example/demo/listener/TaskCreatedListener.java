package com.example.demo.listener;

import com.example.demo.dto.TaskCreatedEvent;
import com.example.demo.model.Notification;
import com.example.demo.repository.JpaNotificationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskCreatedListener {

    private final JpaNotificationRepository notificationRepository;

    @Autowired
    public TaskCreatedListener(JpaNotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @RabbitListener(queues = "task.created.queue")
    public void handleTaskCreated(TaskCreatedEvent event) {
        Notification notification = new Notification();
        notification.setUserId(event.getUserId());
        notification.setMessage("New task created: " + event.getTitle());
        notification.setCreationDate(LocalDateTime.now());
        notification.setRead(false);
        notification.setDeleted(false);
        
        notificationRepository.save(notification);
        
        System.out.println("Notification created for task: " + event.getTaskId());
    }
} 