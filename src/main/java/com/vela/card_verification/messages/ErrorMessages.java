package com.vela.card_verification.messages;

public enum ErrorMessages {
	
	NO_RECORD_FOUND("the input record could not be found, card may not be registered on server"),
	EMPTY_LIST("the input record could not be found, list is empty"),
	WRONG_INPUT("invalid or incomplete card number");
	

	
	
	private String errorMessages;

	ErrorMessages(String errorMessages) {
		this.errorMessages = errorMessages;
	}

	public String getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(String errorMessages) {
		this.errorMessages = errorMessages;
	}
	
	
}
