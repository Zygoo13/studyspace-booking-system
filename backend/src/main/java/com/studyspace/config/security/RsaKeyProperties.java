package com.studyspace.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@Getter
@Setter
@ConfigurationProperties(prefix = "rsa")
public class RsaKeyProperties {

    private Resource publicKey;
    private Resource privateKey;
}