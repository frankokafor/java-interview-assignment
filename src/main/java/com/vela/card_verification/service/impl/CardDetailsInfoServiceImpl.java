package com.vela.card_verification.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.vela.card_verification.exceptions.CardInfoServiceException;
import com.vela.card_verification.messages.ErrorMessages;
import com.vela.card_verification.model.CardInfo;
import com.vela.card_verification.model.Payload;
import com.vela.card_verification.pojo.ExtractPojo;
import com.vela.card_verification.repository.CardInfoRepository;
import com.vela.card_verification.responses.InfoResponse;
import com.vela.card_verification.responses.StatsResponse;
import com.vela.card_verification.service.CardDetailsInfoService;


@Service
public class CardDetailsInfoServiceImpl implements CardDetailsInfoService {
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private CardInfoRepository repo;
	@Value("${binlist.url}")
	String binUrl;

	@Override
	public InfoResponse getCardInfo(String cardNumber) {
		return cardInfo(cardNumber);
	}

	@Override
	public StatsResponse getCardStats(int start, int limit) {
		return getAllCardStats(start, limit);
	}

	private String getFirstEightChar(String number) {
		String num = number.substring(0, 8);
		return num;
	}

	private InfoResponse cardInfo(String number) {
		int nums = 0;
		String num = getFirstEightChar(number);
		CardInfo info = repo.findByCardNumber(number);
		if (info == null) {
			ExtractPojo pojo = restTemplate.getForObject(binUrl + num, ExtractPojo.class);
			if (pojo == null) {
				throw new CardInfoServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
			}
			Payload payload = new Payload(pojo.getScheme(), pojo.getType(), pojo.getBank().getName());
			CardInfo cardInfo = new CardInfo(payload, nums + 1, true, number);
			try {
				CardInfo returnValue = repo.save(cardInfo);
				if (returnValue != null)
					return cardResponse(returnValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		info.setSearchAmount(info.getSearchAmount() + 1);
		CardInfo returnValue = repo.save(info);
		return cardResponse(returnValue);
	}

	private InfoResponse cardResponse(CardInfo info) {
		Payload payload = info.getPayload();
		InfoResponse response = new InfoResponse();
		response.setSuccess(info.getSuccess());
		response.setPayload(payload);
		return response;
	}

	private StatsResponse getAllCardStats(int start, int limit) {
		Map<String, Integer> payload = new HashMap<>();
		StatsResponse response = new StatsResponse();
		if (start > 0)
			start -= 1;
		Pageable request = PageRequest.of(start, limit);
		Page<CardInfo> cardInfoPages = repo.allCards(request);
		List<CardInfo> cards = cardInfoPages.getContent();
		if (cards.isEmpty()) {
			throw new CardInfoServiceException(ErrorMessages.EMPTY_LIST.getErrorMessages());
		}
		cards.forEach(card -> {
			payload.put(card.getCardNumber(), card.getSearchAmount());
		});
		response.setLimit(limit);
		response.setSize(cards.size());
		response.setStart(start);
		response.setPayload(payload);
		response.setSuccess(true);
		return response;
	}

	@Override
	public List<CardInfo> allCards() {
		return repo.getAll();
	}

}
