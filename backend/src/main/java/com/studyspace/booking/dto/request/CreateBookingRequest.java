package com.studyspace.booking.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class CreateBookingRequest {

    @NotNull(message = "Space unit id is required")
    private Long spaceUnitId;

    @NotNull(message = "Combo plan id is required")
    private Long comboPlanId;

    @NotNull(message = "Party size is required")
    @Min(value = 1, message = "Party size must be at least 1")
    private Integer partySize;

    @NotNull(message = "Booking date is required")
    private LocalDate bookingDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotBlank(message = "Contact name must not be blank")
    @Size(max = 100, message = "Contact name must be at most 100 characters")
    private String contactName;

    @NotBlank(message = "Contact phone must not be blank")
    @Size(max = 20, message = "Contact phone must be at most 20 characters")
    private String contactPhone;

    @Email(message = "Contact email is invalid")
    @Size(max = 100, message = "Contact email must be at most 100 characters")
    private String contactEmail;
}