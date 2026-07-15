package com.trafficfine.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "fines")
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String referenceNumber;   // e.g. "TF-2024-00123"

    @ManyToOne                        // Many fines can belong to one category
    @JoinColumn(name = "category_id")
    private FineCategory category;

    @ManyToOne                        // Many fines can be issued by one officer
    @JoinColumn(name = "officer_id")
    private User officer;

    private String driverPhone;       // driver's phone — not stored here by default
    private String district;          // e.g. "Colombo", "Kandy"

    @Enumerated(EnumType.STRING)
    private Status status = Status.UNPAID;   // default is UNPAID

    private LocalDateTime issuedAt;
    private LocalDateTime paidAt;

    public enum Status {
        UNPAID, PAID
    }
}