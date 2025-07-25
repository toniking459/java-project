package com.example.demo.repository.impl;

import com.example.demo.model.Notification;
import com.example.demo.repository.NotificationRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("inmemory")
public class InMemoryNotificationRepository implements NotificationRepository {
    private final Map<Long, Notification> notifications = new HashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @Override
    public Notification save(Notification notification) {
        if (notification.getId() == null) {
            notification.setId(idGen.getAndIncrement());
        }
        notifications.put(notification.getId(), notification);
        return notification;
    }

    @Override
    public Optional<Notification> findById(Long id) {
        Notification n = notifications.get(id);
        return (n != null && !n.isDeleted()) ? Optional.of(n) : Optional.empty();
    }

    @Override
    public List<Notification> findByUserId(Long userId) {
        List<Notification> result = new ArrayList<>();
        for (Notification n : notifications.values()) {
            if (!n.isDeleted() && n.getUserId().equals(userId)) result.add(n);
        }
        return result;
    }

    @Override
    public List<Notification> findByUserIdAndPending(Long userId) {
        List<Notification> result = new ArrayList<>();
        for (Notification n : notifications.values()) {
            if (!n.isDeleted() && n.getUserId().equals(userId) && !n.isRead()) result.add(n);
        }
        return result;
    }

    @Override
    public List<Notification> findAll() {
        List<Notification> result = new ArrayList<>();
        for (Notification n : notifications.values()) {
            if (!n.isDeleted()) result.add(n);
        }
        return result;
    }

    @Override
    public void clear() {
        notifications.clear();
        idGen.set(1);
    }
} 