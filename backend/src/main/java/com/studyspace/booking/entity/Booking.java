package com.studyspace.booking.entity;

import com.studyspace.booking.enums.BookingSource;
import com.studyspace.booking.enums.BookingStatus;
import com.studyspace.combo.entity.ComboPlan;
import com.studyspace.common.entity.BaseEntity;
import com.studyspace.space.entity.SpaceUnit;
import com.studyspace.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_code", nullable = false, unique = true, length = 50)
    private String bookingCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_user_id")
    private User customerUser;

    @Column(name = "contact_name", nullable = false, length = 100)
    private String contactName;

    @Column(name = "contact_phone", nullable = false, length = 20)
    private String contactPhone;

    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    @Column(name = "party_size", nullable = false)
    private Integer partySize;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "space_unit_id", nullable = false)
    private SpaceUnit spaceUnit;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "combo_plan_id", nullable = false)
    private ComboPlan comboPlan;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_source", nullable = false, length = 30)
    private BookingSource bookingSource;

    @Column(name = "scheduled_start", nullable = false)
    private LocalDateTime scheduledStart;

    @Column(name = "scheduled_end", nullable = false)
    private LocalDateTime scheduledEnd;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private BookingStatus status;
}