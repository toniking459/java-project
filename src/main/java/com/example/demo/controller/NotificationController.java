package com.example.demo.controller;

import com.example.demo.model.Notification;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications(@RequestParam Long userId) {
        return ResponseEntity.ok(notificationService.getAllNotifications(userId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Notification>> getPendingNotifications(@RequestParam Long userId) {
        return ResponseEntity.ok(notificationService.getPendingNotifications(userId));
    }
} 