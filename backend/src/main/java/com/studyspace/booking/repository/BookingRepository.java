package com.studyspace.booking.repository;

import com.studyspace.booking.entity.Booking;
import com.studyspace.booking.enums.BookingStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingCode(String bookingCode);

    @EntityGraph(attributePaths = {"customerUser", "spaceUnit", "spaceUnit.parent", "spaceUnit.parent.parent", "comboPlan"})
    List<Booking> findAllByCustomerUserIdOrderByCreatedAtDesc(Long customerUserId);

    @EntityGraph(attributePaths = {"customerUser", "spaceUnit", "spaceUnit.parent", "spaceUnit.parent.parent", "comboPlan"})
    List<Booking> findAllByContactPhoneOrderByCreatedAtDesc(String contactPhone);

    @EntityGraph(attributePaths = {"customerUser", "spaceUnit", "spaceUnit.parent", "spaceUnit.parent.parent", "comboPlan"})
    Optional<Booking> findWithDetailsById(Long id);

    @EntityGraph(attributePaths = {"customerUser", "spaceUnit", "spaceUnit.parent", "spaceUnit.parent.parent", "comboPlan"})
    List<Booking> findAllByStatusInAndScheduledStartLessThanAndScheduledEndGreaterThan(
            Collection<BookingStatus> statuses,
            LocalDateTime endExclusive,
            LocalDateTime startExclusive
    );
}