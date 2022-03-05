package com.muhammedtopgul.applicationgateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author muhammed-topgul
 * @since 05.03.2022 22:02
 */

@Configuration
public class LocalHostRouteConfig {

    @Bean
    public RouteLocator localHostRoutes(RouteLocatorBuilder locatorBuilder) {
        return locatorBuilder.routes()
                .route(r ->r.path("/api/v1/beer","/api/v1/beer/**").uri("http://localhost:8080"))
                .build();
    }
}
