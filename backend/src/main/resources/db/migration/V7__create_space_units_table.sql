CREATE TABLE space_units (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             branch_id BIGINT NOT NULL,
                             floor_id BIGINT NULL,
                             parent_id BIGINT NULL,
                             space_type VARCHAR(30) NOT NULL,
                             name VARCHAR(100) NOT NULL,
                             capacity INT NOT NULL,
                             is_directly_rentable BOOLEAN NOT NULL DEFAULT FALSE,
                             status VARCHAR(30) NOT NULL,
                             price_group VARCHAR(50) NULL,
                             created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             CONSTRAINT fk_space_units_branch
                                 FOREIGN KEY (branch_id) REFERENCES branches(id),
                             CONSTRAINT fk_space_units_floor
                                 FOREIGN KEY (floor_id) REFERENCES floors(id),
                             CONSTRAINT fk_space_units_parent
                                 FOREIGN KEY (parent_id) REFERENCES space_units(id)
);

CREATE UNIQUE INDEX uk_space_units_parent_name ON space_units(parent_id, name);
CREATE INDEX idx_space_units_branch_id ON space_units(branch_id);
CREATE INDEX idx_space_units_floor_id ON space_units(floor_id);
CREATE INDEX idx_space_units_parent_id ON space_units(parent_id);
CREATE INDEX idx_space_units_space_type ON space_units(space_type);
CREATE INDEX idx_space_units_status ON space_units(status);
CREATE INDEX idx_space_units_rentable ON space_units(is_directly_rentable);
CREATE INDEX idx_space_units_price_group ON space_units(price_group);