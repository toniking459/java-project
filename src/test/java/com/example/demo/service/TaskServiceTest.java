package com.example.demo.service;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.impl.InMemoryTaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("inmemory")
class TaskServiceTest {
    @Autowired
    private TaskService taskService;

    @Autowired
    private InMemoryTaskRepository taskRepository;

    private Long userId = 1L;

    @BeforeEach
    void setUp() {
        taskRepository.clear();
    }

    @Test
    void createAndGetTask() {
        Task task = new Task(null, userId, "title", "desc", null, LocalDateTime.now().plusDays(1), false, TaskStatus.PENDING);
        Task created = taskService.createTask(task);
        assertNotNull(created.getId());
        List<Task> all = taskService.getAllTasks(userId);
        assertFalse(all.isEmpty());
        assertEquals("title", all.get(0).getTitle());
    }

    @Test
    void getPendingTasks() {
        Task t1 = new Task(null, userId, "t1", "d1", null, LocalDateTime.now().plusDays(1), false, TaskStatus.PENDING);
        Task t2 = new Task(null, userId, "t2", "d2", null, LocalDateTime.now().plusDays(2), false, TaskStatus.COMPLETED);
        taskService.createTask(t1);
        taskService.createTask(t2);
        List<Task> pending = taskService.getPendingTasks(userId);
        assertEquals(1, pending.size());
        assertEquals("t1", pending.get(0).getTitle());
    }

    @Test
    void softDeleteTask() {
        Task task = new Task(null, userId, "del", "desc", null, LocalDateTime.now().plusDays(1), false, TaskStatus.PENDING);
        Task created = taskService.createTask(task);
        taskService.deleteTask(created.getId());
        List<Task> all = taskService.getAllTasks(userId);
        assertTrue(all.isEmpty());
    }
} 