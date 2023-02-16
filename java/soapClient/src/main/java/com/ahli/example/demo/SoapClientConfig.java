package com.ahli.example.demo;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("com.ahli.example.demo.service1")
@AllArgsConstructor
@Getter
@Slf4j
public class SoapClientConfig {
	
	private final String url;
	
	@PostConstruct
	private void init() {
		log.info("Using SoapClientConfig: url={}", url);
	}
	
}
