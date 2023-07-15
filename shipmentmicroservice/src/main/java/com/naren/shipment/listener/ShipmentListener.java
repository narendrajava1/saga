package com.naren.shipment.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naren.model.order.CustomerOrder;
import com.naren.model.order.VetaEvent;
import com.naren.shipment.repo.Shipment;
import com.naren.shipment.repo.ShipmentRepository;

@Component
public class ShipmentListener {
	@Autowired
	private KafkaTemplate<String, VetaEvent<CustomerOrder>> kafkaTemplate;

	@Autowired
	private ShipmentRepository shipmentRepository;

	@KafkaListener(topics = "new-inventory", groupId = "inventory-group")
	public void shipOrder(String event) throws JsonMappingException, JsonProcessingException {
		Shipment shipment = new Shipment();
		VetaEvent<CustomerOrder> inventoryEvent = new ObjectMapper().readValue(event, VetaEvent.class);
		CustomerOrder order = inventoryEvent.getEvent();
		try {
			if (order.getAddress() == null) {
				throw new Exception("Address not present");
			}
			shipment.setAddress(order.getAddress());
			shipment.setOrderId(order.getOrderId());

			shipment.setStatus("success");

			this.shipmentRepository.save(shipment);
			// do other shipment logic ..

		} catch (Exception e) {
			shipment.setOrderId(order.getOrderId());
			shipment.setStatus("failed");
			this.shipmentRepository.save(shipment);

			// InventoryEvent
			VetaEvent<CustomerOrder> reverseEvent = new VetaEvent<CustomerOrder>();

			reverseEvent.setType("INVENTORY_REVERSED");
			reverseEvent.setEvent(order);
			this.kafkaTemplate.send("reversed-inventory", reverseEvent);
		}
	}
}
