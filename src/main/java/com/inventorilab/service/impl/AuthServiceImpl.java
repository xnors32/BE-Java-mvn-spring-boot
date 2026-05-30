package com.inventorilab.service.impl;

import com.inventorilab.dto.request.LoginRequest;
import com.inventorilab.dto.request.RegisterRequest;
import com.inventorilab.dto.response.JwtResponse;
import com.inventorilab.dto.response.UserResponse;
import com.inventorilab.entity.User;
import com.inventorilab.exception.BadRequestException;
import com.inventorilab.mapper.UserMapper;
import com.inventorilab.repository.UserRepository;
import com.inventorilab.security.CustomUserDetailsService;
import com.inventorilab.security.JwtService;
import com.inventorilab.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email '" + request.getEmail() + "' sudah terdaftar!");
        }

        User user = User.builder()
                .nama(request.getNama())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        User savedUser = userRepository.save(user);
        return UserMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public JwtResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Email atau password salah!"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);

        return JwtResponse.builder()
                .token(token)
                .user(UserMapper.toResponse(user))
                .build();
    }
}
