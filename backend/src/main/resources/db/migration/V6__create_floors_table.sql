CREATE TABLE floors (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        branch_id BIGINT NOT NULL,
                        name VARCHAR(100) NOT NULL,
                        status VARCHAR(30) NOT NULL,
                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        CONSTRAINT fk_floors_branch
                            FOREIGN KEY (branch_id) REFERENCES branches(id)
);

CREATE UNIQUE INDEX uk_floors_branch_name ON floors(branch_id, name);
CREATE INDEX idx_floors_branch_id ON floors(branch_id);
CREATE INDEX idx_floors_status ON floors(status);