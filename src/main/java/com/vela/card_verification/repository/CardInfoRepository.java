package com.vela.card_verification.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.vela.card_verification.model.CardInfo;

@Repository
public interface CardInfoRepository extends PagingAndSortingRepository<CardInfo, Long>{

	@Query(value = "SELECT * FROM card_info",nativeQuery = true)
	Page<CardInfo> allCards(Pageable page);
	
	CardInfo findByCardNumber(String cardNumber);
	
	@Query(value = "SELECT * FROM card_info",nativeQuery = true)
	List<CardInfo> getAll();
}
