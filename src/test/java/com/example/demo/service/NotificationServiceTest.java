package com.example.demo.service;

import com.example.demo.model.Notification;
import com.example.demo.repository.impl.InMemoryNotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("inmemory")
class NotificationServiceTest {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private InMemoryNotificationRepository notificationRepository;

    private Long userId = 1L;

    @BeforeEach
    void setUp() {
        notificationRepository.clear();
    }

    @Test
    void createAndGetNotification() {
        Notification n = new Notification(null, userId, "msg", null, false, false);
        Notification created = notificationService.createNotification(n);
        assertNotNull(created.getId());
        List<Notification> all = notificationService.getAllNotifications(userId);
        assertFalse(all.isEmpty());
        assertEquals("msg", all.get(0).getMessage());
    }

    @Test
    void getPendingNotifications() {
        Notification n1 = new Notification(null, userId, "n1", null, false, false);
        Notification n2 = new Notification(null, userId, "n2", null, true, false);
        notificationService.createNotification(n1);
        notificationService.createNotification(n2);
        List<Notification> pending = notificationService.getPendingNotifications(userId);
        assertEquals(1, pending.size());
        assertEquals("n1", pending.get(0).getMessage());
    }
} 