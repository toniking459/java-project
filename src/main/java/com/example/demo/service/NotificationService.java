package com.example.demo.service;

import com.example.demo.model.Notification;
import java.util.List;
import java.util.Optional;

public interface NotificationService {
    Notification createNotification(Notification notification);
    List<Notification> getAllNotifications(Long userId);
    List<Notification> getPendingNotifications(Long userId);
    Optional<Notification> findById(Long id);
} 