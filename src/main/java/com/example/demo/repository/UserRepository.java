package com.example.demo.repository;

import com.example.demo.model.User;
import java.util.Optional;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
} 