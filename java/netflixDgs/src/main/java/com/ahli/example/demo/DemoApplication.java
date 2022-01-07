package com.ahli.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	
	// Test the application using: http://localhost:8080/graphiql
	
	public static void main(final String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	// TODO field fetchers https://netflix.github.io/dgs/advanced/context-passing/
	// TODO dataloader https://netflix.github.io/dgs/data-loaders/
	// TODO error handler https://netflix.github.io/dgs/error-handling/
	// TODO separate DTO and model
	// TODO add a new scalar
	// TODO unit tests https://netflix.github.io/dgs/query-execution-testing/
	// TODO extend federated type, add entityFetcher https://netflix.github.io/dgs/federation/
	// TODO add some parameter verification
}
