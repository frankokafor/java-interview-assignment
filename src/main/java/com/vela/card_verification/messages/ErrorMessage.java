package com.vela.card_verification.messages;

import java.util.Date;

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

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
