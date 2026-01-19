package com.deukyunlee.indexer.config.redis;

import java.time.Duration;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 27.
 */
public enum RedisKeyInfo {
    ACCOUNT_TOKEN_DATA(RedisPrefix.ACCOUNT_TOKEN_DATA, Duration.ofHours(1L));

    private final String keyPrefix;
    private final Duration ttl;

    RedisKeyInfo(String keyPrefix, Duration ttl) {
        this.keyPrefix = keyPrefix;
        this.ttl = ttl;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public Duration getTtl() {
        return ttl;
    }
}
