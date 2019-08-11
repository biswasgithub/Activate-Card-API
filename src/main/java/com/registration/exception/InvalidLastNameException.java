package com.registration.exception;

public class InvalidLastNameException extends Exception{

	private static final long serialVersionUID = 1L;

	public InvalidLastNameException(String message){
		super(message);
	}
}
