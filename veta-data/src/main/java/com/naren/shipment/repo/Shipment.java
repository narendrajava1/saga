package com.naren.shipment.repo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "shipment")
@Data
public class Shipment {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String address;

	@Column
	private String status;

	@Column
	private long orderId;
}
