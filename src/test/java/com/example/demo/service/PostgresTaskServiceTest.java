package com.example.demo.service;

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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostgresTaskServiceTest {

    @Mock
    private JpaTaskRepository taskRepository;

    @InjectMocks
    private PostgresTaskServiceImpl taskService;

    private Task testTask;
    private Long userId = 1L;

    @BeforeEach
    void setUp() {
        testTask = new Task();
        testTask.setId(1L);
        testTask.setUserId(userId);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setCreationDate(LocalDateTime.now());
        testTask.setTargetDate(LocalDateTime.now().plusDays(1));
        testTask.setStatus(TaskStatus.PENDING);
        testTask.setDeleted(false);
    }

    @Test
    void createTask_ShouldSaveAndReturnTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        Task result = taskService.createTask(testTask);

        assertNotNull(result);
        assertEquals(testTask.getId(), result.getId());
        assertEquals(testTask.getTitle(), result.getTitle());
        verify(taskRepository, times(1)).save(testTask);
    }

    @Test
    void getAllTasks_ShouldReturnNonDeletedTasksForUser() {
        List<Task> expectedTasks = Arrays.asList(testTask);
        when(taskRepository.findByUserIdAndDeletedFalse(userId)).thenReturn(expectedTasks);

        List<Task> result = taskService.getAllTasks(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testTask.getId(), result.get(0).getId());
        verify(taskRepository, times(1)).findByUserIdAndDeletedFalse(userId);
    }

    @Test
    void getPendingTasks_ShouldReturnPendingNonDeletedTasksForUser() {
        List<Task> expectedTasks = Arrays.asList(testTask);
        when(taskRepository.findByUserIdAndStatusAndDeletedFalse(userId, TaskStatus.PENDING))
                .thenReturn(expectedTasks);

        List<Task> result = taskService.getPendingTasks(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TaskStatus.PENDING, result.get(0).getStatus());
        verify(taskRepository, times(1)).findByUserIdAndStatusAndDeletedFalse(userId, TaskStatus.PENDING);
    }

    @Test
    void deleteTask_ShouldMarkTaskAsDeleted() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(testTask);
        assertTrue(testTask.isDeleted());
    }

    @Test
    void deleteTask_WhenTaskNotFound_ShouldDoNothing() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        taskService.deleteTask(999L);

        verify(taskRepository, times(1)).findById(999L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void findById_ShouldReturnTaskIfNotDeleted() {
        when(taskRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testTask));

        Optional<Task> result = taskService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(testTask.getId(), result.get().getId());
        verify(taskRepository, times(1)).findByIdAndDeletedFalse(1L);
    }

    @Test
    void findById_ShouldReturnEmptyIfTaskNotFound() {
        when(taskRepository.findByIdAndDeletedFalse(999L)).thenReturn(Optional.empty());

        Optional<Task> result = taskService.findById(999L);

        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findByIdAndDeletedFalse(999L);
    }
} 