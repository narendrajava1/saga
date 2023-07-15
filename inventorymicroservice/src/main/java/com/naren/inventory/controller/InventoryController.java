package com.naren.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.naren.inventory.repo.Inventory;
import com.naren.inventory.repo.InventoryRepository;
import com.naren.model.inventory.Stock;

@RestController
public class InventoryController {

	@Autowired
	private InventoryRepository inventoryRepository;

	@PostMapping("/inventory")
	public void addInventory(@RequestBody Stock stock) {
		Iterable<Inventory> items = this.inventoryRepository.findByItem(stock.getItem());
		if (items.iterator().hasNext()) {
			items.forEach(item -> {

				item.setQuantity(stock.getQuantity() + item.getQuantity());
				this.inventoryRepository.save(item);
			});
		} else {
			Inventory inventory = new Inventory();
			inventory.setItem(stock.getItem());
			inventory.setQuantity(stock.getQuantity());
			this.inventoryRepository.save(inventory);
		}

	}
}