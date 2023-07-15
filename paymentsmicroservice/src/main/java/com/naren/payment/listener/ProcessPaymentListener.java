package com.naren.payment.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naren.model.order.CustomerOrder;
import com.naren.model.order.VetaEvent;
import com.naren.payment.repo.Payment;
import com.naren.payment.repo.PaymentRepository;

@Component
public class ProcessPaymentListener {

	@Autowired
	private PaymentRepository repository;

	@Autowired
	private KafkaTemplate<String, VetaEvent<CustomerOrder>> kafkaTemplate;

	@KafkaListener(topics = "new-orders", groupId = "orders-group", id = "orders")
	public void processPayment(VetaEvent<CustomerOrder> record) throws JsonMappingException, JsonProcessingException {
		VetaEvent<CustomerOrder> orderEvent = new ObjectMapper().readValue(record.toString(), VetaEvent.class);
		CustomerOrder customerOrder = orderEvent.getEvent();
		Payment payment = new Payment();
		try {

			// save payment details in db
			payment.setAmount(customerOrder.getAmount());
			payment.setMode(customerOrder.getPaymentMode());
			payment.setOrderId(customerOrder.getOrderId());
			payment.setStatus("SUCCESS");
			repository.save(payment);

			// publish payment created event for inventory microservice to consume.

			VetaEvent<CustomerOrder> paymentEvent = new VetaEvent<>();
			paymentEvent.setEvent(customerOrder);
			paymentEvent.setType("PAYMENT_CREATED");
			this.kafkaTemplate.send("new-payments", paymentEvent);
		} catch (Exception e) {
			payment.setOrderId(customerOrder.getOrderId());
			payment.setStatus("FAILED");
			repository.save(payment);

			// reverse previous task
			VetaEvent<CustomerOrder> oe = new VetaEvent<>();
			oe.setEvent(customerOrder);
			oe.setType("ORDER_REVERSED");
			this.kafkaTemplate.send("reversed-orders", orderEvent);
		}
	}

	@KafkaListener(topics = "reversed-payments", groupId = "payments-group", containerFactory = "paymentsKafkaListenerContainerFactory")
	public void reversePayment(String event) {
		try {
			VetaEvent<CustomerOrder> paymentEvent = new ObjectMapper().readValue(event, VetaEvent.class);
			CustomerOrder customerOrder = paymentEvent.getEvent();
			// do refund..

			// update status as failed
			Iterable<Payment> payments = this.repository.findByOrderId(customerOrder.getOrderId());

			payments.forEach(p -> {

				p.setStatus("FAILED");
				this.repository.save(p);
			});

			// reverse previous task
			VetaEvent<CustomerOrder> orderEvent = new VetaEvent<CustomerOrder>();
			orderEvent.setEvent(paymentEvent.getEvent());
			orderEvent.setType("ORDER_REVERSED");
			this.kafkaTemplate.send("reversed-orders", orderEvent);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
