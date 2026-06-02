package com.inventorilab.service.interfaces;

import com.inventorilab.dto.request.*;
import com.inventorilab.dto.response.JwtResponse;
import com.inventorilab.dto.response.UserResponse;

/**
 * Authentication Service Interface
 * 
 * Handles user registration and login for multiple authentication providers:
 * - Email/Password (Traditional)
 * - Google OAuth (Firebase)
 * - Phone OTP (Firebase)
 * 
 * Firebase Reference: https://firebase.google.com/docs/auth
 */
public interface AuthService {
    
    // Traditional Email/Password Authentication
    UserResponse register(RegisterRequest request);
    JwtResponse login(LoginRequest request);
    
    // Firebase Google OAuth Authentication
    // Firebase Reference: https://firebase.google.com/docs/auth/admin/verify-id-tokens
    JwtResponse loginWithGoogle(GoogleLoginRequest request);
    JwtResponse registerWithGoogle(GoogleRegisterRequest request);
    
    // Firebase Phone OTP Authentication
    // Firebase Reference: https://firebase.google.com/docs/auth/admin/verify-id-tokens
    JwtResponse loginWithPhone(PhoneLoginRequest request);
    JwtResponse registerWithPhone(PhoneRegisterRequest request);
}
