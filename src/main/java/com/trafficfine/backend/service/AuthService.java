package com.trafficfine.backend.service;

import com.trafficfine.backend.model.User;
import com.trafficfine.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor   // Lombok: auto-generates constructor for final fields
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String login(String username, String password) {
        // 1. Find the user in database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Check if password matches
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        // 3. Generate and return JWT token
        return jwtService.generateToken(user.getUsername(), user.getRole().name());
    }
}