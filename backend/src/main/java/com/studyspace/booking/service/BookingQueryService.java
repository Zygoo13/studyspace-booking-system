package com.studyspace.booking.service;

import com.studyspace.booking.dto.response.BookingResponse;
import com.studyspace.booking.mapper.BookingMapper;
import com.studyspace.booking.repository.BookingRepository;
import com.studyspace.common.exception.ResourceNotFoundException;
import com.studyspace.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingQueryService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CurrentUserResolver currentUserResolver;

    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(Authentication authentication) {
        User currentUser = currentUserResolver.resolve(authentication);

        return bookingRepository.findAllByCustomerUserIdOrderByCreatedAtDesc(currentUser.getId())
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookingResponse getById(Long bookingId) {
        return bookingMapper.toResponse(
                bookingRepository.findWithDetailsById(bookingId)
                        .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId))
        );
    }

    @Transactional(readOnly = true)
    public BookingResponse getByCode(String bookingCode) {
        return bookingMapper.toResponse(
                bookingRepository.findByBookingCode(bookingCode)
                        .flatMap(booking -> bookingRepository.findWithDetailsById(booking.getId()))
                        .orElseThrow(() -> new ResourceNotFoundException("Booking not found with code: " + bookingCode))
        );
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getByContactPhone(String phone) {
        return bookingRepository.findAllByContactPhoneOrderByCreatedAtDesc(phone)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }
}