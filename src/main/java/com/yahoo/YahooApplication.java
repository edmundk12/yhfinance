package com.yahoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YahooApplication {

	public static void main(String[] args) {
		SpringApplication.run(YahooApplication.class, args);
	}

}
