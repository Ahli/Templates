package com.ahli.example.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.annotation.PostConstruct;

@ConfigurationProperties("com.ahli.example.demo.service1")
@ConstructorBinding
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
