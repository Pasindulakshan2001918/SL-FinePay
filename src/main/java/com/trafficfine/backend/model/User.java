package com.trafficfine.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data                    // Lombok: auto-generates getters, setters, toString
@Entity                  // Tells JPA: make a database table for this
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto-increment ID
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;   // will be stored encrypted, never plain text

    private String phone;      // officer's mobile number for SMS

    @Enumerated(EnumType.STRING)
    private Role role;         // OFFICER or ADMIN

    public enum Role {
        OFFICER, ADMIN
    }
}