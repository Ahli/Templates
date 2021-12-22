package com.ahli.example.demo;

import com.ahli.example.demo.soap.gen.service1.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

@Configuration
public class SoapConfig {
	
	@Bean
	protected ObjectFactory objectFactory() {
		return new ObjectFactory();
	}
	
	@Bean
	protected SoapServiceClient soapServiceClient(final SoapClientConfig config, final ObjectFactory objectFactory) {
		final SoapServiceClient client = new SoapServiceClient(objectFactory);
		client.setDefaultUri(config.getUrl());
		final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan(objectFactory.getClass().getPackageName());
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		client.setInterceptors(new ClientInterceptor[] { new SoapServiceClientInterceptor() });
		return client;
	}
	
}
