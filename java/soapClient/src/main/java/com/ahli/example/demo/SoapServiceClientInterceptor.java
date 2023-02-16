package com.ahli.example.demo;

import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPBodyElement;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPPart;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Interceptor to insert CDATA tags
 */
public class SoapServiceClientInterceptor implements ClientInterceptor {
	
	@Override
	public boolean handleRequest(final MessageContext messageContext) throws WebServiceClientException {
		try {
			final WebServiceMessage request = messageContext.getRequest();
			final SaajSoapMessage saajSoapMessage = (SaajSoapMessage) request;
			final SOAPPart soapPart = saajSoapMessage.getSaajMessage().getSOAPPart();
			final SOAPBody body = soapPart.getEnvelope().getBody();
			
			final XPath xPath = XPathFactory.newInstance().newXPath();
			final NodeList nodes = (NodeList) xPath.evaluate("//param1", body, XPathConstants.NODESET);
			
			for (int i = nodes.getLength() - 1; i >= 0; --i) {
				final Node node = nodes.item(i);
				if (node instanceof SOAPBodyElement) {
					final Node firstChild = node.getFirstChild();
					// if unit tests fail to match the text when there are carriage returns => remove them
					final String nodeValue = firstChild.getNodeValue().replace("\r", "");
					final CDATASection cdataSection = soapPart.createCDATASection(nodeValue);
					node.replaceChild(cdataSection, firstChild);
				}
			}
		} catch (final SOAPException | XPathExpressionException e) {
			throw new RuntimeException("Failed to insert CDATA.", e);
		}
		return true;
	}
	
	@Override
	public boolean handleResponse(final MessageContext messageContext) throws WebServiceClientException {
		return true;
	}
	
	@Override
	public boolean handleFault(final MessageContext messageContext) throws WebServiceClientException {
		return true;
	}
	
	@Override
	public void afterCompletion(final MessageContext messageContext, final Exception e)
			throws WebServiceClientException {
		// do nothing
	}
}
