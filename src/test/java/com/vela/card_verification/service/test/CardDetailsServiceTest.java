package com.vela.card_verification.service.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import com.vela.card_verification.model.CardInfo;
import com.vela.card_verification.model.Payload;
import com.vela.card_verification.pojo.Bank;
import com.vela.card_verification.pojo.Country;
import com.vela.card_verification.pojo.ExtractPojo;
import com.vela.card_verification.pojo.Number;
import com.vela.card_verification.repository.CardInfoRepository;
import com.vela.card_verification.responses.InfoResponse;
import com.vela.card_verification.service.impl.CardDetailsInfoServiceImpl;

class CardDetailsServiceTest {
	@InjectMocks
	CardDetailsInfoServiceImpl service;
	@Mock
	CardInfoRepository repo;
	@Mock
	RestTemplate restTemplate;
	
	CardInfo info;
	ExtractPojo pojo;
	InfoResponse response;
	Payload payload;
	Bank bank;
	Country country;
	com.vela.card_verification.pojo.Number number;


	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		info = new CardInfo();
		pojo = new ExtractPojo();
		response = new InfoResponse();
		info.setSearchAmount(3);
		info.setSuccess(true);
		pojo.setType("debit");
		pojo.setBrand("diamond");
		pojo.setScheme("money");
		response.setSuccess(true);
		payload = new Payload();
		payload.setBank("fidelity");
		payload.setType("visa");
		bank = new Bank();
		bank.setName("uba");
		bank.setPhone("09066677742");
		country = new Country();
		country.setCurrency("naira");
		country.setName("nigeria");
		country.setLatitude(3);
		number = new Number();
		number.setLength(4);
		number.setLuhn(true);
		pojo = new ExtractPojo();
		pojo.setBank(bank);
		pojo.setBrand("global");
		pojo.setCountry(country);
		pojo.setNumber(number);
		pojo.setPrepaid(true);
	}

	@Test
	void testGetCardInfo() {
		when(repo.findByCardNumber(anyString())).thenReturn(info);
		when(repo.save(any(CardInfo.class))).thenReturn(info);
		InfoResponse res = service.getCardInfo("4187451728321110");
		assertNotNull(res);
	}

	@Test
	void testGetCardStats() {
		fail("Not yet implemented");
	}

	@Test
	void testAllCards() {
		fail("Not yet implemented");
	}

}
