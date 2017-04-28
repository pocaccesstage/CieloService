package com.npwit.cielo.poc.rest;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.npwit.cielo.poc.model.PaymentHistory;

@FeignClient(name = PaymentsClient.PAYMENTS_SERVIDE_ID)
public interface PaymentsClient {

	String PAYMENTS_SERVIDE_ID = "poc-cielo-api";
	String PAYMENTS_ENDPOINT = "/payments";
	String PAYMENT_BY_ID_ENDPOINT = "/payments/{id}";
	String CREATE_TOKEN = "/payments/token/{id}";
	String CREATE_SALE = "/payments/newsale/{valor}/{cartao}/{cliente}";

	@RequestMapping(method = RequestMethod.GET, value = PAYMENTS_ENDPOINT)
	List<PaymentHistory> findPayments();

	@RequestMapping(method = RequestMethod.GET, value = PAYMENT_BY_ID_ENDPOINT)
	PaymentHistory getPayment(@PathVariable("id") String paymentId);
	
	@RequestMapping(method = RequestMethod.GET, value = CREATE_TOKEN)
	String getCardToken(@PathVariable("id") String cardNumber);

	@RequestMapping(method = RequestMethod.GET, value = CREATE_SALE)
	PaymentHistory getNewSale(@PathVariable("valor") String amount, @PathVariable("cartao") String cardNumber, @PathVariable("cliente") String customerName);
}