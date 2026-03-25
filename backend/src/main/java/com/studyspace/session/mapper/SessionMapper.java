package com.studyspace.session.mapper;

import com.studyspace.session.dto.response.SessionResponse;
import com.studyspace.session.entity.Session;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

    public SessionResponse toResponse(Session session) {
        return SessionResponse.builder()
                .id(session.getId())
                .bookingId(session.getBooking().getId())
                .bookingCode(session.getBooking().getBookingCode())
                .bookingStatus(session.getBooking().getStatus())
                .spaceUnitId(session.getBooking().getSpaceUnit().getId())
                .spaceName(session.getBooking().getSpaceUnit().getName())
                .contactName(session.getBooking().getContactName())
                .contactPhone(session.getBooking().getContactPhone())
                .scheduledStart(session.getBooking().getScheduledStart())
                .scheduledEnd(session.getBooking().getScheduledEnd())
                .actualStart(session.getActualStart())
                .actualEnd(session.getActualEnd())
                .status(session.getStatus())
                .createdAt(session.getCreatedAt())
                .updatedAt(session.getUpdatedAt())
                .build();
    }
}