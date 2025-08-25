package com.example.udw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface User extends JpaRepository<com.example.udw.entity.User, Long> {
    Optional<com.example.udw.entity.User> findByUsername(String username);
}