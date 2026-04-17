package com.wealth.investor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class InvestorPlatformApplication {

	public static void main(String[] args) {

		SpringApplication.run(InvestorPlatformApplication.class, args);
	}

}
