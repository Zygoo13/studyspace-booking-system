package com.studyspace.user.controller;

import com.studyspace.common.response.ApiResponse;
import com.studyspace.config.swagger.OpenApiConfig;
import com.studyspace.user.dto.request.UpdateUserStatusRequest;
import com.studyspace.user.dto.response.UserDetailResponse;
import com.studyspace.user.dto.response.UserSummaryResponse;
import com.studyspace.user.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Operation(
            summary = "Get all users",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    )
    @GetMapping
    public ApiResponse<List<UserSummaryResponse>> getAllUsers() {
        return ApiResponse.success(
                "Get users successfully",
                adminUserService.getAllUsers(),
                HttpStatus.OK.value()
        );
    }

    @Operation(
            summary = "Get user by id",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    )
    @GetMapping("/{id}")
    public ApiResponse<UserDetailResponse> getUserById(@PathVariable Long id) {
        return ApiResponse.success(
                "Get user successfully",
                adminUserService.getUserById(id),
                HttpStatus.OK.value()
        );
    }

    @Operation(
            summary = "Update user active status",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    )
    @PatchMapping("/{id}/status")
    public ApiResponse<UserDetailResponse> updateUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserStatusRequest request
    ) {
        return ApiResponse.success(
                "Update user status successfully",
                adminUserService.updateUserStatus(id, request.getActive()),
                HttpStatus.OK.value()
        );
    }
}