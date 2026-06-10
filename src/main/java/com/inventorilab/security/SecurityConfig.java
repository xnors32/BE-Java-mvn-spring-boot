package com.inventorilab.security;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Security Configuration untuk Sistem Inventaris Laboratorium
 *
 * Features:
 * - JWT Authentication
 * - CORS untuk Frontend Access
 * - Role-based Authorization
 * - Stateless Session Management
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Value("${app.cors.allowed-origins:https://frontend-baru.vercel.app}")
    private String allowedOriginsFromEnv;

    /**
     * Security Filter Chain Configuration
     * Mengatur authentication, authorization, dan CORS
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {
        http
            // Disable CSRF (karena menggunakan JWT)
            .csrf(AbstractHttpConfigurer::disable)

            // Enable CORS dengan default settings
            .cors(Customizer.withDefaults())

            // Stateless session management (REST API)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Authorization configuration
            .authorizeHttpRequests(auth ->
                auth
                    // =============== PUBLIC ENDPOINTS ===============
                    // Health check endpoint
                    .requestMatchers("/health")
                    .permitAll()

                    // Authentication endpoints (Register & Login)
                    .requestMatchers("/api/auth/**")
                    .permitAll()

                    // =============== KATEGORI ENDPOINTS ===============
                    // Read kategori (semua role)
                    .requestMatchers(HttpMethod.GET, "/api/kategori/**")
                    .hasAnyRole("ADMIN", "PETUGAS", "MAHASISWA")
                    // Create, Update, Delete kategori (hanya Admin & Petugas)
                    .requestMatchers(HttpMethod.POST, "/api/kategori/**")
                    .hasAnyRole("ADMIN", "PETUGAS")
                    .requestMatchers(HttpMethod.PUT, "/api/kategori/**")
                    .hasAnyRole("ADMIN", "PETUGAS")
                    .requestMatchers(HttpMethod.DELETE, "/api/kategori/**")
                    .hasAnyRole("ADMIN", "PETUGAS")

                    // =============== BARANG ENDPOINTS ===============
                    // Read barang (semua role)
                    .requestMatchers(HttpMethod.GET, "/api/barang/**")
                    .hasAnyRole("ADMIN", "PETUGAS", "MAHASISWA")
                    // Create, Update, Delete barang (hanya Admin & Petugas)
                    .requestMatchers(HttpMethod.POST, "/api/barang/**")
                    .hasAnyRole("ADMIN", "PETUGAS")
                    .requestMatchers(HttpMethod.PUT, "/api/barang/**")
                    .hasAnyRole("ADMIN", "PETUGAS")
                    .requestMatchers(HttpMethod.DELETE, "/api/barang/**")
                    .hasAnyRole("ADMIN", "PETUGAS")

                    // =============== PEMINJAMAN ENDPOINTS ===============
                    // Read peminjaman (semua role bisa read sendirinya/berdasarkan role)
                    .requestMatchers(HttpMethod.GET, "/api/peminjaman/**")
                    .hasAnyRole("ADMIN", "PETUGAS", "MAHASISWA")
                    // Create peminjaman (semua role)
                    .requestMatchers(HttpMethod.POST, "/api/peminjaman")
                    .hasAnyRole("ADMIN", "PETUGAS", "MAHASISWA")
                    // Approve, Reject, Kembalikan (hanya Admin & Petugas)
                    .requestMatchers("/api/peminjaman/{id}/approve")
                    .hasAnyRole("ADMIN", "PETUGAS")
                    .requestMatchers("/api/peminjaman/{id}/reject")
                    .hasAnyRole("ADMIN", "PETUGAS")
                    .requestMatchers(
                        "/api/peminjaman/{id}/return",
                        "/api/peminjaman/{id}/kembalikan"
                    )
                    .hasAnyRole("ADMIN", "PETUGAS")

                    // =============== DETAIL PEMINJAMAN ENDPOINTS ===============
                    // Read detail (semua role)
                    .requestMatchers(HttpMethod.GET, "/api/detail-peminjaman/**")
                    .hasAnyRole("ADMIN", "PETUGAS", "MAHASISWA")
                    // Update kondisi kembali (hanya Admin & Petugas)
                    .requestMatchers(HttpMethod.PATCH, "/api/detail-peminjaman/**")
                    .hasAnyRole("ADMIN", "PETUGAS")

                    // =============== LAPORAN ENDPOINTS ===============
                    .requestMatchers("/api/laporan/**")
                    .hasAnyRole("ADMIN", "PETUGAS")

                    // =============== SHOP ENDPOINTS ===============
                    // Read shop products (semua role)
                    .requestMatchers(HttpMethod.GET, "/api/shop/**")
                    .hasAnyRole("ADMIN", "PETUGAS", "MAHASISWA")
                    // Create, Update, Delete shop products (hanya ADMIN)
                    .requestMatchers(HttpMethod.POST, "/api/shop/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/shop/**")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/shop/**")
                    .hasRole("ADMIN")

                    // =============== USER ENDPOINTS ===============
                    .requestMatchers("/api/users/**")
                    .hasAnyRole("ADMIN", "PETUGAS")

                    // =============== CORS PREFLIGHT ===============
                    // Allow OPTIONS (CORS preflight) untuk semua endpoints
                    .requestMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()

                    // =============== DEFAULT ===============
                    // Semua request lain harus authenticated
                    .anyRequest()
                    .authenticated()
            )

            // Set authentication provider
            .authenticationProvider(authenticationProvider())

            // Add JWT filter sebelum UsernamePasswordAuthenticationFilter
            .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    /**
     * CORS Configuration Source
     * Mengonfigurasi CORS untuk semua API endpoints
     *
     * Development: Mengizinkan localhost dari berbagai port
     * Production: Set specific origins di application.yml atau environment variable
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // =============== ALLOWED ORIGINS ===============
        // Development: Allow localhost dan 127.0.0.1 dari berbagai port
        // Production: Ubah ke specific domain(s)
        List<String> origins = new java.util.ArrayList<>(Arrays.asList(
            "http://localhost:5173",
            "http://127.0.0.1:5173",
            "http://localhost:3000",
            "http://localhost:8080",
            "http://localhost:4173",
            "http://127.0.0.1:3000"
        ));
        if (allowedOriginsFromEnv != null && !allowedOriginsFromEnv.isBlank()) {
            for (String o : allowedOriginsFromEnv.split(",")) {
                o = o.trim();
                if (!o.isEmpty()) origins.add(o);
            }
        }
        config.setAllowedOrigins(origins);

        // =============== ALLOWED METHODS ===============
        // Support REST methods: GET, POST, PUT, PATCH, DELETE
        config.setAllowedMethods(
            Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS",
                "HEAD"
            )
        );

        // =============== ALLOWED HEADERS ===============
        // Allow all headers dari client (Content-Type, Authorization, Custom Headers)
        config.setAllowedHeaders(Arrays.asList("*"));

        // =============== EXPOSED HEADERS ===============
        // Headers yang bisa diakses client dari response
        config.setExposedHeaders(
            Arrays.asList(
                "Authorization", // JWT Token
                "Content-Type",
                "X-Total-Count", // For pagination info
                "X-Page-Number",
                "X-Page-Size",
                "X-Error-Message" // For error info
            )
        );

        // =============== CREDENTIALS ===============
        // Allow credentials (cookies, auth headers, SSL client certs)
        config.setAllowCredentials(true);

        // =============== MAX AGE ===============
        // Cache preflight response untuk 1 hour (3600 seconds)
        config.setMaxAge(3600L);

        // Register CORS configuration untuk semua /api/** endpoints
        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return source;
    }

    /**
     * Authentication Provider
     * Menggunakan DAO dengan database user details
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider =
            new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Authentication Manager
     * Used untuk authenticate user credentials
     */
    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Password Encoder
     * Menggunakan BCrypt untuk hash password
     * Strength: 10 (default, balance antara security & performance)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
