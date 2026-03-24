package com.studyspace.user.controller;

import com.studyspace.common.response.ApiResponse;
import com.studyspace.config.swagger.OpenApiConfig;
import com.studyspace.user.dto.response.AuthUserResponse;
import com.studyspace.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get current user",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    )
    @GetMapping("/me")
    public ApiResponse<AuthUserResponse> me(Authentication authentication) {
        return ApiResponse.success(
                "Get current user successfully",
                userService.getCurrentUser(authentication),
                HttpStatus.OK.value()
        );
    }
}