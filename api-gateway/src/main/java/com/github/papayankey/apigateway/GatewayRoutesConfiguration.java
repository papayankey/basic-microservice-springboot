package com.github.papayankey.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfiguration {
    @Bean
    RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("route-one", p -> p.path("/verify/{id}")
                        .filters(f -> f.setPath("/customers/verify/{id}"))
                        .uri("lb://customer-service"))
                .route("route-two", p -> p.path("/customers/verify/{id}")
                        .uri("lb://customer-service"))
                .build();
    }
}
