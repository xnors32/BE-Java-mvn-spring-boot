package com.inventorilab.service.impl;

import com.inventorilab.dto.response.UserResponse;
import com.inventorilab.entity.User;
import com.inventorilab.exception.ResourceNotFoundException;
import com.inventorilab.mapper.UserMapper;
import com.inventorilab.repository.UserRepository;
import com.inventorilab.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan ID " + id + " tidak ditemukan!"));
        return UserMapper.toResponse(user);
    }

    @Override
    public Page<UserResponse> getAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserMapper::toResponse);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan ID " + id + " tidak ditemukan!"));
        userRepository.delete(user);
    }
}
