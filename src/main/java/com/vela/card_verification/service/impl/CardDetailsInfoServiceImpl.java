package com.vela.card_verification.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.vela.card_verification.exceptions.CardInfoServiceException;
import com.vela.card_verification.exceptions.InvalidInputException;
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
	private Logger log = LoggerFactory.getLogger(CardDetailsInfoServiceImpl.class);

	@Override
	public InfoResponse getCardInfo(String cardNumber) {
		return cardInfo(cardNumber);
	}

	@Override
	public StatsResponse getCardStats(int start, int limit) {
		return getAllCardStats(start, limit);
	}

	//checks if number is complete and valid, also remove spaces
	private String validateInput(String number) {
		String nospace = number.replaceAll("\\s+", "");
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(nospace);
		boolean b = m.find();
		if(nospace.chars().anyMatch(Character::isLetter) || b) {
			log.info("invalid card number");
			throw new InvalidInputException(ErrorMessages.WRONG_INPUT.getErrorMessages());
		}
		String num = nospace.substring(0, 8);
		log.info("card verified");
		return num;
	}

	/*takes in the validated card number and checks if its already in the database
	 * if not it makes an external api call
	 * 
	 */
	@Async
	private InfoResponse cardInfo(String number) {
		int nums = 0;
		String num = validateInput(number);
		CardInfo info = repo.findByCardNumber(num);
		if (info == null) {
			ExtractPojo pojo = restTemplate.getForObject(binUrl + num, ExtractPojo.class);
			if (pojo == null) {
				throw new CardInfoServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessages());
			}
			Payload payload = new Payload(pojo.getScheme() == null ? "" : pojo.getScheme(), 
					pojo.getType() == null ? "" : pojo.getType(), 
					pojo.getBank().getName() == null ? "" : pojo.getBank().getName());
			CardInfo cardInfo = new CardInfo(payload, nums + 1, true, num);
			cardInfo.setLatestRequest(new Date());
			try {
				CardInfo returnValue = repo.save(cardInfo);
				if (returnValue != null)
					return cardResponse(returnValue);
			} catch (Exception e) {
				log.info("unable to save object");
			}
		}
		info.setSearchAmount(info.getSearchAmount() + 1);
		return cardResponse(repo.save(info));
	}

	//map card details to the response payload
	private InfoResponse cardResponse(CardInfo info) {
		Payload payload = info.getPayload();
		InfoResponse response = new InfoResponse();
		response.setSuccess(info.getSuccess() == null ? false : info.getSuccess());
		response.setPayload(payload);
		return response;
	}

	@Async
	private StatsResponse getAllCardStats(int start, int limit) {
		Map<String, Integer> payload = new HashMap<>();
		StatsResponse response = new StatsResponse();
		if (start > 0)
			start -= 1;
		Pageable request = PageRequest.of(start, limit);
		Page<CardInfo> cardInfoPages = repo.allCards(request);
		List<CardInfo> cards = cardInfoPages.getContent();
		if (cards.isEmpty()) {
			log.info("list is empty");
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
		return repo.findAll();
	}

}
