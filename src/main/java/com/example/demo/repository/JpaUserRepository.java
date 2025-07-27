package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("h2")
public interface JpaUserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
} 