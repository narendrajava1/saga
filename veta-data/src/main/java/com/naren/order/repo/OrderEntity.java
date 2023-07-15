package com.naren.order.repo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="orders")
@Data
public class OrderEntity {


    @Id
    @GeneratedValue
    private long id;

    @Column
    private String item;

    @Column
    private int quantity;

    @Column
    private double amount;

    @Column
    private String status;
}