package com.jpay.core_banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class JpayCoreBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpayCoreBankingApplication.class, args);
	}

}
