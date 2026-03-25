CREATE TABLE sessions (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          booking_id BIGINT NOT NULL,
                          actual_start DATETIME NOT NULL,
                          actual_end DATETIME NULL,
                          status VARCHAR(30) NOT NULL,
                          created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          CONSTRAINT fk_sessions_booking
                              FOREIGN KEY (booking_id) REFERENCES bookings(id),
                          CONSTRAINT uk_sessions_booking_id UNIQUE (booking_id)
);

CREATE INDEX idx_sessions_status ON sessions(status);
CREATE INDEX idx_sessions_actual_start ON sessions(actual_start);
CREATE INDEX idx_sessions_actual_end ON sessions(actual_end);