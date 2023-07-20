package com.naren.account.repo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "account")
@Data
public class Account {
	@Id
	@GeneratedValue
	private Long id;
	private String number;
	private String customerId;
	private int amount;
}