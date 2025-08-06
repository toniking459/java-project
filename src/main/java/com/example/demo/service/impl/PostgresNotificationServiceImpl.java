package com.example.demo.service.impl;

import com.example.demo.model.Notification;
import com.example.demo.repository.JpaNotificationRepository;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("postgres")
public class PostgresNotificationServiceImpl implements NotificationService {
    private final JpaNotificationRepository notificationRepository;

    @Autowired
    public PostgresNotificationServiceImpl(JpaNotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification createNotification(Notification notification) {
        throw new UnsupportedOperationException("Notifications can only be created through message broker events");
    }

    @Override
    public List<Notification> getAllNotifications(Long userId) {
        return notificationRepository.findByUserIdAndDeletedFalse(userId);
    }

    @Override
    public List<Notification> getPendingNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadFalseAndDeletedFalse(userId);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationRepository.findByIdAndDeletedFalse(id);
    }
} 