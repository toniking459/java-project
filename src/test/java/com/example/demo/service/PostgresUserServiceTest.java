package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.JpaUserRepository;
import com.example.demo.service.impl.PostgresUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostgresUserServiceTest {

    @Mock
    private JpaUserRepository userRepository;

    @InjectMocks
    private PostgresUserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setDeleted(false);
    }

    @Test
    void register_ShouldSaveAndReturnUser() {
        when(userRepository.findByUsernameAndDeletedFalse("testuser")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.register(testUser);

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        verify(userRepository, times(1)).findByUsernameAndDeletedFalse("testuser");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void register_WhenUsernameExists_ShouldThrowException() {
        when(userRepository.findByUsernameAndDeletedFalse("testuser")).thenReturn(Optional.of(testUser));

        assertThrows(IllegalArgumentException.class, () -> userService.register(testUser));
        verify(userRepository, times(1)).findByUsernameAndDeletedFalse("testuser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_WithValidCredentials_ShouldReturnUser() {
        when(userRepository.findByUsernameAndDeletedFalse("testuser")).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.login("testuser", "password123");

        assertTrue(result.isPresent());
        assertEquals(testUser.getId(), result.get().getId());
        assertEquals(testUser.getUsername(), result.get().getUsername());
        verify(userRepository, times(1)).findByUsernameAndDeletedFalse("testuser");
    }

    @Test
    void login_WithInvalidPassword_ShouldReturnEmpty() {
        when(userRepository.findByUsernameAndDeletedFalse("testuser")).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.login("testuser", "wrongpassword");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsernameAndDeletedFalse("testuser");
    }

    @Test
    void login_WithNonExistentUser_ShouldReturnEmpty() {
        when(userRepository.findByUsernameAndDeletedFalse("nonexistent")).thenReturn(Optional.empty());

        Optional<User> result = userService.login("nonexistent", "password123");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsernameAndDeletedFalse("nonexistent");
    }

    @Test
    void findById_ShouldReturnUserIfNotDeleted() {
        when(userRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(testUser.getId(), result.get().getId());
        verify(userRepository, times(1)).findByIdAndDeletedFalse(1L);
    }

    @Test
    void findById_ShouldReturnEmptyIfUserNotFound() {
        when(userRepository.findByIdAndDeletedFalse(999L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(999L);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByIdAndDeletedFalse(999L);
    }

    @Test
    void findAll_ShouldReturnAllNonDeletedUsers() {
        List<User> expectedUsers = Arrays.asList(testUser);
        when(userRepository.findByDeletedFalse()).thenReturn(expectedUsers);

        List<User> result = userService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUser.getId(), result.get(0).getId());
        verify(userRepository, times(1)).findByDeletedFalse();
    }
} 