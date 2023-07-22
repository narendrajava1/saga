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

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/createorder")
	public void createOrder(@RequestBody CustomerOrder customerOrder) {
		orderService.createOrder(customerOrder);

	}
	@GetMapping("/fetch")
	public VetaEvent<CustomerOrder> findByOrderId(@RequestParam Long orderId) {
		log.debug("fetching the order for this orderId {}",orderId);
		return orderService.getOrderById(orderId);
		
	}

}
