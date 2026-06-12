package com.trafficfine.backend.service;

import com.trafficfine.backend.dto.RegisterOfficerRequest;
import com.trafficfine.backend.model.User;
import com.trafficfine.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor   // Lombok: auto-generates constructor for final fields
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String login(String username, String password) {
        // 1. Find the user in database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        // 2. Check if password matches
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong password");
        }

        // 3. Generate and return JWT token
        return jwtService.generateToken(user.getUsername(), user.getRole().name());
    }

    public User registerOfficer(RegisterOfficerRequest request) {
        // 1. Make sure the username is not already taken
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        // 2. Build the User object
        User officer = new User();
        officer.setUsername(request.getUsername());
        officer.setPassword(passwordEncoder.encode(request.getPassword())); // encrypt
        officer.setPhone(request.getPhone());
        officer.setRole(User.Role.OFFICER);   // role is always OFFICER — frontend cannot change this

        // 3. Save and return
        return userRepository.save(officer);
    }
}