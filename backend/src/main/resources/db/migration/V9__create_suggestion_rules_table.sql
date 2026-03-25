CREATE TABLE suggestion_rules (
                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  min_people INT NOT NULL,
                                  max_people INT NOT NULL,
                                  target_space_type VARCHAR(30) NOT NULL,
                                  min_capacity INT NULL,
                                  max_capacity INT NULL,
                                  applicable_price_group VARCHAR(50) NULL,
                                  priority INT NOT NULL,
                                  is_active BOOLEAN NOT NULL DEFAULT TRUE,
                                  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_suggestion_rules_people_range ON suggestion_rules(min_people, max_people);
CREATE INDEX idx_suggestion_rules_target_space_type ON suggestion_rules(target_space_type);
CREATE INDEX idx_suggestion_rules_priority ON suggestion_rules(priority);
CREATE INDEX idx_suggestion_rules_is_active ON suggestion_rules(is_active);