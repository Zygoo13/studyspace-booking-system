package com.studyspace.user.service;

import com.studyspace.common.exception.ResourceNotFoundException;
import com.studyspace.user.dto.response.AuthUserResponse;
import com.studyspace.user.entity.User;
import com.studyspace.user.mapper.UserMapper;
import com.studyspace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public AuthUserResponse getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadCredentialsException("Unauthenticated");
        }

        Object principal = authentication.getPrincipal();

        String email;
        if (principal instanceof Jwt jwt) {
            email = jwt.getSubject();
        } else {
            email = authentication.getName();
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new BadCredentialsException("Account is inactive");
        }

        return userMapper.toAuthUserResponse(user);
    }
}