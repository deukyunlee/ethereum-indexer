package com.deukyunlee.indexer.config.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 27.
 */
@EnableCaching
@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    public CacheManager cacheManager() {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        RedisCacheConfiguration defaultConfiguration = defaultConfiguration();
        initCacheConfig(defaultConfiguration, cacheConfigurations);
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(defaultConfiguration)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    private RedisCacheConfiguration defaultConfiguration() {
        PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(Object.class)
                .build();
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.EVERYTHING)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeKeysWith(fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));
    }

    private void initCacheConfig(RedisCacheConfiguration configuration, Map<String, RedisCacheConfiguration> cacheConfigurations) {
        for (RedisKeyInfo key : RedisKeyInfo.values()) {
            cacheConfigurations.put(key.getKeyPrefix(), configuration.entryTtl(key.getTtl()));
        }
    }
}

