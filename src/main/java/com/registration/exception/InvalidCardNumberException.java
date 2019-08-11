package com.registration.exception;

public class InvalidCardNumberException extends Exception{

	private static final long serialVersionUID = 1L;

	public InvalidCardNumberException(String message) {
		super(message);	
	}
}
