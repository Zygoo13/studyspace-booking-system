package com.studyspace.session.service;

import com.studyspace.booking.entity.Booking;
import com.studyspace.booking.enums.BookingStatus;
import com.studyspace.booking.repository.BookingRepository;
import com.studyspace.common.exception.BadRequestException;
import com.studyspace.common.exception.ConflictException;
import com.studyspace.common.exception.ResourceNotFoundException;
import com.studyspace.session.dto.response.SessionResponse;
import com.studyspace.session.entity.Session;
import com.studyspace.session.enums.SessionStatus;
import com.studyspace.session.mapper.SessionMapper;
import com.studyspace.session.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final BookingRepository bookingRepository;
    private final SessionMapper sessionMapper;

    @Transactional
    public SessionResponse checkIn(Long bookingId) {
        Booking booking = bookingRepository.findWithDetailsById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (sessionRepository.existsByBookingId(bookingId)) {
            throw new ConflictException("Session already exists for this booking");
        }

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new BadRequestException("Only CONFIRMED booking can be checked in");
        }

        Session session = Session.builder()
                .booking(booking)
                .actualStart(LocalDateTime.now())
                .status(SessionStatus.IN_PROGRESS)
                .build();

        booking.setStatus(BookingStatus.CHECKED_IN);

        Session saved = sessionRepository.save(session);
        bookingRepository.save(booking);

        return sessionMapper.toResponse(saved);
    }

    @Transactional
    public SessionResponse checkOut(Long sessionId) {
        Session session = sessionRepository.findWithDetailsById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + sessionId));

        if (session.getStatus() == SessionStatus.COMPLETED) {
            return sessionMapper.toResponse(session);
        }

        session.setActualEnd(LocalDateTime.now());
        session.setStatus(SessionStatus.COMPLETED);

        Booking booking = session.getBooking();
        booking.setStatus(BookingStatus.COMPLETED);

        Session saved = sessionRepository.save(session);
        bookingRepository.save(booking);

        return sessionMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public SessionResponse getById(Long sessionId) {
        return sessionMapper.toResponse(
                sessionRepository.findWithDetailsById(sessionId)
                        .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + sessionId))
        );
    }

    @Transactional(readOnly = true)
    public SessionResponse getByBookingId(Long bookingId) {
        return sessionMapper.toResponse(
                sessionRepository.findWithDetailsByBookingId(bookingId)
                        .orElseThrow(() -> new ResourceNotFoundException("Session not found for booking id: " + bookingId))
        );
    }

    @Transactional(readOnly = true)
    public List<SessionResponse> getInProgressSessions() {
        return sessionRepository.findAllByStatusOrderByActualStartDesc(SessionStatus.IN_PROGRESS)
                .stream()
                .map(sessionMapper::toResponse)
                .toList();
    }
}