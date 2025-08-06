package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/users")
public class UserController {
    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok("Login stub");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Object user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
} 