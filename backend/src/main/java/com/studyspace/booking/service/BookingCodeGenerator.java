package com.studyspace.booking.service;

import com.studyspace.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class BookingCodeGenerator {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final BookingRepository bookingRepository;

    public String nextCode(LocalDateTime now) {
        String prefix = "SS-" + now.format(DATE_FORMAT) + "-";

        for (int i = 0; i < 20; i++) {
            int random = ThreadLocalRandom.current().nextInt(1000, 10000);
            String candidate = prefix + random;
            if (bookingRepository.findByBookingCode(candidate).isEmpty()) {
                return candidate;
            }
        }

        return prefix + System.nanoTime();
    }
}