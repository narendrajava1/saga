package com.naren.account.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.naren.account.repo.Account;
import com.naren.account.repo.AccountRepository;
import com.naren.model.order.CustomerOrder;
import com.naren.model.order.VetaEvent;

@RestController
@RequestMapping("/api/customer")
public class AccountController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private AccountRepository repository;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/{customer}")
	public List<Account> findByCustomer(@PathVariable("customer") String customerId) {
		LOGGER.info("findByCustomer: customerId={}", customerId);
		return repository.findByCustomerId(customerId);
	}

	@PostMapping("/createcustomer")
	public Account create(@RequestBody Account account) {
		LOGGER.info("create: {}", account);
		return repository.save(account);
	}

	@GetMapping("/fetchorder")
	public ResponseEntity<VetaEvent<CustomerOrder>> findOrderByOrderId(@RequestParam Long orderId) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<?> httpEntity = new HttpEntity<>(headers);
		String uri = "http://order-service/orders/fetch";
		UriComponents builder = UriComponentsBuilder.fromHttpUrl(uri).queryParam("orderId", orderId).build();
		ResponseEntity<VetaEvent<CustomerOrder>> responseEntity = restTemplate.exchange(builder.toString(),
				HttpMethod.GET, httpEntity, new ParameterizedTypeReference<VetaEvent<CustomerOrder>>() {
				});
		System.out.println(responseEntity);
		return responseEntity;
	}
}
