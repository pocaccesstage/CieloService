package com.npwit.poc.cielo.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.npwit.poc.cielo.model.PaymentHistory;

import cieloecommerce.sdk.Merchant;
import cieloecommerce.sdk.ecommerce.CardToken;
import cieloecommerce.sdk.ecommerce.CieloEcommerce;
import cieloecommerce.sdk.ecommerce.CreditCard;
import cieloecommerce.sdk.ecommerce.Customer;
import cieloecommerce.sdk.ecommerce.Environment;
import cieloecommerce.sdk.ecommerce.Payment;
import cieloecommerce.sdk.ecommerce.Sale;
import cieloecommerce.sdk.ecommerce.request.CieloError;
import cieloecommerce.sdk.ecommerce.request.CieloRequestException;

@Component
@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
public class PaymentResource {

	private static Logger log = LoggerFactory.getLogger(PaymentResource.class);
	private String merchantId = "45f6ef97-24f8-481f-b48b-0f2a2e135989";
	private String merchantKey = "USFNGLJJNGZYWUNXQRJJVVYXHGTSMIBOGJGDNAQG";
	
	@GET
	public List<PaymentHistory> findPayments() {
		List<PaymentHistory> result = new ArrayList<>();
		result.add(this.buildPayment("1", "135.01"));
		result.add(this.buildPayment("2", "358.02"));
		return result;
	}

	@GET
	@Path("{id}")
	public PaymentHistory getPayment(@PathParam("id") String paymentId) {
		log.info("Cielo->createCardToken: merchantId="+merchantId+", merchantKey="+merchantKey+", paymentID="+paymentId);

		// TODO validar o formato do do ID, antes de chamar a API da Cielo
		PaymentHistory sale = this.findSaleByPayment(merchantId, merchantKey, paymentId);
		if(sale != null) {
			log.info("Cielo->createCardToken: sale request="+sale.getRequest()+", amount="+sale.getAmount()+", status="+sale.getStatus());
		}
		
		return sale;
	}

	@GET
	@Path("/token/{id}")
	public String getCardToken(@PathParam("id") String cardNumber) {
		log.info("Cielo->createCardToken: merchantId="+merchantId+", merchantKey="+merchantKey);

		final CreditCard card = new CreditCard("123", "Visa");
		card.setExpirationDate("12/2018");
		card.setCardNumber(cardNumber); // "0000000000000001" -> "ReturnCode":"4","ReturnMessage":"Operation Successful","Status":1
		card.setHolder("Fulano de Tal");
		
		CardToken cardToken = createCardToken(merchantId, merchantKey, card);
		log.info("Cielo->createCardToken: token="+cardToken);
		
		return cardToken.getCardToken();
	}
	
	@GET
	@Path("/newsale/{valor}/{cartao}/{cliente}")
	public PaymentHistory getNewSale(@PathParam("valor") String amount, @PathParam("cartao") String cardNumber, @PathParam("cliente") String customerName) {
		log.info("Cielo->createCardToken: merchantId="+merchantId+", merchantKey="+merchantKey);

		final CreditCard card = new CreditCard("123", "Visa");
		card.setExpirationDate("12/2018");
		card.setCardNumber(cardNumber); // "0000000000000001" -> "ReturnCode":"4","ReturnMessage":"Operation Successful","Status":1
		card.setHolder("Fulano de Tal");
		
		PaymentHistory newSale = this.createSale(merchantId, merchantKey, amount, cardNumber, customerName);
		if(newSale != null) {
			log.info("Cielo->createCardToken: sale paymentID="+newSale.getPayment()+", amount="+newSale.getAmount()+", status="+newSale.getStatus());
		}
		
		return newSale;
	}
	
	private PaymentHistory buildPayment(String id, String amonut) {

		PaymentHistory result = new PaymentHistory();
		result.setAmount(new BigDecimal(amonut));
		result.setRequest("12345678-0001-1234-1234-123456789012");
		result.setMerchantOrder("2017041601");

		return result;
	}
	
