package com.inventorilab.mapper;

import com.inventorilab.dto.response.UserResponse;
import com.inventorilab.entity.User;

public class UserMapper {
    
    public static UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .idUser(user.getId())
                .nama(user.getNama())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
