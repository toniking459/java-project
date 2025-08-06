package com.example.demo.service;

import com.example.demo.dto.TaskCreatedEvent;
import com.example.demo.model.Task;
import com.example.demo.model.TaskStatus;
import com.example.demo.repository.JpaNotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "spring.rabbitmq.host=localhost",
    "spring.rabbitmq.port=5672"
})
@RabbitListenerTest
class MessageBrokerTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private JpaNotificationRepository notificationRepository;

    @Test
    void testTaskCreationPublishesMessage() throws InterruptedException {
        Task task = new Task();
        task.setUserId(1L);
        task.setTitle("Test Task for Message Broker");
        task.setDescription("Test Description");
        task.setCreationDate(LocalDateTime.now());
        task.setStatus(TaskStatus.PENDING);

        Task createdTask = taskService.createTask(task);
        assertNotNull(createdTask.getId());

        TimeUnit.SECONDS.sleep(2);

        long notificationCount = notificationRepository.count();
        assertTrue(notificationCount > 0, "Notification should be created via message broker");
    }

    @Test
    void testTaskCreatedEventStructure() {
        TaskCreatedEvent event = new TaskCreatedEvent(
            1L, 1L, "Test Task", "Test Description", 
            LocalDateTime.now(), "PENDING"
        );

        assertNotNull(event.getTaskId());
        assertNotNull(event.getUserId());
        assertNotNull(event.getTitle());
        assertNotNull(event.getDescription());
        assertNotNull(event.getCreationDate());
        assertNotNull(event.getStatus());
    }
} 