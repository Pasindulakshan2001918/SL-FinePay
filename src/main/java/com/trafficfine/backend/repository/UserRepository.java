package com.trafficfine.backend.repository;

import com.trafficfine.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Spring auto-generates the SQL: SELECT * FROM users WHERE username = ?
    Optional<User> findByUsername(String username);
}