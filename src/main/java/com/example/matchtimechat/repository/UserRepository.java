package com.example.matchtimechat.repository;

import com.example.matchtimechat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);  // Проверка на существование email
}
