package com.muhammedtopgul.applicationgateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author muhammed-topgul
 * @since 05.03.2022 22:02
 */

@Configuration
@Profile("!local-discovery")
public class LocalHostRouteConfig {

    @Bean
    public RouteLocator localHostRoutes(RouteLocatorBuilder locatorBuilder) {
        return locatorBuilder.routes()
                .route(r -> r.path("/api/v1/beer", "/api/v1/beer/*", "/api/v1/beer/by-upc/*")
                            .uri("http://localhost:8080"))
                .route(r -> r.path("/api/v1/customer", "/api/v1/customer/**")
                            .uri("http://localhost:8085"))
                .route(r -> r.path("/api/v1/beer/*/inventory")
                            .uri("http://localhost:8090"))
                .build();
    }
}
