package com.example.demo.service;

import com.example.demo.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(User user);
    Optional<User> login(String username, String password);
    Optional<User> findById(Long id);
    List<User> findAll();
} 