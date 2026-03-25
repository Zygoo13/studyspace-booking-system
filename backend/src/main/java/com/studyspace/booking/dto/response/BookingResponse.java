package com.studyspace.booking.dto.response;

import com.studyspace.booking.enums.BookingSource;
import com.studyspace.booking.enums.BookingStatus;
import com.studyspace.space.enums.SpaceType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BookingResponse {

    private Long id;
    private String bookingCode;

    private Long customerUserId;

    private String contactName;
    private String contactPhone;
    private String contactEmail;

    private Integer partySize;

    private Long spaceUnitId;
    private String spaceName;
    private SpaceType spaceType;

    private Long comboPlanId;
    private String comboPlanName;

    private BookingSource bookingSource;
    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;
    private BookingStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}