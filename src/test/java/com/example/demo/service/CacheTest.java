package com.example.demo.service;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
class CacheTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void testCacheManagerExists() {
        assertNotNull(cacheManager);
        assertNotNull(cacheManager.getCache("tasks"));
        assertNotNull(cacheManager.getCache("pendingTasks"));
        assertNotNull(cacheManager.getCache("task"));
    }

    @Test
    void testCacheForGetAllTasks() {
        Long userId = 1L;
        
        List<Task> firstCall = taskService.getAllTasks(userId);
        List<Task> secondCall = taskService.getAllTasks(userId);
        
        assertEquals(firstCall, secondCall);
    }

    @Test
    void testCacheForGetPendingTasks() {
        Long userId = 1L;
        
        List<Task> firstCall = taskService.getPendingTasks(userId);
        List<Task> secondCall = taskService.getPendingTasks(userId);
        
        assertEquals(firstCall, secondCall);
    }
} 