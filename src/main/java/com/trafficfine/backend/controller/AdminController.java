package com.trafficfine.backend.controller;

import com.trafficfine.backend.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final FineService fineService;

    // GET /api/admin/stats — only ADMIN role can call this
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getStats() {
        List<Object[]> districtStats = fineService.getDistrictStats();
        List<Object[]> categoryStats = fineService.getCategoryStats();
        return ResponseEntity.ok(Map.of(
            "districtStats", districtStats,
            "categoryStats", categoryStats
        ));
    }
}