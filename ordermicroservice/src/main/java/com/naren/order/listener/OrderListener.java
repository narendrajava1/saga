package com.naren.order.listener;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naren.model.order.CustomerOrder;
import com.naren.model.order.VetaEvent;
import com.naren.order.repo.OrderEntity;
import com.naren.order.repo.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderListener {

	@Autowired
	private OrderRepository repository;

	@KafkaListener(topics = "reversed-orders", groupId = "orders-group", id = "orders")
	public void reverseOrder(String event) {

		try {
			VetaEvent<CustomerOrder> orderEvent = new ObjectMapper().readValue(event, VetaEvent.class);

			Optional<OrderEntity> order = this.repository.findById(orderEvent.getEvent().getOrderId());

			order.ifPresent(o -> {
				o.setStatus("FAILED");
				this.repository.save(o);
			});
		} catch (Exception e) {
			log.error("error occurred in while doing transaction back {}", e);
		}

	}
}