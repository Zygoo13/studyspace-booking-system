package com.studyspace.auth.service;

import com.studyspace.auth.dto.request.LoginRequest;
import com.studyspace.auth.dto.request.LogoutRequest;
import com.studyspace.auth.dto.request.RefreshTokenRequest;
import com.studyspace.auth.dto.request.RegisterRequest;
import com.studyspace.auth.dto.response.AuthTokenResponse;
import com.studyspace.auth.entity.RefreshToken;
import com.studyspace.common.exception.BadRequestException;
import com.studyspace.user.entity.User;
import com.studyspace.user.enums.Role;
import com.studyspace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public AuthTokenResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .isActive(true)
                .build();

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
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new BadCredentialsException("Account is inactive");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
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
            throw new BadCredentialsException("Account is inactive");
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
}