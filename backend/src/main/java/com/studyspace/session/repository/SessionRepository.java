package com.studyspace.session.repository;

import com.studyspace.session.entity.Session;
import com.studyspace.session.enums.SessionStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    boolean existsByBookingId(Long bookingId);

    @EntityGraph(attributePaths = {"booking", "booking.spaceUnit", "booking.comboPlan"})
    Optional<Session> findWithDetailsById(Long id);

    @EntityGraph(attributePaths = {"booking", "booking.spaceUnit", "booking.comboPlan"})
    Optional<Session> findWithDetailsByBookingId(Long bookingId);

    @EntityGraph(attributePaths = {"booking", "booking.spaceUnit", "booking.comboPlan"})
    List<Session> findAllByStatusOrderByActualStartDesc(SessionStatus status);
}