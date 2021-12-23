package com.ahli.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	
	// Test the application using: http://localhost:8080/graphiql
	
	public static void main(final String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
}
