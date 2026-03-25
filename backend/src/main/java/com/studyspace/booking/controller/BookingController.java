package com.studyspace.booking.controller;

import com.studyspace.booking.dto.request.CreateBookingRequest;
import com.studyspace.booking.dto.response.BookingResponse;
import com.studyspace.booking.service.BookingQueryService;
import com.studyspace.booking.service.BookingService;
import com.studyspace.common.response.ApiResponse;
import com.studyspace.config.swagger.OpenApiConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingQueryService bookingQueryService;

    @Operation(
            summary = "Create web booking",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<BookingResponse> create(
            @Valid @RequestBody CreateBookingRequest request,
            Authentication authentication
    ) {
        return ApiResponse.success(
                "Create booking successfully",
                bookingService.createWebBooking(request, authentication),
                HttpStatus.CREATED.value()
        );
    }

    @Operation(
            summary = "Get my bookings",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    )
    @GetMapping("/me")
    public ApiResponse<List<BookingResponse>> getMyBookings(Authentication authentication) {
        return ApiResponse.success(
                "Get my bookings successfully",
                bookingQueryService.getMyBookings(authentication),
                HttpStatus.OK.value()
        );
    }

    @Operation(
            summary = "Get booking by id",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    )
    @GetMapping("/{id}")
    public ApiResponse<BookingResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(
                "Get booking successfully",
                bookingQueryService.getById(id),
                HttpStatus.OK.value()
        );
    }
}