package com.trafficfine.backend.controller;

import com.trafficfine.backend.dto.CreateFineRequest;
import com.trafficfine.backend.dto.FineResponse;
import com.trafficfine.backend.model.Fine;
import com.trafficfine.backend.service.FineService;
import com.trafficfine.backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FineController {

    private final FineService fineService;
    private final JwtService jwtService;

    // GET /api/fines/TF-2024-00123
    @GetMapping("/{referenceNumber}")
    public ResponseEntity<FineResponse> getFine(@PathVariable String referenceNumber) {
        return ResponseEntity.ok(fineService.getFine(referenceNumber));
    }

    // POST /api/fines/TF-2024-00123/pay
    @PostMapping("/{referenceNumber}/pay")
    public ResponseEntity<FineResponse> payFine(@PathVariable String referenceNumber) {
        return ResponseEntity.ok(fineService.payFine(referenceNumber));
    }

    // POST /api/fines/create — officer creates a fine after stopping a driver
    @PostMapping("/create")
    public ResponseEntity<Fine> createFine(
            @RequestBody CreateFineRequest request,
            @RequestHeader("Authorization") String authHeader) {

        // Extract the officer's username from their JWT token
        String token = authHeader.substring(7); // remove "Bearer "
        String officerUsername = jwtService.extractUsername(token);

        return ResponseEntity.ok(fineService.createFine(request, officerUsername));
    }
}