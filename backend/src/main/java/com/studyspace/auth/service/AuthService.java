package com.studyspace.auth.service;

import com.studyspace.auth.dto.request.RegisterRequest;
import com.studyspace.auth.dto.response.AuthUserResponse;
import com.studyspace.common.exception.BadRequestException;
import com.studyspace.user.entity.User;
import com.studyspace.user.enums.Role;
import com.studyspace.user.mapper.UserMapper;
import com.studyspace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public AuthUserResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName().trim())
                .phone(request.getPhone().trim())
                .email(email)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);
        return userMapper.toAuthUserResponse(savedUser);
    }
}