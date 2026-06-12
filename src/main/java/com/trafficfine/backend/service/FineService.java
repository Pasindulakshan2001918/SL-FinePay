package com.trafficfine.backend.service;

import com.trafficfine.backend.dto.CreateFineRequest;
import com.trafficfine.backend.dto.FineResponse;
import com.trafficfine.backend.model.Fine;
import com.trafficfine.backend.model.FineCategory;
import com.trafficfine.backend.model.User;
import com.trafficfine.backend.repository.FineCategoryRepository;
import com.trafficfine.backend.repository.FineRepository;
import com.trafficfine.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FineService {

    private final FineRepository fineRepository;
    private final SmsService smsService;
    private final FineCategoryRepository fineCategoryRepository;
    private final UserRepository userRepository;

    // Look up a fine by reference number
    @Transactional
    public FineResponse getFine(String referenceNumber) {
        Fine fine = fineRepository.findByReferenceNumber(referenceNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fine not found"));
        return FineResponse.from(fine);
    }

    // Pay a fine
    @Transactional
    public FineResponse payFine(String referenceNumber) {
        Fine fine = fineRepository.findByReferenceNumber(referenceNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fine not found"));

        if (fine.getStatus() == Fine.Status.PAID) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fine already paid");
        }

        // Mark as paid
        fine.setStatus(Fine.Status.PAID);
        fine.setPaidAt(LocalDateTime.now());
        fineRepository.save(fine);

        // officer is still in session because of @Transactional
        String officerPhone = fine.getOfficer().getPhone();
        smsService.sendSms(officerPhone,
            "Fine " + referenceNumber + " has been paid. Driver may collect license.");

        return FineResponse.from(fine);
    }

    // Officer creates a fine from the Android app
    public Fine createFine(CreateFineRequest request, String officerUsername) {
        // 1. Find the officer who is logged in (from JWT token)
        User officer = userRepository.findByUsername(officerUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Officer not found"));

        // 2. Find the fine category by ID
        FineCategory category = fineCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        // 3. Make sure this reference number doesn't already exist
        if (fineRepository.findByReferenceNumber(request.getReferenceNumber()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reference number already exists");
        }

        // 4. Build the Fine object
        Fine fine = new Fine();
        fine.setReferenceNumber(request.getReferenceNumber());
        fine.setCategory(category);
        fine.setOfficer(officer);
        fine.setDriverPhone(request.getDriverPhone());
        fine.setDistrict(request.getDistrict());
        fine.setStatus(Fine.Status.UNPAID);
        fine.setIssuedAt(LocalDateTime.now());

        return fineRepository.save(fine);
    }

    // Admin: get stats
    public List<Object[]> getDistrictStats() {
        return fineRepository.getTotalCollectionByDistrict();
    }

    public List<Object[]> getCategoryStats() {
        return fineRepository.getCountByCategory();
    }
}