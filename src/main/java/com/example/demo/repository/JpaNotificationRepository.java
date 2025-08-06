package com.example.demo.repository;

import com.example.demo.model.Notification;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile({"h2", "postgres"})
public interface JpaNotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
    List<Notification> findByUserIdAndReadFalse(Long userId);
    List<Notification> findByUserIdAndDeletedFalse(Long userId);
    List<Notification> findByUserIdAndReadFalseAndDeletedFalse(Long userId);
    Optional<Notification> findByIdAndDeletedFalse(Long id);
} 