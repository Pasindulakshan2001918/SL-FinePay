package com.trafficfine.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "fine_categories")
public class FineCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;       // e.g. "Speeding", "Illegal Parking"

    @Column(nullable = false)
    private Double amount;     // fine amount in LKR
}