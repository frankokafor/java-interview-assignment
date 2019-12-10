package com.vela.card_verification.messages;

import java.util.Date;

import lombok.Data;

@Data
public class ErrorMessage {

	private Date timestamp;
	private String message;

	public ErrorMessage() {
		super();
	}

	public ErrorMessage(Date timestamp, String message) {
		super();
		this.timestamp = timestamp;
		this.message = message;
	}
}
