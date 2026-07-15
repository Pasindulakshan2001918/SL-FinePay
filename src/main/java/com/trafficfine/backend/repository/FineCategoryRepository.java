package com.trafficfine.backend.repository;

import com.trafficfine.backend.model.FineCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineCategoryRepository extends JpaRepository<FineCategory, Long> {
    // JpaRepository already gives you: findAll(), findById(), save(), delete()
    // No extra methods needed for now
}