package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @GetMapping
    public ResponseEntity<List<?>> getAllNotifications(@RequestParam Long userId) {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<?>> getPendingNotifications(@RequestParam Long userId) {
        return ResponseEntity.ok(Collections.emptyList());
    }
} 