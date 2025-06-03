package com.jpromi.operation_point;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OperationPointApplication {

	public static void main(String[] args) {
		SpringApplication.run(OperationPointApplication.class, args);
	}

}
