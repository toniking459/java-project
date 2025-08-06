package com.example.demo.service.impl;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.JpaTaskRepository;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("h2")
public class H2TaskServiceImpl implements TaskService {
    private final JpaTaskRepository taskRepository;

    @Autowired
    public H2TaskServiceImpl(JpaTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    @Override
    public List<Task> getPendingTasks(Long userId) {
        return taskRepository.findByUserIdAndStatus(userId, TaskStatus.PENDING);
    }

    @Override
    public void deleteTask(Long taskId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        taskOpt.ifPresent(task -> {
            task.setDeleted(true);
            taskRepository.save(task);
        });
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }
} 