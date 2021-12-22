package com.ahli.example.demo;

import com.ahli.example.demo.soap.gen.service1.GetValue;
import com.ahli.example.demo.soap.gen.service1.GetValueResponse;
import com.ahli.example.demo.soap.gen.service1.ObjectFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

@Slf4j
public class SoapServiceClient extends WebServiceGatewaySupport {
	
	private final ObjectFactory objectFactory;
	
	public SoapServiceClient(final ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}
	
	/**
	 * @return
	 * @throws org.springframework.ws.client.WebServiceClientException
	 * 		SOAP call had exception
	 */
	public String fetchValue(final String param1) {
		final GetValue request = objectFactory.createGetValue();
		request.setParam1(param1);
		
		final Object response = getWebServiceTemplate().marshalSendAndReceive(request);
		if (response instanceof GetValueResponse getValueResponse) {
			return getValueResponse.getReturn();
		} else {
			log.error("Unhandled response object {}: {}", response.getClass(), response);
			throw new RuntimeException("Unhandled response object");
		}
	}
	
}
