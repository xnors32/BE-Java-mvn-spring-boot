package com.inventorilab.service.interfaces;

import com.inventorilab.dto.request.LoginRequest;
import com.inventorilab.dto.request.RegisterRequest;
import com.inventorilab.dto.response.JwtResponse;
import com.inventorilab.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
    JwtResponse login(LoginRequest request);
}
