package com.studyspace.booking.service;

import com.studyspace.booking.dto.request.CreateBookingRequest;
import com.studyspace.booking.dto.request.CreateWalkInBookingRequest;
import com.studyspace.booking.dto.response.BookingResponse;
import com.studyspace.booking.entity.Booking;
import com.studyspace.booking.enums.BookingSource;
import com.studyspace.booking.enums.BookingStatus;
import com.studyspace.booking.mapper.BookingMapper;
import com.studyspace.booking.repository.BookingRepository;
import com.studyspace.combo.entity.ComboPlan;
import com.studyspace.combo.repository.ComboPlanRepository;
import com.studyspace.common.exception.BadRequestException;
import com.studyspace.common.exception.ConflictException;
import com.studyspace.common.exception.ResourceNotFoundException;
import com.studyspace.space.entity.SpaceUnit;
import com.studyspace.space.repository.SpaceUnitRepository;
import com.studyspace.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SpaceUnitRepository spaceUnitRepository;
    private final ComboPlanRepository comboPlanRepository;
    private final BookingMapper bookingMapper;
    private final BookingAvailabilityService bookingAvailabilityService;
    private final BookingCodeGenerator bookingCodeGenerator;
    private final CurrentUserResolver currentUserResolver;

    @Transactional
    public BookingResponse createWebBooking(CreateBookingRequest request, Authentication authentication) {
        User currentUser = currentUserResolver.resolve(authentication);
        LocalDateTime scheduledStart = LocalDateTime.of(request.getBookingDate(), request.getStartTime());

        Booking booking = createBookingInternal(
                request.getSpaceUnitId(),
                request.getComboPlanId(),
                request.getPartySize(),
                normalize(request.getContactName()),
                normalize(request.getContactPhone()),
                normalize(request.getContactEmail()),
                currentUser,
                BookingSource.WEB,
                scheduledStart
        );

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponse createWalkInBooking(CreateWalkInBookingRequest request) {
        LocalDateTime scheduledStart = LocalDateTime.now();

        Booking booking = createBookingInternal(
                request.getSpaceUnitId(),
                request.getComboPlanId(),
                request.getPartySize(),
                normalize(request.getContactName()),
                normalize(request.getContactPhone()),
                normalize(request.getContactEmail()),
                null,
                BookingSource.WALK_IN,
                scheduledStart
        );

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponse cancelByStaff(Long bookingId) {
        Booking booking = bookingRepository.findWithDetailsById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return bookingMapper.toResponse(booking);
        }

        if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CHECKED_IN) {
            throw new BadRequestException("Only CONFIRMED or NO_SHOW booking may be cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    private Booking createBookingInternal(
            Long spaceUnitId,
            Long comboPlanId,
            Integer partySize,
            String contactName,
            String contactPhone,
            String contactEmail,
            User customerUser,
            BookingSource bookingSource,
            LocalDateTime scheduledStart
    ) {
        if (scheduledStart.isBefore(LocalDateTime.now().minusMinutes(1))) {
            throw new BadRequestException("Scheduled start must not be in the past");
        }

        SpaceUnit spaceUnit = spaceUnitRepository.findWithTreeById(spaceUnitId)
                .orElseThrow(() -> new ResourceNotFoundException("Space unit not found with id: " + spaceUnitId));

        ComboPlan comboPlan = comboPlanRepository.findById(comboPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Combo plan not found with id: " + comboPlanId));

        validateSpaceAndCombo(spaceUnit, comboPlan, partySize);

        LocalDateTime scheduledEnd = scheduledStart.plusMinutes(comboPlan.getDurationMinutes());

        if (!bookingAvailabilityService.isAvailable(spaceUnit, scheduledStart, scheduledEnd)) {
            throw new ConflictException("Selected space is not available in the requested time range");
        }

        return Booking.builder()
                .bookingCode(bookingCodeGenerator.nextCode(LocalDateTime.now()))
                .customerUser(customerUser)
                .contactName(contactName)
                .contactPhone(contactPhone)
                .contactEmail(contactEmail)
                .partySize(partySize)
                .spaceUnit(spaceUnit)
                .comboPlan(comboPlan)
                .bookingSource(bookingSource)
                .scheduledStart(scheduledStart)
                .scheduledEnd(scheduledEnd)
                .status(BookingStatus.CONFIRMED)
                .build();
    }

    private void validateSpaceAndCombo(SpaceUnit spaceUnit, ComboPlan comboPlan, Integer partySize) {
        if (!bookingAvailabilityService.isOperational(spaceUnit)) {
            throw new BadRequestException("Selected space is not available for direct booking");
        }

        if (spaceUnit.getCapacity() < partySize) {
            throw new BadRequestException("Selected space does not have enough capacity");
        }

        if (!Boolean.TRUE.equals(comboPlan.getIsActive())) {
            throw new BadRequestException("Selected combo plan is inactive");
        }

        if (comboPlan.getApplicableSpaceType() != spaceUnit.getSpaceType()) {
            throw new BadRequestException("Selected combo plan does not apply to this space type");
        }

        if (comboPlan.getMinCapacity() != null && spaceUnit.getCapacity() < comboPlan.getMinCapacity()) {
            throw new BadRequestException("Selected combo plan does not apply to this space capacity");
        }

        if (comboPlan.getMaxCapacity() != null && spaceUnit.getCapacity() > comboPlan.getMaxCapacity()) {
            throw new BadRequestException("Selected combo plan does not apply to this space capacity");
        }

        String comboPriceGroup = normalize(comboPlan.getApplicablePriceGroup());
        String spacePriceGroup = normalize(spaceUnit.getPriceGroup());

        if (comboPriceGroup != null && !comboPriceGroup.equalsIgnoreCase(spacePriceGroup == null ? "" : spacePriceGroup)) {
            throw new BadRequestException("Selected combo plan does not apply to this price group");
        }
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}