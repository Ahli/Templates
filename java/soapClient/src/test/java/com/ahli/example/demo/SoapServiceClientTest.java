package com.ahli.example.demo;

import com.ahli.example.demo.soap.gen.service1.ObjectFactory;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.ws.test.client.RequestMatchers;
import org.springframework.ws.test.client.ResponseCreators;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = { SoapServiceClient.class })
@Import(SoapServiceClientTest.Context.class)
class SoapServiceClientTest {
	
	@Autowired
	SoapServiceClient client;
	
	@Autowired
	ObjectFactory objectFactory;
	
	MockWebServiceServer mockServer;
	
	@BeforeEach
	void init() {
		client.setDefaultUri("http:l//localhost:8080");
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan(objectFactory.getClass().getPackageName());
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		client.setInterceptors(new ClientInterceptor[] { new SoapServiceClientInterceptor() });
		
		mockServer = MockWebServiceServer.createServer(client);
		XMLUnit.setIgnoreWhitespace(true);
		// TODO whitespace is not ignored in the validRequest.xml... bug in Spring-WS in 4.0.1 & 3.1.5
		//    => removed whitespace in validRequest.xml
	}
	
	@Test
	void testShouldSendValidRequest() throws IOException {
		mockServer.expect(RequestMatchers.payload(new ClassPathResource("service1/validRequest.xml")))
				.andRespond(ResponseCreators.withPayload(new ClassPathResource("service1/successResponse.xml")));
		
		String response = client.fetchValue(">>>123<<<");
		assertThat(response).isEqualTo("ABC");
		
		mockServer.verify();
	}
	
	
	@TestConfiguration
	static class Context {
		@Bean
		ObjectFactory objectFactory() {
			return new ObjectFactory();
		}
	}
	
}