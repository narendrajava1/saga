package com.naren.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingConfig {
	@Bean
	public RouteLocator apiRoutes(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(prdocate->
		prdocate.path("/api/customer/**")
		.uri("lb://ACCOUNT-SERVICE")).build();
	}
}
