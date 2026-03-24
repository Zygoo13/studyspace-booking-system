package com.studyspace.auth.service;

import com.studyspace.auth.entity.RefreshToken;
import com.studyspace.auth.repository.RefreshTokenRepository;
import com.studyspace.auth.util.TokenGenerator;
import com.studyspace.common.exception.BadRequestException;
import com.studyspace.user.entity.User;
import com.studyspace.config.security.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(TokenGenerator.generateRefreshToken())
                .user(user)
                .expiresAt(LocalDateTime.now().plusDays(jwtProperties.getRefreshTokenExpirationDays()))
                .isRevoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional(readOnly = true)
    public RefreshToken getValidRefreshTokenOrThrow(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Refresh token does not exist"));

        if (Boolean.TRUE.equals(refreshToken.getIsRevoked())) {
            throw new BadRequestException("Refresh token has been revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Refresh token has expired");
        }

        return refreshToken;
    }

    @Transactional
    public void revokeToken(RefreshToken refreshToken) {
        refreshToken.setIsRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void revokeAllActiveTokensByUser(User user) {
        var tokens = refreshTokenRepository.findAllByUserAndIsRevokedFalse(user);
        for (RefreshToken token : tokens) {
            token.setIsRevoked(true);
        }
        refreshTokenRepository.saveAll(tokens);
    }
}