package com.all.facebook;

public class FacebookServiceException extends Exception {

	public static final String FACEBOOK_USER_ACCESS_DENIED = "access_denied";

	private static final long serialVersionUID = 8972567933546561832L;

	private String errorType;

	public FacebookServiceException(String message, String errorType) {
		super(message);
		this.errorType = errorType;
	}

	public FacebookServiceException(String message) {
		super(message);

	}

	public FacebookServiceException(Throwable e) {
		super(e);
	}

	public boolean isAccessDenied() {
		return errorType != null && errorType.equals(FACEBOOK_USER_ACCESS_DENIED);
	}
}
