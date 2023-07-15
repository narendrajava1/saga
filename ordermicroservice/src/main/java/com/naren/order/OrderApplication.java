package com.naren.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class OrderApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(OrderApplication.class, args);
	}
}
