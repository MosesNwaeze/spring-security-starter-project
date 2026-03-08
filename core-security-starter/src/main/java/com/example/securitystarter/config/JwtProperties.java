
package com.example.securitystarter.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
    private String secret;
    private long expiration;

}
