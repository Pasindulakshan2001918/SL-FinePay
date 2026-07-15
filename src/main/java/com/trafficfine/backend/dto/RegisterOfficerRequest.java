package com.trafficfine.backend.dto;

import lombok.Data;

@Data
public class RegisterOfficerRequest {
    private String username;   // officer's login name
    private String password;   // plain text — backend will encrypt it
    private String phone;      // officer's mobile number for SMS
}
