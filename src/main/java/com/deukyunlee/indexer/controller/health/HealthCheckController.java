package com.deukyunlee.indexer.controller.health;

import com.deukyunlee.indexer.model.health.response.HealthCheckResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 28.
 */
@Slf4j
@RestController
@Tag(name = "Health Check", description = "헬스 체크 API")
public class HealthCheckController {
    @Value("${spring.profiles.active}")
    private String serverMode;

    @GetMapping
    public HealthCheckResponse checkHealth() {
        return new HealthCheckResponse(serverMode, String.valueOf(Instant.now().toEpochMilli()));
    }
}
