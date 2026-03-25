package com.studyspace.session.dto.response;

import com.studyspace.booking.enums.BookingStatus;
import com.studyspace.session.enums.SessionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SessionResponse {

    private Long id;

    private Long bookingId;
    private String bookingCode;
    private BookingStatus bookingStatus;

    private Long spaceUnitId;
    private String spaceName;

    private String contactName;
    private String contactPhone;

    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;

    private LocalDateTime actualStart;
    private LocalDateTime actualEnd;

    private SessionStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}