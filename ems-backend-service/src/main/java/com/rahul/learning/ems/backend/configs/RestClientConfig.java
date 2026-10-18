package com.rahul.learning.ems.backend.configs;

import java.net.http.HttpClient;
import java.time.Duration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    /*
     * Normal RestClient.Builder.
     *
     * This is intentionally NOT load-balanced.
     * Eureka uses this builder for communication with
     * the Eureka Server.
     */
    @Bean
    @Primary
    public RestClient.Builder restClientBuilder() {

        return RestClient.builder();
    }

    /*
     * Load-balanced RestClient.Builder.
     *
     * This builder is specifically for application-to-service
     * communication using Eureka service names.
     */
    @Bean(name = "notificationRestClientBuilder")
    @LoadBalanced
    public RestClient.Builder notificationRestClientBuilder() {

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();

        JdkClientHttpRequestFactory requestFactory =
                new JdkClientHttpRequestFactory(httpClient);

        requestFactory.setReadTimeout(Duration.ofSeconds(5));

        return RestClient.builder()
                .requestFactory(requestFactory);
    }
}