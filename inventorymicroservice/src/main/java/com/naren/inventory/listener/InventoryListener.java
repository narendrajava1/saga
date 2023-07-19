package com.naren.inventory.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naren.inventory.repo.Inventory;
import com.naren.inventory.repo.InventoryRepository;
import com.naren.model.order.CustomerOrder;
import com.naren.model.order.VetaEvent;

@Component
public class InventoryListener {

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private KafkaTemplate<String, VetaEvent<CustomerOrder>> kafkaTemplate;

	@KafkaListener(topics = "new-payments", groupId = "payments-group")
	public void updateInventory(String paymentEvent) throws Exception {

		VetaEvent<CustomerOrder> vetaEvent = new ObjectMapper().readValue(paymentEvent, VetaEvent.class);
		CustomerOrder customerOrder = vetaEvent.getEvent();
		try {
			// update stock in database
			Iterable<Inventory> inventories = this.inventoryRepository.findByItem(customerOrder.getItem());
			boolean exists = inventories.iterator().hasNext();
			if (!exists)
				throw new Exception("Stock not available");

			inventories.forEach(i -> {
				i.setQuantity(i.getQuantity() - customerOrder.getQuantity());

				this.inventoryRepository.save(i);
			});
			vetaEvent.setOrderType("INVENTORY_UPDATED");
			vetaEvent.setEvent(customerOrder);
			this.kafkaTemplate.send("new-inventory", vetaEvent);
		} catch (Exception e) {
			// reverse previous task
			VetaEvent<CustomerOrder> pe = new VetaEvent<>();
			pe.setEvent(customerOrder);
			pe.setOrderType("PAYMENT_REVERSED");
			this.kafkaTemplate.send("reversed-payments", pe);
		}
	}

	@KafkaListener(topics = "reversed-inventory", groupId = "inventory-group")
	public void reverseInventory(String event) {
		try {
			VetaEvent<CustomerOrder> vetaEvent = new ObjectMapper().readValue(event, VetaEvent.class);
			Iterable<Inventory> inv = this.inventoryRepository.findByItem(vetaEvent.getEvent().getItem());
			inv.forEach(i -> {

				i.setQuantity(i.getQuantity() + vetaEvent.getEvent().getQuantity());

				this.inventoryRepository.save(i);
			});

			// reverse previous task
			// paymentEvent
			vetaEvent.setEvent(vetaEvent.getEvent());
			vetaEvent.setOrderType("PAYMENT_REVERSED");
			this.kafkaTemplate.send("reversed-payments", vetaEvent);
		} catch (Exception e) {

		}
	}

}
