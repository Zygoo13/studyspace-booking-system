package com.studyspace.auth.service;

import com.studyspace.auth.entity.RefreshToken;
import com.studyspace.auth.repository.RefreshTokenRepository;
import com.studyspace.auth.util.TokenGenerator;
import com.studyspace.common.exception.BadRequestException;
import com.studyspace.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.security.refresh-token-expiration-days:7}")
    private long refreshTokenExpirationDays;

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        LocalDateTime now = LocalDateTime.now();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(TokenGenerator.generateRefreshToken())
                .user(user)
                .expiresAt(now.plusDays(refreshTokenExpirationDays))
                .isRevoked(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional(readOnly = true)
    public RefreshToken getValidRefreshTokenOrThrow(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Refresh token không tồn tại"));

        if (Boolean.TRUE.equals(refreshToken.getIsRevoked())) {
            throw new BadRequestException("Refresh token đã bị thu hồi");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Refresh token đã hết hạn");
        }

        return refreshToken;
    }

    @Transactional
    public void revokeToken(RefreshToken refreshToken) {
        refreshToken.setIsRevoked(true);
        refreshToken.setUpdatedAt(LocalDateTime.now());
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void revokeAllActiveTokensByUser(User user) {
        var tokens = refreshTokenRepository.findAllByUserAndIsRevokedFalse(user);
        LocalDateTime now = LocalDateTime.now();

        for (RefreshToken token : tokens) {
            token.setIsRevoked(true);
            token.setUpdatedAt(now);
        }

        refreshTokenRepository.saveAll(tokens);
    }
}