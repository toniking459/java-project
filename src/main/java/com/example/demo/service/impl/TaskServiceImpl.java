package com.example.demo.service.impl;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Profile("inmemory")
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(Task task) {
        task.setCreationDate(LocalDateTime.now());
        if (task.getStatus() == null) task.setStatus(TaskStatus.PENDING);
        task.setDeleted(false);
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    @Override
    public List<Task> getPendingTasks(Long userId) {
        return taskRepository.findByUserIdAndPending(userId);
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.markDeleted(taskId);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }
} 