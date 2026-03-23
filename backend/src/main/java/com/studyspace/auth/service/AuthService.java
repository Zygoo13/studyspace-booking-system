package com.studyspace.auth.service;

import com.studyspace.auth.dto.request.LoginRequest;
import com.studyspace.auth.dto.request.LogoutRequest;
import com.studyspace.auth.dto.request.RefreshTokenRequest;
import com.studyspace.auth.dto.request.RegisterRequest;
import com.studyspace.auth.dto.response.AuthTokenResponse;
import com.studyspace.auth.dto.response.AuthUserResponse;
import com.studyspace.auth.entity.RefreshToken;
import com.studyspace.common.exception.BadRequestException;
import com.studyspace.common.exception.ResourceNotFoundException;
import com.studyspace.user.entity.User;
import com.studyspace.user.enums.Role;
import com.studyspace.user.mapper.UserMapper;
import com.studyspace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    @Transactional
    public AuthTokenResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email đã tồn tại");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(savedUser);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser);

        return AuthTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenExpirationSeconds())
                .build();
    }

    @Transactional
    public AuthTokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Email hoặc mật khẩu không đúng"));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new BadCredentialsException("Tài khoản đã bị khóa");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Email hoặc mật khẩu không đúng");
        }

        refreshTokenService.revokeAllActiveTokensByUser(user);

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenExpirationSeconds())
                .build();
    }

    @Transactional
    public AuthTokenResponse refresh(RefreshTokenRequest request) {
        RefreshToken oldRefreshToken =
                refreshTokenService.getValidRefreshTokenOrThrow(request.getRefreshToken());

        User user = oldRefreshToken.getUser();

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new BadCredentialsException("Tài khoản đã bị khóa");
        }

        refreshTokenService.revokeToken(oldRefreshToken);

        String newAccessToken = jwtService.generateAccessToken(user);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        return AuthTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenExpirationSeconds())
                .build();
    }

    @Transactional
    public void logout(LogoutRequest request) {
        RefreshToken refreshToken =
                refreshTokenService.getValidRefreshTokenOrThrow(request.getRefreshToken());

        refreshTokenService.revokeToken(refreshToken);
    }

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
            throw new BadCredentialsException("Tài khoản đã bị khóa");
        }

        return userMapper.toAuthUserResponse(user);
    }
}