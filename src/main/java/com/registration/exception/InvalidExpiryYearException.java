package com.registration.exception;

public class InvalidExpiryYearException extends Exception{

	private static final long serialVersionUID = 1L;

	public InvalidExpiryYearException(String message) {
		super(message);
	}	
}
