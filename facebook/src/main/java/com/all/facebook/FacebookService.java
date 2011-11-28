package com.all.facebook;

import java.util.List;

public interface FacebookService {

	/**
	 * Gets the facebook authentication url for the OAuth dialog
	 * 
	 * @return the facebook authentication url
	 */
	String authorizationUrl();

	/**
	 * Gets the redirect uri parameter from the OAuth dialog
	 * 
	 * @return the redirect uri parameter from the OAuth dialog
	 */
	String redirectUrl();

	/**
	 * Given the responseUrl obtained, this metohd will extract the access token
	 * 
	 * @param responseUrl
	 * @return the access token
	 * @throws FacebookServiceException
	 *           if could not obtain the acces token
	 */
	String authorizeUrl(String responseUrl) throws FacebookServiceException;

	/**
	 * Forms the xmpp user to login into the xmpp facebook chat <br>
	 * The password is obtained using the {@link #promoteSessionUrl(String)} 
	 * @param accessToken
	 * @return
	 * @throws FacebookServiceException 
	 */
	String getXmppUser(String accessToken) throws FacebookServiceException;

	String getXmppPassword(String accessToken) throws FacebookServiceException;

	void authorize(String accessToken) throws FacebookServiceException;
	
	void publishFeed(String connection, String message) throws FacebookServiceException;

	List<FacebookUser> getFriends() throws FacebookServiceException;
	
}
