package com.trafficfine.backend.repository;

import com.trafficfine.backend.model.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface FineRepository extends JpaRepository<Fine, Long> {

    // Find a fine by its reference number
    Optional<Fine> findByReferenceNumber(String referenceNumber);

    // Find all fines by district (for admin stats)
    List<Fine> findByDistrict(String district);

    // Custom query: total amount collected per district
    @Query("SELECT f.district, SUM(f.category.amount) FROM Fine f WHERE f.status = 'PAID' GROUP BY f.district")
    List<Object[]> getTotalCollectionByDistrict();

    // Custom query: count fines by category
    @Query("SELECT f.category.name, COUNT(f) FROM Fine f GROUP BY f.category.name")
    List<Object[]> getCountByCategory();
}