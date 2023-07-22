package com.naren.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Order(0)
@Component
public class RequestTraceFilter implements GlobalFilter {

	private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);
	
//	@Autowired
//	FilterUtility filterUtility;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
//		if (isCorrelationIdPresent(requestHeaders)) {
			logger.debug("reqest headers : {}. ", requestHeaders);
//					filterUtility.getCorrelationId(requestHeaders));
//		} else {
//			String correlationID = generateCorrelationId();
//			exchange = filterUtility.setCorrelationId(exchange, correlationID);
//			logger.debug("EazyBank-correlation-id generated in tracing filter: {}.", correlationID);
//		}
		return chain.filter(exchange);
	}
}