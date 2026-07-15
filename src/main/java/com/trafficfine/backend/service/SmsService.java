package com.trafficfine.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SmsService {

    // Sign up at notify.lk to get these values
    private static final String USER_ID = "YOUR_NOTIFY_LK_USER_ID";
    private static final String API_KEY = "YOUR_NOTIFY_LK_API_KEY";
    private static final String SENDER_ID = "NotifyDEMO";

    public void sendSms(String phoneNumber, String message) {
        try {
            String url = UriComponentsBuilder
                .fromHttpUrl("https://app.notify.lk/api/v1/send")
                .queryParam("user_id", USER_ID)
                .queryParam("api_key", API_KEY)
                .queryParam("sender_id", SENDER_ID)
                .queryParam("to", phoneNumber)
                .queryParam("message", message)
                .toUriString();

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            // Log error but don't crash the payment if SMS fails
            System.err.println("SMS failed: " + e.getMessage());
        }
    }
}