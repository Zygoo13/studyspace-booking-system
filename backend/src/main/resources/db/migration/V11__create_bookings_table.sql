CREATE TABLE bookings (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          booking_code VARCHAR(50) NOT NULL,
                          customer_user_id BIGINT NULL,
                          contact_name VARCHAR(100) NOT NULL,
                          contact_phone VARCHAR(20) NOT NULL,
                          contact_email VARCHAR(100) NULL,
                          party_size INT NOT NULL,
                          space_unit_id BIGINT NOT NULL,
                          combo_plan_id BIGINT NOT NULL,
                          booking_source VARCHAR(30) NOT NULL,
                          scheduled_start DATETIME NOT NULL,
                          scheduled_end DATETIME NOT NULL,
                          status VARCHAR(30) NOT NULL,
                          created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          CONSTRAINT uk_bookings_booking_code UNIQUE (booking_code),
                          CONSTRAINT fk_bookings_customer_user
                              FOREIGN KEY (customer_user_id) REFERENCES users(id),
                          CONSTRAINT fk_bookings_space_unit
                              FOREIGN KEY (space_unit_id) REFERENCES space_units(id),
                          CONSTRAINT fk_bookings_combo_plan
                              FOREIGN KEY (combo_plan_id) REFERENCES combo_plans(id)
);

CREATE INDEX idx_bookings_customer_user_id ON bookings(customer_user_id);
CREATE INDEX idx_bookings_contact_phone ON bookings(contact_phone);
CREATE INDEX idx_bookings_space_unit_id ON bookings(space_unit_id);
CREATE INDEX idx_bookings_combo_plan_id ON bookings(combo_plan_id);
CREATE INDEX idx_bookings_status ON bookings(status);
CREATE INDEX idx_bookings_scheduled_start_end ON bookings(scheduled_start, scheduled_end);