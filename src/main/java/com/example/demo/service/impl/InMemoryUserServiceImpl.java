package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Profile("in-memory")
public class InMemoryUserServiceImpl implements UserService {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, User> usersByUsername = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public User register(User user) {

        if (usersByUsername.containsKey(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        user.setId(idCounter.getAndIncrement());
        user.setDeleted(false);
        users.put(user.getId(), user);
        usersByUsername.put(user.getUsername(), user);
        return user;
    }

    @Override
    public Optional<User> login(String username, String password) {
        User user = usersByUsername.get(username);
        if (user != null && !user.isDeleted() && user.getPassword().equals(password)) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = users.get(id);
        return user != null && !user.isDeleted() ? Optional.of(user) : Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return users.values().stream()
                .filter(user -> !user.isDeleted())
                .toList();
    }
} 