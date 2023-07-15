package com.naren.order.service;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.naren.model.order.CustomerOrder;
import com.naren.model.order.VetaEvent;
import com.naren.order.repo.OrderEntity;
import com.naren.order.repo.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private KafkaTemplate<String, VetaEvent<CustomerOrder>> kafkaTemplate;

	public void createOrder(CustomerOrder customerOrder) {
		OrderEntity order = new OrderEntity();
		try {
			// save order in database
			// save order in database
			order.setAmount(customerOrder.getAmount());
			order.setItem(customerOrder.getItem());
			order.setQuantity(customerOrder.getQuantity());
			order.setStatus("CREATED");
			order = orderRepository.save(order);
			customerOrder.setOrderId(order.getId());

			// publish order created event for payment microservice to consume.
			VetaEvent<CustomerOrder> event = new VetaEvent<>();
			event.setEvent(customerOrder);
			event.setType("ORDER_CREATED");
			CompletableFuture<SendResult<String, VetaEvent<CustomerOrder>>> completableFuture = kafkaTemplate
					.send("new-orders", event);
			completableFuture.whenComplete(new BiConsumer<SendResult<String, VetaEvent<CustomerOrder>>, Throwable>() {

				@Override
				public void accept(SendResult<String, VetaEvent<CustomerOrder>> result, Throwable u) {
					if (u != null) {
						log.error("error while send ing to the topic: {}", u);
					} else {
						ProducerRecord<String, VetaEvent<CustomerOrder>> record = result.getProducerRecord();
						VetaEvent<CustomerOrder> customerRecord = record.value();
						log.info(
								"Producing request succeeded data is: {} and partition is: {}, key is: {},topic is: {} at timestamp is: {}",
								customerRecord, record.partition(), record.key(), record.topic(), record.timestamp());
					}

				}

			});
		} catch (Exception e) {
			order.setStatus("FAILED");
			orderRepository.save(order);
		}
	}

}
