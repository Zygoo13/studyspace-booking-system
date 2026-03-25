package com.studyspace.session.controller;

import com.studyspace.common.constant.ApiPaths;
import com.studyspace.common.response.ApiResponse;
import com.studyspace.config.swagger.OpenApiConfig;
import com.studyspace.session.dto.response.SessionResponse;
import com.studyspace.session.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.API_STAFF + "/sessions")
@RequiredArgsConstructor
public class StaffSessionController {

    private final SessionService sessionService;

    @Operation(
            summary = "Check in booking",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    )
    @PostMapping("/check-in/{bookingId}")
    public ApiResponse<SessionResponse> checkIn(@PathVariable Long bookingId) {
        return ApiResponse.success(
                "Check in successfully",
                sessionService.checkIn(bookingId),
                HttpStatus.OK.value()
        );
    }

    @Operation(
            summary = "Check out session",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    )
    @PostMapping("/check-out/{sessionId}")
    public ApiResponse<SessionResponse> checkOut(@PathVariable Long sessionId) {
        return ApiResponse.success(
                "Check out successfully",
                sessionService.checkOut(sessionId),
                HttpStatus.OK.value()
        );
    }

    @Operation(
            summary = "Get session by id",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    )
    @GetMapping("/{sessionId}")
    public ApiResponse<SessionResponse> getById(@PathVariable Long sessionId) {
        return ApiResponse.success(
                "Get session successfully",
                sessionService.getById(sessionId),
                HttpStatus.OK.value()
        );
    }

    @Operation(
            summary = "Get session by booking id",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    )
    @GetMapping("/booking/{bookingId}")
    public ApiResponse<SessionResponse> getByBookingId(@PathVariable Long bookingId) {
        return ApiResponse.success(
                "Get session successfully",
                sessionService.getByBookingId(bookingId),
                HttpStatus.OK.value()
        );
    }

    @Operation(
            summary = "Get in-progress sessions",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    )
    @GetMapping("/in-progress")
    public ApiResponse<List<SessionResponse>> getInProgressSessions() {
        return ApiResponse.success(
                "Get in-progress sessions successfully",
                sessionService.getInProgressSessions(),
                HttpStatus.OK.value()
        );
    }
}