package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @GetMapping
    public ResponseEntity<List<?>> getAllTasks() {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<?>> getPendingTasks() {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Object task) {
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
} 