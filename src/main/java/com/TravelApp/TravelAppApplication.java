package com.TravelApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class TravelAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelAppApplication.class, args);
	}

}
