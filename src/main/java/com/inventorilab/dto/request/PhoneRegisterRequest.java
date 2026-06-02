package com.inventorilab.dto.request;

import com.inventorilab.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Firebase Phone OTP Registration Request DTO
 * 
 * Firebase Reference: https://firebase.google.com/docs/auth/web/phone-auth
 * 
 * Frontend Flow:
 * 1. User enters phone number (e.g., +62812345678)
 * 2. Frontend triggers Firebase phone OTP
 * 3. Firebase sends OTP via SMS or WhatsApp
 * 4. User verifies OTP in frontend
 * 5. Firebase signs in user and returns ID token
 * 6. User enters their full name (needed for profile)
 * 7. Frontend sends this DTO with ID token + name + role to backend
 * 
 * Backend Flow:
 * 1. Validates ID token with Firebase Admin SDK
 * 2. Extracts user info (uid, phone number) from Firebase token
 * 3. Creates new user in database with phone provider
 * 4. Sets user name from request
 * 5. Generates JWT token and returns to frontend (auto-login)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneRegisterRequest {

    @NotBlank(message = "Firebase ID token tidak boleh kosong!")
    // Firebase Reference: This is the ID token returned by firebase.auth().currentUser.getIdToken()
    // User has already verified OTP at this point
    private String idToken;

    @NotBlank(message = "Nama tidak boleh kosong!")
    @Size(min = 2, max = 100, message = "Nama harus berukuran antara 2 hingga 100 karakter!")
    // User's full name - required for phone registration
    // Firebase doesn't provide name for phone-only accounts
    private String nama;

    @NotNull(message = "Role tidak boleh kosong!")
    // Role determines user's permission level in the system
    // Values: MAHASISWA, PETUGAS, ADMIN
    private Role role;
}
