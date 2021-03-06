package com.muhammedtopgul.applicationgateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author muhammed-topgul
 * @since 06.03.2022 22:47
 */

@Configuration
@Profile("local-discovery")
public class LoadBalanceRouteConfig {

    @Bean
    public RouteLocator loadBalanceRoutes(RouteLocatorBuilder locatorBuilder) {
        return locatorBuilder.routes()
                .route(r -> r.path("/api/v1/beer", "/api/v1/beer/*", "/api/v1/beer/by-upc/*")
                        .uri("lb://beer-service"))
                .route(r -> r.path("/api/v1/customer", "/api/v1/customer/**")
                        .uri("lb://order-service"))
                .route(r -> r.path("/api/v1/beer/*/inventory")
                        .filters(filter -> filter.circuitBreaker(
                                circuit -> circuit
                                        .setName("inventoryCircuitBreaker")
                                        .setFallbackUri("forward:/inventory-failover")
                                        .setRouteId("inv-failover")))
                        .uri("lb://inventory-service"))
                .route(r -> r.path("/inventory-failover/**")
                        .uri("lb://inventory-failover"))
                .build();
    }
}
