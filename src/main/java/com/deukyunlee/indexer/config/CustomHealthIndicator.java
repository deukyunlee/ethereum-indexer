package com.deukyunlee.indexer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 28.
 */
@Component
public class CustomHealthIndicator implements HealthIndicator {
    @Value("${spring.profiles.active}")
    private String serverMode;

    private String time;

    @Override
    public Health health() {
        time = LocalDateTime.now(ZoneId.from(ZoneOffset.UTC)).toString();
        return Health.up()
                .withDetail("service_mode", serverMode)
                .withDetail("time", time)
                .build();
    }
}