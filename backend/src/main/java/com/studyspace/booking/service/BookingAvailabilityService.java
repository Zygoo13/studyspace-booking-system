package com.studyspace.booking.service;

import com.studyspace.booking.entity.Booking;
import com.studyspace.booking.enums.BookingStatus;
import com.studyspace.booking.repository.BookingRepository;
import com.studyspace.space.entity.SpaceUnit;
import com.studyspace.space.enums.SpaceStatus;
import com.studyspace.space.enums.SpaceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingAvailabilityService {

    private static final EnumSet<BookingStatus> BLOCKING_STATUSES =
            EnumSet.of(BookingStatus.CONFIRMED, BookingStatus.CHECKED_IN);

    private final BookingRepository bookingRepository;

    @Transactional(readOnly = true)
    public boolean isAvailable(SpaceUnit target, LocalDateTime start, LocalDateTime end) {
        if (!isOperational(target)) {
            return false;
        }

        List<Booking> overlaps = bookingRepository
                .findAllByStatusInAndScheduledStartLessThanAndScheduledEndGreaterThan(
                        BLOCKING_STATUSES,
                        end,
                        start
                );

        return overlaps.stream().noneMatch(existing -> isConflict(target, existing.getSpaceUnit()));
    }

    public boolean isOperational(SpaceUnit target) {
        if (!Boolean.TRUE.equals(target.getIsDirectlyRentable())) {
            return false;
        }

        SpaceUnit current = target;
        while (current != null) {
            if (current.getStatus() != SpaceStatus.ACTIVE) {
                return false;
            }
            current = current.getParent();
        }

        if (target.getFloor() != null && target.getFloor().getStatus() != SpaceStatus.ACTIVE) {
            return false;
        }

        SpaceUnit room = findRoom(target);
        if (room != null && room.getFloor() != null && room.getFloor().getStatus() != SpaceStatus.ACTIVE) {
            return false;
        }

        return true;
    }

    private boolean isConflict(SpaceUnit requested, SpaceUnit existing) {
        if (requested.getId().equals(existing.getId())) {
            return true;
        }

        if (requested.getSpaceType() == SpaceType.SEAT) {
            Long requestedTableId = requested.getParent() != null ? requested.getParent().getId() : null;
            Long requestedRoomId = getRoomId(requested);

            if (existing.getSpaceType() == SpaceType.TABLE && requestedTableId != null && existing.getId().equals(requestedTableId)) {
                return true;
            }

            return existing.getSpaceType() == SpaceType.ROOM
                    && requestedRoomId != null
                    && existing.getId().equals(requestedRoomId);
        }

        if (requested.getSpaceType() == SpaceType.TABLE) {
            Long requestedRoomId = getRoomId(requested);

            if (existing.getSpaceType() == SpaceType.ROOM
                    && requestedRoomId != null
                    && existing.getId().equals(requestedRoomId)) {
                return true;
            }

            return existing.getSpaceType() == SpaceType.SEAT
                    && existing.getParent() != null
                    && existing.getParent().getId().equals(requested.getId());
        }

        if (requested.getSpaceType() == SpaceType.ROOM) {
            return isDescendantOf(existing, requested);
        }

        return false;
    }

    private boolean isDescendantOf(SpaceUnit child, SpaceUnit ancestor) {
        SpaceUnit current = child;
        while (current != null) {
            if (current.getId().equals(ancestor.getId())) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    private Long getRoomId(SpaceUnit spaceUnit) {
        SpaceUnit room = findRoom(spaceUnit);
        return room != null ? room.getId() : null;
    }

    private SpaceUnit findRoom(SpaceUnit spaceUnit) {
        SpaceUnit current = spaceUnit;
        while (current != null && current.getSpaceType() != SpaceType.ROOM) {
            current = current.getParent();
        }
        return current;
    }
}