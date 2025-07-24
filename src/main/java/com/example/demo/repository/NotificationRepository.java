package com.example.demo.repository;

import com.example.demo.model.Notification;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);
    Optional<Notification> findById(Long id);
    List<Notification> findByUserId(Long userId);
    List<Notification> findByUserIdAndPending(Long userId);
    List<Notification> findAll();
} 