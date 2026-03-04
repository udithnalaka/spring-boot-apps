package com.ud.order.client.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@ConfigurationProperties(prefix = "app.rest-client")
@Validated
public record RestClientProperties(
        @DurationUnit(ChronoUnit.SECONDS)
        Duration connectTimeout,

        @DurationUnit(ChronoUnit.SECONDS)
        Duration responseTimeout,

        ConnectionPool connectionPool,
        Map<String, ServiceConfig> services
) {
    public record ConnectionPool(
            int maxTotal,
            int maxPerRoute,
            Duration idleEvictTimeout
    ) {}

    public record ServiceConfig(String baseUrl) {}
}
