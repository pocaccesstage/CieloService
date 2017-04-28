package com.npwit.cielo.poc.rest;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.npwit.cielo.poc.model.PaymentHistory;

@RestController
@RequestMapping(value = "/aggregation", produces = "application/json")
public class AggregationResource {

	// Uses Feign
	private PaymentsClient paymentsClient;

	// Uses Ribbon to load balance requests
	private LoadBalancerClient loadBalancer;
	private RestTemplate restTemplate;

	// Uses Ribbon to load balance requests
	private RestTemplate loadBalancedRestTemplate;

	@Autowired
	public void setLoadBalancer(LoadBalancerClient loadBalancer) {
		this.loadBalancer = loadBalancer;
	}

	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Autowired
	public void setLoadBalancedRestTemplate(RestTemplate loadBalancedRestTemplate) {
		this.loadBalancedRestTemplate = loadBalancedRestTemplate;
	}

	@Autowired
	public void setPaymentsClient(PaymentsClient paymentsClient) {
		this.paymentsClient = paymentsClient;
	}

	@RequestMapping(value = "/payments", method = RequestMethod.GET)
	public List<PaymentHistory> findPayments() {
		return this.paymentsClient.findPayments();
	}

	@RequestMapping(value = "/payments1/{id}", method = RequestMethod.GET)
	public PaymentHistory findPayment1(@PathVariable(value = "id") String id) {
		ServiceInstance instance = loadBalancer.choose(PaymentsClient.PAYMENTS_SERVIDE_ID);
		URI uri = instance.getUri();
		String url = String.format("%s%s/{id}", uri, PaymentsClient.PAYMENTS_ENDPOINT);
		return this.restTemplate.getForObject(url, PaymentHistory.class, id);
	}

	@RequestMapping(value = "/payments2/{id}", method = RequestMethod.GET)
	public PaymentHistory findPayment2(@PathVariable(value = "id") String id) {
		String url = String.format("http://%s%s/{id}", PaymentsClient.PAYMENTS_SERVIDE_ID, PaymentsClient.PAYMENTS_ENDPOINT);
		return this.loadBalancedRestTemplate.getForObject(url, PaymentHistory.class, id);
	}

	@RequestMapping(value = "/payments1/token/{id}", method = RequestMethod.GET)
	public String getCardToken1(@PathVariable("id") String cardNumber) {
		ServiceInstance instance = loadBalancer.choose(PaymentsClient.PAYMENTS_SERVIDE_ID);
		URI uri = instance.getUri();
		String url = String.format("%s%s/token/{id}", uri, PaymentsClient.PAYMENTS_ENDPOINT);
		return this.restTemplate.getForObject(url, String.class, cardNumber);
	}

	@RequestMapping(value = "/payments2/token/{id}", method = RequestMethod.GET)
	public String getCardToken2(@PathVariable("id") String cardNumber) {
		String url = String.format("http://%s%s/token/{id}", PaymentsClient.PAYMENTS_SERVIDE_ID, PaymentsClient.PAYMENTS_ENDPOINT);
		return this.loadBalancedRestTemplate.getForObject(url, String.class, cardNumber);
	}
	
	@RequestMapping(value = "/payments1/newsale/{valor}/{cartao}/{cliente}", method = RequestMethod.GET)
	public PaymentHistory getNewSale1(@PathVariable("valor") String amount, @PathVariable("cartao") String cardNumber, @PathVariable("cliente") String customerName) {
		ServiceInstance instance = loadBalancer.choose(PaymentsClient.PAYMENTS_SERVIDE_ID);
		URI uri = instance.getUri();
		String url = String.format("%s%s/newsale/{valor}/{cartao}/{cliente}", uri, PaymentsClient.PAYMENTS_ENDPOINT);
		return this.restTemplate.getForObject(url, PaymentHistory.class, amount, cardNumber, customerName);
	}

	@RequestMapping(value = "/payments2/newsale/{valor}/{cartao}/{cliente}", method = RequestMethod.GET)
	public PaymentHistory getNewSale2(@PathVariable("valor") String amount, @PathVariable("cartao") String cardNumber, @PathVariable("cliente") String customerName) {
		String url = String.format("http://%s%s/newsale/{valor}/{cartao}/{cliente}", PaymentsClient.PAYMENTS_SERVIDE_ID, PaymentsClient.PAYMENTS_ENDPOINT);
		return this.loadBalancedRestTemplate.getForObject(url, PaymentHistory.class, amount, cardNumber, customerName);
	}

}