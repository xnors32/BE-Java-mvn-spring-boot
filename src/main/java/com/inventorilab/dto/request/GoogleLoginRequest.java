package com.inventorilab.dto.request;

import com.inventorilab.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Firebase Google OAuth Login Request DTO
 * 
 * Firebase Reference: https://firebase.google.com/docs/auth/web/google-signin
 * 
 * Frontend Flow:
 * 1. User clicks "Sign in with Google"
 * 2. Firebase signs in user and returns ID token
 * 3. Frontend sends this DTO with Firebase ID token to backend
 * 
 * Backend Flow:
 * 1. Validates ID token with Firebase Admin SDK
 * 2. Extracts user info (uid, email, name) from token
 * 3. Finds or creates user in database
 * 4. Generates JWT token and returns to frontend
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginRequest {

    @NotBlank(message = "Firebase ID token tidak boleh kosong!")
    // Firebase Reference: This is the ID token returned by firebase.auth().currentUser.getIdToken()
    private String idToken;
}
