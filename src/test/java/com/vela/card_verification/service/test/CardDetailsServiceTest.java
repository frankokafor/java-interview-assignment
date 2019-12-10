package com.vela.card_verification.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;

import com.vela.card_verification.exceptions.CardInfoServiceException;
import com.vela.card_verification.exceptions.InvalidInputException;
import com.vela.card_verification.model.CardInfo;
import com.vela.card_verification.model.Payload;
import com.vela.card_verification.pojo.Bank;
import com.vela.card_verification.pojo.ExtractPojo;
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
	
	@Value("${binlist.url}")
	String binUrl;
	
	CardInfo info;
	InfoResponse response;
	Payload payload;
	Bank bank;


	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		info = new CardInfo();
		response = new InfoResponse();
		info.setSearchAmount(3);
		info.setSuccess(true);
		response.setSuccess(true);
		payload = new Payload();
		payload.setBank("fidelity");
		payload.setType("visa");
		bank = new Bank();
		bank.setName("uba");
		bank.setPhone("09066677742");

	}

	@Test
	void testGetCardInfo() {
		when(repo.findByCardNumber(anyString())).thenReturn(info);
		when(repo.save(any(CardInfo.class))).thenReturn(info);
		InfoResponse res = service.getCardInfo("4187451728321110");
		assertNotNull(res);
		assertEquals(res.getSuccess(), info.getSuccess());
		verify(restTemplate,times(0)).getForObject("4187451728321110", ExtractPojo.class);
	}

	@Test
	void testGetCardStats() {
		Pageable request = PageRequest.of(0, 2);
		when(repo.allCards(request)).thenReturn(null);
		assertThrows(NullPointerException.class, () -> {
			service.getCardStats(0, 2);
		});
	}

	@Test
	void test_CardInfoServiceException() {
		when(repo.findByCardNumber(anyString())).thenReturn(null);
		assertThrows(CardInfoServiceException.class, () -> {
			service.getCardInfo("41874517345678976");
		});
	}
	
	@Test
	void test_InvalidInputException() {
		assertThrows(InvalidInputException.class, () -> {
			service.getCardInfo("418745173ygfdcvfrew");
		});
		when(repo.findByCardNumber("4187451728321110")).thenReturn(null);
		when(repo.save(any(CardInfo.class))).thenReturn(info);
		verify(restTemplate,times(0)).getForObject(binUrl + "4187451728321110", ExtractPojo.class);
	}

}
