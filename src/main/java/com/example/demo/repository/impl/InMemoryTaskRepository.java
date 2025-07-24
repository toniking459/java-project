package com.example.demo.repository.impl;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.TaskRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("inmemory")
public class InMemoryTaskRepository implements TaskRepository {
    private final Map<Long, Task> tasks = new HashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(idGen.getAndIncrement());
        }
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> findById(Long id) {
        Task task = tasks.get(id);
        return (task != null && !task.isDeleted()) ? Optional.of(task) : Optional.empty();
    }

    @Override
    public List<Task> findByUserId(Long userId) {
        List<Task> result = new ArrayList<>();
        for (Task t : tasks.values()) {
            if (!t.isDeleted() && t.getUserId().equals(userId)) result.add(t);
        }
        return result;
    }

    @Override
    public List<Task> findByUserIdAndPending(Long userId) {
        List<Task> result = new ArrayList<>();
        for (Task t : tasks.values()) {
            if (!t.isDeleted() && t.getUserId().equals(userId) && t.getStatus() == TaskStatus.PENDING) result.add(t);
        }
        return result;
    }

    @Override
    public List<Task> findAll() {
        List<Task> result = new ArrayList<>();
        for (Task t : tasks.values()) {
            if (!t.isDeleted()) result.add(t);
        }
        return result;
    }

    @Override
    public void markDeleted(Long id) {
        Task t = tasks.get(id);
        if (t != null) t.setDeleted(true);
    }
} 