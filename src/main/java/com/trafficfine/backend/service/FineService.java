package com.trafficfine.backend.service;

import com.trafficfine.backend.model.Fine;
import com.trafficfine.backend.repository.FineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FineService {

    private final FineRepository fineRepository;
    private final SmsService smsService;

    // Look up a fine by reference number
    public Fine getFine(String referenceNumber) {
        return fineRepository.findByReferenceNumber(referenceNumber)
                .orElseThrow(() -> new RuntimeException("Fine not found"));
    }

    // Pay a fine
    public Fine payFine(String referenceNumber) {
        Fine fine = getFine(referenceNumber);

        if (fine.getStatus() == Fine.Status.PAID) {
            throw new RuntimeException("Fine already paid");
        }

        // Mark as paid
        fine.setStatus(Fine.Status.PAID);
        fine.setPaidAt(LocalDateTime.now());
        fineRepository.save(fine);

        // Send SMS to the officer
        String officerPhone = fine.getOfficer().getUsername(); // store phone as username or add phone field
        smsService.sendSms(officerPhone,
            "Fine " + referenceNumber + " has been paid. Driver may collect license.");

        return fine;
    }

    // Admin: get stats
    public List<Object[]> getDistrictStats() {
        return fineRepository.getTotalCollectionByDistrict();
    }

    public List<Object[]> getCategoryStats() {
        return fineRepository.getCountByCategory();
    }
}