package com.trafficfine.backend;

import com.trafficfine.backend.model.*;
import com.trafficfine.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FineCategoryRepository categoryRepository;
    private final FineRepository fineRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Only seed if database is empty
        if (userRepository.count() > 0) return;

        // Create an admin user
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(User.Role.ADMIN);
        userRepository.save(admin);

        // Create an officer user
        User officer = new User();
        officer.setUsername("officer1");
        officer.setPassword(passwordEncoder.encode("pass123"));
        officer.setRole(User.Role.OFFICER);
        userRepository.save(officer);

        // Create fine categories
        FineCategory speeding = new FineCategory();
        speeding.setName("Speeding");
        speeding.setAmount(2500.0);
        categoryRepository.save(speeding);

        FineCategory parking = new FineCategory();
        parking.setName("Illegal Parking");
        parking.setAmount(1500.0);
        categoryRepository.save(parking);

        // Create a sample unpaid fine
        Fine fine = new Fine();
        fine.setReferenceNumber("TF-2024-00001");
        fine.setCategory(speeding);
        fine.setOfficer(officer);
        fine.setDriverPhone("0771234567");
        fine.setDistrict("Colombo");
        fine.setStatus(Fine.Status.UNPAID);
        fine.setIssuedAt(java.time.LocalDateTime.now());
        fineRepository.save(fine);

        System.out.println("✅ Sample data inserted!");
    }
}
