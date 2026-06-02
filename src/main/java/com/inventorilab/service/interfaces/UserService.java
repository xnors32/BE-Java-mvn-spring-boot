package com.inventorilab.service.interfaces;

import com.inventorilab.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse getById(Long id);
    Page<UserResponse> getAll(Pageable pageable);
    void delete(Long id);
}
