package com.inventorilab.controller;

import com.inventorilab.dto.request.*;
import com.inventorilab.dto.response.JwtResponse;
import com.inventorilab.dto.response.UserResponse;
import com.inventorilab.response.WebResponse;
import com.inventorilab.service.interfaces.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<WebResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse userResponse = authService.register(request);
        WebResponse<UserResponse> response = WebResponse.<UserResponse>builder()
                .code(HttpStatus.CREATED.value())
                .status("CREATED")
                .message("Registrasi pengguna berhasil dilakukan!")
                .data(userResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<WebResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse jwtResponse = authService.login(request);
        WebResponse<JwtResponse> response = WebResponse.<JwtResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Login berhasil!")
                .data(jwtResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // Firebase auth (Google & Phone) dinonaktifkan sementara
    // Lihat: FirebaseConfig.java - diatur via app.firebase.enabled
    // ============================================================
}
