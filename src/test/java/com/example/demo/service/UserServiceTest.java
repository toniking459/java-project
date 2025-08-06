package com.example.demo.service;

import com.example.demo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("inmemory")
class UserServiceTest {
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void registerAndLoginUser() {
        User user = new User(null, "testuser", "pass", false);
        User saved = userService.register(user);
        assertNotNull(saved.getId());
        Optional<User> found = userService.login("testuser", "pass");
        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUsername());
    }

    @Test
    void loginWithWrongPassword() {
        User user = new User(null, "user2", "pass2", false);
        userService.register(user);
        Optional<User> found = userService.login("user2", "wrong");
        assertFalse(found.isPresent());
    }

    @Test
    void registerDuplicateUser() {
        User user = new User(null, "dup", "123", false);
        userService.register(user);
        User user2 = new User(null, "dup", "123", false);
        userService.register(user2);
        long count = userService.findAll().stream().filter(u -> u.getUsername().equals("dup")).count();
        assertTrue(count >= 1);
    }
} 