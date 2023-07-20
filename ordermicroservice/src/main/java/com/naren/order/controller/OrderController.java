package com.naren.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.naren.model.order.CustomerOrder;
import com.naren.model.order.VetaEvent;
import com.naren.order.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/createorder")
	public void createOrder(@RequestBody CustomerOrder customerOrder) {
		orderService.createOrder(customerOrder);

	}
	@GetMapping("/fetch")
	public VetaEvent<CustomerOrder> findByOrderId(@RequestParam Long orderId) {
		return orderService.getOrderById(orderId);
		
	}

}
