package com.inventorilab.dto.response;

import com.inventorilab.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long idUser;
    private String nama;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
}
