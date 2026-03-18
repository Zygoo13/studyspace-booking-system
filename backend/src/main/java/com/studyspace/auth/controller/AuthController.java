package com.studyspace.auth.controller;

import com.studyspace.auth.dto.request.RegisterRequest;
import com.studyspace.auth.dto.response.AuthUserResponse;
import com.studyspace.auth.service.AuthService;
import com.studyspace.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<AuthUserResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthUserResponse response = authService.register(request);
        return ApiResponse.success("Register successful", response);
    }
}