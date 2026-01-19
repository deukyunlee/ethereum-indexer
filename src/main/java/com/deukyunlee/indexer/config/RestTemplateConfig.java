package com.deukyunlee.indexer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;
import java.time.Duration;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 22.
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
