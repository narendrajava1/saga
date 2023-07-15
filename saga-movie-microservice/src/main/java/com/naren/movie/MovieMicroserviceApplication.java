package com.naren.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("com.naren")
@ComponentScan("com.naren")
public class MovieMicroserviceApplication {
	public static void main(String[] args) {
		SpringApplication.run(MovieMicroserviceApplication.class, args);
	}
}
