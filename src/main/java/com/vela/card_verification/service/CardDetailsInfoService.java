package com.vela.card_verification.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vela.card_verification.model.CardInfo;
import com.vela.card_verification.responses.InfoResponse;
import com.vela.card_verification.responses.StatsResponse;

@Service
public interface CardDetailsInfoService {

	public InfoResponse getCardInfo(String cardNumber);
	public StatsResponse getCardStats(int start, int limit);
	public List<CardInfo> allCards();
	
}
