CREATE TABLE combo_plans (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             name VARCHAR(150) NOT NULL,
                             duration_minutes INT NOT NULL,
                             price DECIMAL(12,2) NOT NULL,
                             applicable_space_type VARCHAR(30) NOT NULL,
                             min_capacity INT NULL,
                             max_capacity INT NULL,
                             applicable_price_group VARCHAR(50) NULL,
                             is_active BOOLEAN NOT NULL DEFAULT TRUE,
                             created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_combo_plans_space_type ON combo_plans(applicable_space_type);
CREATE INDEX idx_combo_plans_is_active ON combo_plans(is_active);
CREATE INDEX idx_combo_plans_price_group ON combo_plans(applicable_price_group);