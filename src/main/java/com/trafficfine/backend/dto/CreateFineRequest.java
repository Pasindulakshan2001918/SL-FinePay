package com.trafficfine.backend.dto;

import lombok.Data;

@Data
public class CreateFineRequest {
    private String referenceNumber;   // unique code on the physical fine sheet
    private Long categoryId;          // 1 = Speeding, 2 = Illegal Parking, etc.
    private String driverPhone;       // driver's mobile number
    private String district;          // e.g. "Colombo", "Kandy"
}
