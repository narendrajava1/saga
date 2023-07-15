package com.naren.inventory.repo;

import org.springframework.data.repository.CrudRepository;

public interface InventoryRepository extends CrudRepository<Inventory, Long> {

	Iterable<Inventory> findByItem(String item);
}