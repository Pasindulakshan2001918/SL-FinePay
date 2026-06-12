package com.trafficfine.backend.dto;

import com.trafficfine.backend.model.Fine;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FineResponse {
    private Long id;
    private String referenceNumber;
    private String categoryName;
    private Double amount;
    private String district;
    private String status;
    private LocalDateTime issuedAt;
    private LocalDateTime paidAt;

    // Convert Fine entity → this DTO (safe, no lazy loading)
    public static FineResponse from(Fine fine) {
        FineResponse dto = new FineResponse();
        dto.setId(fine.getId());
        dto.setReferenceNumber(fine.getReferenceNumber());
        dto.setCategoryName(fine.getCategory().getName());
        dto.setAmount(fine.getCategory().getAmount());
        dto.setDistrict(fine.getDistrict());
        dto.setStatus(fine.getStatus().name());
        dto.setIssuedAt(fine.getIssuedAt());
        dto.setPaidAt(fine.getPaidAt());
        return dto;
    }
}
