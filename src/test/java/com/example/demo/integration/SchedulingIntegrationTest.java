package com.example.demo.integration;

import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.JpaTaskRepository;
import com.example.demo.service.SchedulingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class SchedulingIntegrationTest {

    @Autowired
    private SchedulingService schedulingService;

    @Autowired
    private JpaTaskRepository taskRepository;

    @Test
    void testScheduledTaskDetection_IntegrationTest() {

        Task overdueTask = new Task();
        overdueTask.setUserId(1L);
        overdueTask.setTitle("Просроченная задача (интеграционный тест)");
        overdueTask.setDescription("Описание для интеграционного теста");
        overdueTask.setCreationDate(LocalDateTime.now().minusDays(3));
        overdueTask.setTargetDate(LocalDateTime.now().minusDays(1));
        overdueTask.setStatus(TaskStatus.PENDING);
        overdueTask.setDeleted(false);
        
        Task savedTask = taskRepository.save(overdueTask);


        Task normalTask = new Task();
        normalTask.setUserId(2L);
        normalTask.setTitle("Обычная задача (интеграционный тест)");
        normalTask.setDescription("Описание обычной задачи");
        normalTask.setCreationDate(LocalDateTime.now());
        normalTask.setTargetDate(LocalDateTime.now().plusDays(1));
        normalTask.setStatus(TaskStatus.PENDING);
        normalTask.setDeleted(false);
        
        taskRepository.save(normalTask);


        schedulingService.checkOverdueTasks();


        Task updatedTask = taskRepository.findById(savedTask.getId()).orElse(null);
        assertNotNull(updatedTask);
        assertEquals(TaskStatus.OVERDUE, updatedTask.getStatus());


        Task unchangedTask = taskRepository.findById(normalTask.getId()).orElse(null);
        assertNotNull(unchangedTask);
        assertEquals(TaskStatus.PENDING, unchangedTask.getStatus());
    }

    @Test
    void testFindOverdueTasksQuery() {

        LocalDateTime now = LocalDateTime.now();
        

        Task overdueTask1 = createTask(1L, "Просроченная 1", now.minusDays(2), TaskStatus.PENDING);
        taskRepository.save(overdueTask1);
        

        Task overdueTask2 = createTask(2L, "Просроченная 2", now.minusDays(1), TaskStatus.PENDING);
        taskRepository.save(overdueTask2);
        

        Task alreadyOverdueTask = createTask(3L, "Уже просроченная", now.minusDays(1), TaskStatus.OVERDUE);
        taskRepository.save(alreadyOverdueTask);
        

        Task futureTask = createTask(4L, "Будущая задача", now.plusDays(1), TaskStatus.PENDING);
        taskRepository.save(futureTask);
        

        Task deletedTask = createTask(5L, "Удаленная просроченная", now.minusDays(1), TaskStatus.PENDING);
        deletedTask.setDeleted(true);
        taskRepository.save(deletedTask);


        List<Task> overdueTasks = taskRepository.findOverdueTasks(now);


        assertEquals(2, overdueTasks.size());
        assertTrue(overdueTasks.stream().anyMatch(t -> t.getTitle().equals("Просроченная 1")));
        assertTrue(overdueTasks.stream().anyMatch(t -> t.getTitle().equals("Просроченная 2")));
        

        assertFalse(overdueTasks.stream().anyMatch(t -> t.getTitle().equals("Уже просроченная")));
        assertFalse(overdueTasks.stream().anyMatch(t -> t.getTitle().equals("Будущая задача")));
        assertFalse(overdueTasks.stream().anyMatch(t -> t.getTitle().equals("Удаленная просроченная")));
    }

    private Task createTask(Long userId, String title, LocalDateTime targetDate, TaskStatus status) {
        Task task = new Task();
        task.setUserId(userId);
        task.setTitle(title);
        task.setDescription("Описание для " + title);
        task.setCreationDate(LocalDateTime.now().minusDays(5));
        task.setTargetDate(targetDate);
        task.setStatus(status);
        task.setDeleted(false);
        return task;
    }
}