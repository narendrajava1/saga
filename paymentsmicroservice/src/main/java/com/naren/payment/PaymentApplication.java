package com.naren.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
@ComponentScan("com.naren")
public class PaymentApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(PaymentApplication.class, args);
	}
}
