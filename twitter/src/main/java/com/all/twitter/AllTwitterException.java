package com.all.twitter;

import twitter4j.TwitterException;

public class AllTwitterException extends Exception {

	private static final long serialVersionUID = -3981209071425372956L;

	public AllTwitterException(String message, Throwable cause) {
		super(message, cause);
	}

	public AllTwitterException(String message) {
		super(message);
	}

	public int getStatusCode() {
		if (getCause() instanceof TwitterException) {
			TwitterException twitterException = (TwitterException) getCause();
			return twitterException.getStatusCode();
		}
		return -1;
	}
}
