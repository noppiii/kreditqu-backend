package com.creditqu.credit_scoring_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableDiscoveryClient(autoRegister = false)
public class CreditScoringServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditScoringServiceApplication.class, args);
	}

}
