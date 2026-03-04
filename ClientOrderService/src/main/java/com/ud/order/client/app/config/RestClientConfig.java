package com.ud.order.client.app.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(RestClientProperties.class)
public class RestClientConfig {

    private final RestClientProperties properties;

    public RestClientConfig(RestClientProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RestClient.Builder restClientBuilder() {

        var connectionManager =
                PoolingHttpClientConnectionManagerBuilder.create()
                        .setMaxConnTotal(properties.connectionPool().maxTotal())
                        .setMaxConnPerRoute(properties.connectionPool().maxPerRoute())
                        .build();

        var requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.of(properties.connectTimeout()))
                .setResponseTimeout(Timeout.of((properties.responseTimeout())))
                .build();

        var httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .evictExpiredConnections()
                .evictIdleConnections(Timeout.of(properties.connectionPool().idleEvictTimeout()))
                .build();

        var factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return RestClient.builder()
                .requestFactory(factory)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

    }

    @Bean
    public RestClient orderServiceClient(RestClient.Builder restClientBuilder) {
        return restClientBuilder
                .clone()
                .baseUrl(properties.services().get("order-service").baseUrl())
                .build();
    }


}
