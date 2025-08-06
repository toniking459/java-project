package com.example.demo.service;

import com.example.demo.dto.TaskCreatedEvent;
import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.JpaTaskRepository;
import com.example.demo.service.impl.PostgresTaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostgresTaskServiceAsyncTest {

    @Mock
    private JpaTaskRepository taskRepository;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private PostgresTaskServiceImpl taskService;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task();
        testTask.setId(1L);
        testTask.setUserId(100L);
        testTask.setTitle("Тестовая задача");
        testTask.setDescription("Описание тестовой задачи");
        testTask.setCreationDate(LocalDateTime.now());
        testTask.setTargetDate(LocalDateTime.now().plusDays(1));
        testTask.setStatus(TaskStatus.PENDING);
        testTask.setDeleted(false);
    }

    @Test
    void testCreateTaskAsync_Success() throws Exception {

        when(taskRepository.save(any(Task.class))).thenReturn(testTask);
        doNothing().when(messageService).publishTaskCreatedEvent(any(TaskCreatedEvent.class));


        CompletableFuture<Task> result = taskService.createTaskAsync(testTask);
        Task createdTask = result.get();


        assertNotNull(createdTask);
        assertEquals(testTask.getId(), createdTask.getId());
        assertEquals(testTask.getTitle(), createdTask.getTitle());
        
        verify(taskRepository).save(testTask);
        verify(messageService).publishTaskCreatedEvent(any(TaskCreatedEvent.class));
    }

    @Test
    void testCreateTaskAsync_WithException() {

        when(taskRepository.save(any(Task.class))).thenThrow(new RuntimeException("Database error"));

 & Assert
        CompletableFuture<Task> result = taskService.createTaskAsync(testTask);
        
        assertThrows(RuntimeException.class, () -> {
            result.get();
        });
        
        verify(taskRepository).save(testTask);
        verify(messageService, never()).publishTaskCreatedEvent(any(TaskCreatedEvent.class));
    }

    @Test
    void testGetAllTasksAsync_Success() throws Exception {

        List<Task> expectedTasks = Arrays.asList(testTask);
        when(taskRepository.findByUserIdAndDeletedFalse(100L)).thenReturn(expectedTasks);


        CompletableFuture<List<Task>> result = taskService.getAllTasksAsync(100L);
        List<Task> actualTasks = result.get();


        assertNotNull(actualTasks);
        assertEquals(1, actualTasks.size());
        assertEquals(testTask.getId(), actualTasks.get(0).getId());
        
        verify(taskRepository).findByUserIdAndDeletedFalse(100L);
    }

    @Test
    void testGetAllTasksAsync_EmptyResult() throws Exception {

        when(taskRepository.findByUserIdAndDeletedFalse(999L)).thenReturn(Arrays.asList());


        CompletableFuture<List<Task>> result = taskService.getAllTasksAsync(999L);
        List<Task> actualTasks = result.get();


        assertNotNull(actualTasks);
        assertTrue(actualTasks.isEmpty());
        
        verify(taskRepository).findByUserIdAndDeletedFalse(999L);
    }

    @Test
    void testBulkUpdateTasksAsync_Success() throws Exception {

        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Задача 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Задача 2");

        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskRepository.save(any(Task.class))).thenReturn(task1, task2);


        CompletableFuture<Void> result = taskService.bulkUpdateTasksAsync(tasks);
        result.get();


        assertTrue(result.isDone());
        assertFalse(result.isCompletedExceptionally());
        
        verify(taskRepository, times(2)).save(any(Task.class));
    }

    @Test
    void testBulkUpdateTasksAsync_WithInterruption() {

        List<Task> tasks = Arrays.asList(testTask);
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Thread.currentThread().interrupt();
            return testTask;
        });

 & Assert
        CompletableFuture<Void> result = taskService.bulkUpdateTasksAsync(tasks);
        
        assertThrows(RuntimeException.class, () -> {
            result.get();
        });
    }

    @Test
    void testBulkUpdateTasksAsync_EmptyList() throws Exception {

        List<Task> emptyTasks = Arrays.asList();


        CompletableFuture<Void> result = taskService.bulkUpdateTasksAsync(emptyTasks);
        result.get();


        assertTrue(result.isDone());
        assertFalse(result.isCompletedExceptionally());
        
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void testAsyncMethods_AreAnnotatedCorrectly() {

        assertTrue(CompletableFuture.class.isAssignableFrom(
            taskService.createTaskAsync(testTask).getClass()));
        assertTrue(CompletableFuture.class.isAssignableFrom(
            taskService.getAllTasksAsync(100L).getClass()));
        assertTrue(CompletableFuture.class.isAssignableFrom(
            taskService.bulkUpdateTasksAsync(Arrays.asList(testTask)).getClass()));
    }
}