	private CardToken createCardToken(String merchantId, String merchantKey, CreditCard card) {
		
		// Configure seu merchant
		Merchant merchant = new Merchant(merchantId, merchantKey);

		// Informe os dados do cartão que irá tokenizar
		CardToken cardToken = new CardToken().setBrand(card.getBrand())
		                                     .setCardNumber(card.getCardNumber())
		                                     .setHolder(card.getHolder())
		                                     .setExpirationDate(card.getExpirationDate());

		// Crie o Token para o cartão
		try {
			// // Configure o SDK com seu merchant e o ambiente apropriado para
			// gerar o token
			cardToken = new CieloEcommerce(merchant, Environment.SANDBOX).createCardToken(cardToken);

		} catch (CieloRequestException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return cardToken;
	}
	
	public PaymentHistory findSaleByPayment(String merchantId, String merchantKey, String payment) {

		PaymentHistory result = new PaymentHistory();
		Merchant merchant = new Merchant(merchantId, merchantKey);
		
		// Crie o pagamento na Cielo
		try {
			// Crie uma instância de Sale informando o ID do pagamento
			Sale sale = new Sale(payment);

		    // Configure o SDK com seu merchant e o ambiente apropriado para criar a venda
		    sale = new CieloEcommerce(merchant, Environment.SANDBOX).querySale(payment);
		    
			result.setAmount(new BigDecimal(sale.getPayment().getAmount()));
			result.setRequest(sale.getPayment().getAuthorizationCode());
			result.setMerchantOrder(sale.getMerchantOrderId());
			result.setStatus(sale.getPayment().getStatus());
			result.setPayment(sale.getPayment().getPaymentId());

		} catch (CieloRequestException e) {
		    // Em caso de erros de integração, podemos tratar o erro aqui.
		    // os códigos de erro estão todos disponíveis no manual de integração.
		    CieloError error = e.getError();
		    result.setStatus(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;		
	}
	
	public PaymentHistory createSale(String merchantId, String merchantKey, String amount, String cardNumber, String customerName) {

		System.out.println("Sale->create: merchantId="+merchantId+", merchantKey="+merchantKey+", amount="+amount+", cardNumber="+cardNumber+", customerName="+customerName);
		
		PaymentHistory paymentHistory = new PaymentHistory();
		paymentHistory.setAmount(new BigDecimal(amount));
		paymentHistory.setRequest("12345678-0001-1234-1234-123456789012");
		paymentHistory.setMerchantOrder("2017041601");
		
		CreditCard card = new CreditCard("123", "Visa");
		card.setExpirationDate("12/2018");
		card.setCardNumber(cardNumber); // "0000000000000001" -> "ReturnCode":"4","ReturnMessage":"Operation Successful","Status":1
		card.setHolder("Npwit da Silva");
		
		// Configure merchant
		Merchant merchant = new Merchant(merchantId, merchantKey);

		// Create a Sale instance
		Sale sale = new Sale(paymentHistory.getPayment());
		sale.setMerchantOrderId(paymentHistory.getMerchantOrder());
		
		// Create a Customer instance
		Customer customer = sale.customer(customerName);

		// Create a Payment instance
		Payment payment = sale.payment(new Integer(amount));
		
		// Create a CrediCard instance
		// See SDK manual, version 3.0
		payment.creditCard(card.getSecurityCode(), card.getBrand()).setExpirationDate(card.getExpirationDate())
		                                 .setCardNumber(card.getCardNumber())
		                                 .setHolder(card.getHolder());	

		// Create payment in Cielo
		try {
		    // Configure o SDK com seu merchant e o ambiente apropriado para criar a venda
		    sale = new CieloEcommerce(merchant, Environment.SANDBOX).createSale(sale);

		    // Com a venda criada na Cielo, já temos o ID do pagamento, TID e demais
		    // dados retornados pela Cielo
		    String paymentId = sale.getPayment().getPaymentId();
		    paymentHistory.setPayment(paymentId);
		    paymentHistory.setStatus(new Integer(sale.getPayment().getReturnCode()));
		    paymentHistory.setAmount(new BigDecimal(sale.getPayment().getAmount()));
		    CardToken cardToken = this.createCardToken(merchantId, merchantKey, card);
		    paymentHistory.setCardToken(cardToken.getCardToken());

		} catch (CieloRequestException e) {
		    // Em caso de erros de integração, podemos tratar o erro aqui.
		    // os códigos de erro estão todos disponíveis no manual de integração.
		    CieloError error = e.getError();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		log.info("Nova venda com cartao de credito criada: " + paymentHistory.getPayment());
		
		return paymentHistory;
	}

}