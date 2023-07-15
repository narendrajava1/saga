package com.naren.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.naren.model.order.CustomerOrder;
import com.naren.order.service.OrderService;

@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/orders")
	public void createOrder(@RequestBody CustomerOrder customerOrder) {
		orderService.createOrder(customerOrder);

	}

}
