package com.example.demo.repository.impl;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("inmemory")
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGen.getAndIncrement());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = users.get(id);
        return (user != null && !user.isDeleted()) ? Optional.of(user) : Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(u -> !u.isDeleted() && u.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        for (User u : users.values()) {
            if (!u.isDeleted()) result.add(u);
        }
        return result;
    }
} 