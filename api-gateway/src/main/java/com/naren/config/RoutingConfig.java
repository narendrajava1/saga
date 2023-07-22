package com.naren.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(0)
public class RoutingConfig {
	@Bean
	public RouteLocator apiRoutes(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(prdocate->
		prdocate.path("/api/customer/**")
		.uri("lb://ACCOUNT-SERVICE"))
				.route(prdocate->
				prdocate.path("/orders/**")
				.uri("lb://ORDER-SERVICE"))				
				.build();
				
	}
}
