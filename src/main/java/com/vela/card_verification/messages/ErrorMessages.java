package com.vela.card_verification.messages;

public enum ErrorMessages {
	
	INTERNAL_SERVER_ERROR("ineternal server error. please contact the administrator"),
	NO_RECORD_FOUND("the input record could not be found, card may not be registered on server"),
	EMPTY_LIST("the input record could not be found, list is empty");
	

	
	
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
