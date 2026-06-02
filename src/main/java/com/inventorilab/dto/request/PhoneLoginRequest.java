package com.inventorilab.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Firebase Phone OTP Login Request DTO
 * 
 * Firebase Reference: https://firebase.google.com/docs/auth/web/phone-auth
 * 
 * Frontend Flow:
 * 1. User enters phone number (e.g., +62812345678)
 * 2. Frontend triggers Firebase phone OTP
 * 3. Firebase sends OTP via SMS or WhatsApp
 * 4. User verifies OTP in frontend
 * 5. Firebase signs in user and returns ID token
 * 6. Frontend sends this DTO with Firebase ID token to backend
 * 
 * Backend Flow:
 * 1. Validates ID token with Firebase Admin SDK
 * 2. Extracts user info (uid, phone number) from token
 * 3. Finds or creates user in database with phone provider
 * 4. Generates JWT token and returns to frontend
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneLoginRequest {

    @NotBlank(message = "Firebase ID token tidak boleh kosong!")
    // Firebase Reference: This is the ID token returned by firebase.auth().currentUser.getIdToken()
    // User has already verified OTP at this point
    private String idToken;
}
