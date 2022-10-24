package com.ahli.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class CrudTest {
	
	@Container
	private static final MongoDBContainer mongo = new MongoDBContainer(DockerImageName.parse("mongo:6.0.2"));
	
	@Autowired
	private MeasurementRepository measurementRepository;
	
	@DynamicPropertySource
	static void modifyContext(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
	}
	
	@Test
	void testContainerWorks() {
		assertThat(mongo.isRunning()).isTrue();
	}
	
	@Test
	void testSave() {
		assertThat(measurementRepository.findAll()).isEmpty();
		
		MetaData metaData = new MetaData("device_1", "hoursOnline");
		Instant timestamp = Instant.parse("2022-11-03T18:00:00.00Z");
		List<Measurement> measurements = new ArrayList<>(1000);
		float value = 1000f;
		
		for (int i = 1000; i > 0; i--) {
			measurements.add(new Measurement(timestamp, metaData, value));
			timestamp = timestamp.minus(1, ChronoUnit.HOURS);
			value--;
		}
		
		measurementRepository.saveAll(measurements);
		
		assertThat(measurementRepository.findAll()).hasSize(1000);
	}
	
}
