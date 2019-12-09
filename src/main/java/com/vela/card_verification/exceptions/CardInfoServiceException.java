package com.vela.card_verification.exceptions;

public class CardInfoServiceException extends RuntimeException {

	private static final long serialVersionUID = -8274909731175076091L;
	private String message;

	public CardInfoServiceException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
