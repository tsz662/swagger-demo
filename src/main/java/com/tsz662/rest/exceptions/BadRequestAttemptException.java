package com.tsz662.rest.exceptions;

public class BadRequestAttemptException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BadRequestAttemptException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public BadRequestAttemptException(String message) {
		super(message);
	}
	
	public BadRequestAttemptException(Throwable cause) {
		super(cause);
	}
	
	public BadRequestAttemptException(){}

}
