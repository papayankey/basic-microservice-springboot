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
                // verify customer
                .route("verify-customer", p -> p.path("/verify/{id}")
                        .filters(f -> f.setPath("/customers/verify/{id}"))
                        .uri("lb://customer-service"))
                .route("verify-customer", p -> p.path("/customers/verify/{id}")
                        .uri("lb://customer-service"))

                // get a customer
                .route("get-customer", p -> p.path("/info/{id}")
                        .filters(f -> f.setPath("/customers/{id}"))
                        .uri("lb://customer-service"))
                .route("get-customer", p -> p.path("/customers/{id}")
                        .uri("lb://customer-service"))

                // get customers
                .route("get-customers", p -> p.path("/info")
                        .filters(f -> f.setPath("/customers"))
                        .uri("lb://customer-service"))
                .route("get-customers", p -> p.path("/customers")
                        .uri("lb://customer-service"))

                // add customer
                .route("add-customer", p -> p.path("/info").and().method("POST")
                        .filters(f -> f.setPath("/customers"))
                        .uri("lb://customer-service"))
                .route("add-customer", p -> p.path("/customers").and().method("POST")
                        .uri("lb://customer-service"))
                .build();
    }
}
