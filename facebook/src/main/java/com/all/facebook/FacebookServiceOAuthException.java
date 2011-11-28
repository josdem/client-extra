package com.all.facebook;


public class FacebookServiceOAuthException extends FacebookServiceException {

	private static final long serialVersionUID = -977916176526969775L;

	public FacebookServiceOAuthException(String message, String errorType) {
		super(message, errorType);
	}
}
