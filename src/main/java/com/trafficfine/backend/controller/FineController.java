package com.trafficfine.backend.controller;

import com.trafficfine.backend.model.Fine;
import com.trafficfine.backend.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FineController {

    private final FineService fineService;

    // GET /api/fines/TF-2024-00123
    @GetMapping("/{referenceNumber}")
    public ResponseEntity<Fine> getFine(@PathVariable String referenceNumber) {
        return ResponseEntity.ok(fineService.getFine(referenceNumber));
    }

    // POST /api/fines/TF-2024-00123/pay
    @PostMapping("/{referenceNumber}/pay")
    public ResponseEntity<Fine> payFine(@PathVariable String referenceNumber) {
        return ResponseEntity.ok(fineService.payFine(referenceNumber));
    }
}