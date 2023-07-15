package com.naren.payment.repo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "payment")
public class Payment {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String mode;

	@Column
	private Long orderId;

	@Column
	private double amount;

	@Column
	private String status;
}