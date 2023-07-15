package com.naren.inventory.repo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "inventory")
@Data
public class Inventory {

	@Id
	@GeneratedValue
	private long id;

	@Column
	private int quantity;

	@Column
	private String item;
}