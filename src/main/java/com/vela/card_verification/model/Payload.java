package com.vela.card_verification.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class Payload implements Serializable {

	private static final long serialVersionUID = 1L;
	private String scheme;
	private String type;
	private String bank;

	public Payload() {
		super();
	}

	public Payload(String scheme, String type, String bank) {
		super();
		this.scheme = scheme;
		this.type = type;
		this.bank = bank;
	}
}
