package com.studyspace.booking.controller;

import com.studyspace.booking.dto.request.CreateWalkInBookingRequest;
import com.studyspace.booking.dto.response.BookingResponse;
import com.studyspace.booking.service.BookingQueryService;
import com.studyspace.booking.service.BookingService;
import com.studyspace.common.constant.ApiPaths;
import com.studyspace.common.response.ApiResponse;
import com.studyspace.config.swagger.OpenApiConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.API_STAFF + "/bookings")
@RequiredArgsConstructor
public class StaffBookingController {

    private final BookingService bookingService;
    private final BookingQueryService bookingQueryService;

    @Operation(
            summary = "Create walk-in booking",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    )
    @PostMapping("/walk-in")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<BookingResponse> createWalkIn(@Valid @RequestBody CreateWalkInBookingRequest request) {
        return ApiResponse.success(
                "Create walk-in booking successfully",
                bookingService.createWalkInBooking(request),
                HttpStatus.CREATED.value()
        );
    }

    @Operation(
            summary = "Get booking by booking code",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    )
    @GetMapping("/code/{bookingCode}")
    public ApiResponse<BookingResponse> getByCode(@PathVariable String bookingCode) {
        return ApiResponse.success(
                "Get booking successfully",
                bookingQueryService.getByCode(bookingCode),
                HttpStatus.OK.value()
        );
    }

    @Operation(
            summary = "Get bookings by contact phone",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    )
    @GetMapping("/search")
    public ApiResponse<List<BookingResponse>> getByContactPhone(@RequestParam String phone) {
        return ApiResponse.success(
                "Get bookings successfully",
                bookingQueryService.getByContactPhone(phone),
                HttpStatus.OK.value()
        );
    }

    @Operation(
            summary = "Cancel booking by staff",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME_NAME)
    )
    @PatchMapping("/{id}/cancel")
    public ApiResponse<BookingResponse> cancel(@PathVariable Long id) {
        return ApiResponse.success(
                "Cancel booking successfully",
                bookingService.cancelByStaff(id),
                HttpStatus.OK.value()
        );
    }
}