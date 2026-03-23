package com.studyspace.auth.controller;

import com.studyspace.auth.dto.request.LoginRequest;
import com.studyspace.auth.dto.request.LogoutRequest;
import com.studyspace.auth.dto.request.RefreshTokenRequest;
import com.studyspace.auth.dto.request.RegisterRequest;
import com.studyspace.auth.dto.response.AuthTokenResponse;
import com.studyspace.auth.dto.response.AuthUserResponse;
import com.studyspace.auth.service.AuthService;
import com.studyspace.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AuthTokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.success(
                "Register successfully",
                authService.register(request),
                HttpStatus.CREATED.value()
        );
    }

    @PostMapping("/login")
    public ApiResponse<AuthTokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(
                "Login successfully",
                authService.login(request),
                HttpStatus.OK.value()
        );
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthTokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ApiResponse.success(
                "Refresh token successfully",
                authService.refresh(request),
                HttpStatus.OK.value()
        );
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@Valid @RequestBody LogoutRequest request) {
        authService.logout(request);
        return ApiResponse.success(
                "Logout successfully",
                null,
                HttpStatus.OK.value()
        );
    }

    @GetMapping("/me")
    public ApiResponse<AuthUserResponse> me(Authentication authentication) {
        return ApiResponse.success(
                "Get current user successfully",
                authService.getCurrentUser(authentication),
                HttpStatus.OK.value()
        );
    }
}