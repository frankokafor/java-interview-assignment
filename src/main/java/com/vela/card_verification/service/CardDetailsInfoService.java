package com.vela.card_verification.service;

import org.springframework.stereotype.Service;

import com.vela.card_verification.responses.InfoResponse;
import com.vela.card_verification.responses.StatsResponse;

@Service
public interface CardDetailsInfoService {

	public InfoResponse getCardInfo(String cardNumber);
	public StatsResponse getCardStats(int start, int limit);
	
}
