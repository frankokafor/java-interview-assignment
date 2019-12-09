package com.vela.card_verification.exceptions;

public class InvalidInputException extends RuntimeException {

	private static final long serialVersionUID = -8274909731175076091L;
	private String message;

	public InvalidInputException(String message) {
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
