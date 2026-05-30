package com.inventorilab.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public Auth Endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        
                        // Kategori Endpoints
                        .requestMatchers(HttpMethod.GET, "/api/kategori/**").hasAnyRole("ADMIN", "PETUGAS", "MAHASISWA")
                        .requestMatchers(HttpMethod.POST, "/api/kategori/**").hasAnyRole("ADMIN", "PETUGAS")
                        .requestMatchers(HttpMethod.PUT, "/api/kategori/**").hasAnyRole("ADMIN", "PETUGAS")
                        .requestMatchers(HttpMethod.DELETE, "/api/kategori/**").hasAnyRole("ADMIN", "PETUGAS")
                        
                        // Barang Endpoints
                        .requestMatchers(HttpMethod.GET, "/api/barang/**").hasAnyRole("ADMIN", "PETUGAS", "MAHASISWA")
                        .requestMatchers(HttpMethod.POST, "/api/barang/**").hasAnyRole("ADMIN", "PETUGAS")
                        .requestMatchers(HttpMethod.PUT, "/api/barang/**").hasAnyRole("ADMIN", "PETUGAS")
                        .requestMatchers(HttpMethod.DELETE, "/api/barang/**").hasAnyRole("ADMIN", "PETUGAS")
                        
                        // Peminjaman Actions (Approve, Reject, Kembalikan by Petugas/Admin)
                        .requestMatchers("/api/peminjaman/{id}/approve").hasAnyRole("ADMIN", "PETUGAS")
                        .requestMatchers("/api/peminjaman/{id}/reject").hasAnyRole("ADMIN", "PETUGAS")
                        .requestMatchers("/api/peminjaman/{id}/kembalikan").hasAnyRole("ADMIN", "PETUGAS")
                        
                        // Laporan Endpoints
                        .requestMatchers("/api/laporan/**").hasAnyRole("ADMIN", "PETUGAS")
                        
                        // Allow CORS preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // General Requests
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
