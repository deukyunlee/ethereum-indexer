package com.deukyunlee.indexer.model.health.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 28.
 */
@Getter
@AllArgsConstructor
public class HealthCheckResponse {

    private String runningMode;
    private String time;
}

