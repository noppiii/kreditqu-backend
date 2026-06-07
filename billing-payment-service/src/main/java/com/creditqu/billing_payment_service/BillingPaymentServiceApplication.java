package com.creditqu.billing_payment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableDiscoveryClient(autoRegister = false)
public class BillingPaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingPaymentServiceApplication.class, args);
	}

}
