package com.vela.card_verification.responses;

import com.vela.card_verification.model.Payload;

import lombok.Data;

@Data
public class InfoResponse {

	private Boolean success;
	private Payload payload;

}
