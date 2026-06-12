package com.trafficfine.backend.controller;

import com.trafficfine.backend.dto.RegisterOfficerRequest;
import com.trafficfine.backend.model.User;
import com.trafficfine.backend.service.AuthService;
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
    private final AuthService authService;

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

    // POST /api/admin/register-officer — admin registers a new officer
    @PostMapping("/register-officer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerOfficer(@RequestBody RegisterOfficerRequest request) {
        User officer = authService.registerOfficer(request);
        officer.setPassword(null); // never send password back in response
        return ResponseEntity.ok(officer);
    }
}