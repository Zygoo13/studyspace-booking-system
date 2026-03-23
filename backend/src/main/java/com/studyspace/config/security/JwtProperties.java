package com.studyspace.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.security")
public class JwtProperties {
    private String issuer;
    private long accessTokenExpirationMinutes;
    private long refreshTokenExpirationDays;
}

