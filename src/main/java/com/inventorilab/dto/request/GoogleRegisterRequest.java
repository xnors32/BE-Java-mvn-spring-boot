package com.inventorilab.dto.request;

import com.inventorilab.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Firebase Google OAuth Registration Request DTO
 * 
 * Firebase Reference: https://firebase.google.com/docs/auth/web/google-signin
 * 
 * Frontend Flow:
 * 1. User clicks "Sign up with Google"
 * 2. Firebase signs in user and returns ID token
 * 3. Frontend extracts user info from Google profile
 * 4. Frontend sends this DTO with Firebase ID token + role to backend
 * 
 * Backend Flow:
 * 1. Validates ID token with Firebase Admin SDK
 * 2. Extracts user info (uid, email, name) from Firebase token
 * 3. Creates new user in database with Google auth provider
 * 4. Generates JWT token and returns to frontend (auto-login)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleRegisterRequest {

    @NotBlank(message = "Firebase ID token tidak boleh kosong!")
    // Firebase Reference: This is the ID token returned by firebase.auth().currentUser.getIdToken()
    private String idToken;

    @NotNull(message = "Role tidak boleh kosong!")
    // Role determines user's permission level in the system
    // Values: MAHASISWA, PETUGAS, ADMIN
    private Role role;
}
