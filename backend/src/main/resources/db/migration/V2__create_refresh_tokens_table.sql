CREATE TABLE refresh_tokens (
                                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                user_id BIGINT NOT NULL,
                                token VARCHAR(255) NOT NULL UNIQUE,
                                expires_at DATETIME NOT NULL,
                                is_revoked BOOLEAN NOT NULL DEFAULT FALSE,
                                created_at DATETIME NOT NULL,
                                updated_at DATETIME NOT NULL,
                                CONSTRAINT fk_refresh_tokens_user
                                    FOREIGN KEY (user_id) REFERENCES users(id)
);