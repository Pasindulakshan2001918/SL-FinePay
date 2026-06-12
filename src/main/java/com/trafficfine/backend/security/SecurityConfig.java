package com.trafficfine.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity          // enables @PreAuthorize on controller methods
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())        // disable CSRF for REST APIs
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // login — public, anyone can call
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/error").permitAll() 
                // driver looks up a fine — public, no login needed
                .requestMatchers(HttpMethod.GET, "/api/fines/**").permitAll()
                // driver pays a fine — public, no login needed
                .requestMatchers(HttpMethod.POST, "/api/fines/*/pay").permitAll()
                // officer creates a fine — must be logged in as OFFICER
                .requestMatchers(HttpMethod.POST, "/api/fines/create").authenticated()
                // everything else (admin endpoints) — must be authenticated
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // This bean encrypts passwords using BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}