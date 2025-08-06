package com.example.demo.service.impl;

import com.example.demo.model.Notification;
import com.example.demo.repository.JpaNotificationRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Profile("inmemory")
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification createNotification(Notification notification) {
        notification.setCreationDate(LocalDateTime.now());
        notification.setDeleted(false);
<<<<<<< HEAD
=======

        // Не перезаписываем read, если оно уже задано
        // Если notification.isRead() == false по умолчанию, оставляем как есть

        notification.setRead(false);

>>>>>>> 8cee96d572f14ab4cf4a23753eb4c48803472306
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getAllNotifications(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public List<Notification> getPendingNotifications(Long userId) {
        return notificationRepository.findByUserIdAndPending(userId);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationRepository.findById(id);
    }
} 