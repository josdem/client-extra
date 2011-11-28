package com.all.facebook.impl;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.all.facebook.FacebookService;
import com.all.facebook.FacebookServiceException;
import com.all.facebook.FacebookServiceOAuthException;
import com.all.facebook.FacebookUser;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookException;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.FacebookType;
import com.restfb.types.User;

@Service
public class FacebookServiceImpl implements FacebookService {

	private static final String PASSWORD = "225ce6bf7a193870690514db2e0259f5";

	private static final String PIPE = "|";

	// given in the application settings page in Facebook
	private static final String API_KEY = "455cd6bc104968b10e9b4ea1335dc57e";

	// App authentication is handled by verifying that the redirect_uri is in the same domain as
	// the Site URL configured in the Developer App.
	private static final String REDIRECT_URI = "www.facebook.com/connect/login_success.html";

	// for more info: http://developers.facebook.com/docs/authentication/
	// check Client-side Flow, for not exposing the app secret
	// "The app secret is available from the Developer App and should not be shared with anyone or embedded
	// in any code that you will distribute (you should use the client-side flow for these scenarios)."
	private static final String AUTHORIZATION_URL = "https://graph.facebook.com/oauth/authorize?"
			+ "client_id=159880797410187" 
			+ "&redirect_uri=http://" + REDIRECT_URI
			+ "&scope=xmpp_login,offline_access,publish_stream" // offline_access == access token expires=0
			+ "&display=popup" + "&response_type=token";// this enables the client-side flow

	private static final String ENCODING_ISO_8859_1 = "ISO-8859-1";

	private static final String EQUALS = "=";

	private static final String AMPERSAND = "&";

	private FacebookClient facebookClient;

	private String accessToken;

	@Override
	public String authorizationUrl() {
		return AUTHORIZATION_URL;
	}

	@Override
	public String redirectUrl() {
		return REDIRECT_URI;
	}

	@Override
	public String authorizeUrl(String responseUrl) throws FacebookServiceException {
		URL url;

		try {
			url = new URL(responseUrl);
		} catch (MalformedURLException e) {
			throw new FacebookServiceException(e);
		}

		if (!responseUrl.contains(REDIRECT_URI)) {
			throw new FacebookServiceException("The url does not correpond with the redirect_uri parameter: " + responseUrl);
		}

		String ref = url.getRef();
		if (ref == null) {
			Map<String, String> error = extractKeyValuePairs(url.getQuery());
			String errorType = error.get("error");
			throw new FacebookServiceException(error.toString(), errorType);
		}

		Map<String, String> keyValuePairs = extractKeyValuePairs(ref);

		if (!keyValuePairs.containsKey("access_token")) {
			throw new FacebookServiceException("Could not find the access token in the responseUrl: " + responseUrl);
		}

		authorize(keyValuePairs.get("access_token"));
		
		return accessToken;
	}

	private Map<String, String> extractKeyValuePairs(String queryString) throws FacebookServiceException {
		try {

			Map<String, String> keyValuePairs = new HashMap<String, String>();
			StringTokenizer pairs = new StringTokenizer(queryString, AMPERSAND);
			while (pairs.hasMoreTokens()) {
				String pair = pairs.nextToken();
				StringTokenizer parts = new StringTokenizer(pair, EQUALS);
				String key = URLDecoder.decode(parts.nextToken(), ENCODING_ISO_8859_1);
				String value = URLDecoder.decode(parts.nextToken(), ENCODING_ISO_8859_1);
				keyValuePairs.put(key, value);
			}
			return keyValuePairs;

		} catch (UnsupportedEncodingException e) {
			throw new FacebookServiceException(e);
		}
	}

	@Override
	public String getXmppUser(String accessToken) throws FacebookServiceException {
		String[] keyArray = validateAccessTokenFormat(accessToken);
		String sessionKey = keyArray[1];
		return new StringBuilder(API_KEY).append(PIPE).append(sessionKey).toString();
	}

	@Override
	public String getXmppPassword(String accessToken) throws FacebookServiceException {
		return PASSWORD;
	}

	private String[] validateAccessTokenFormat(String accessToken) throws FacebookServiceException {
		String[] keyArray = accessToken.split(Pattern.quote(PIPE));
		;
		if (keyArray.length != 3) {
			throw new FacebookServiceException("Invalid access token format");
		}
		return keyArray;
	}

	@Override
	public void authorize(String accessToken) throws FacebookServiceException {
		try {
			this.accessToken = accessToken;
			// if facebook responses with no error then we have a valid access token
			getFacebookClient().fetchObject("me", User.class);
		} catch(FacebookOAuthException foae){
			this.accessToken = null;
			this.facebookClient = null;
			throw new FacebookServiceOAuthException(foae.getErrorMessage(), foae.getErrorType());
		} catch (FacebookServiceException fse) {
			this.accessToken = null;
			this.facebookClient = null;
			throw fse;
		} catch (FacebookException e) {
			this.accessToken = null;
			this.facebookClient = null;
			throw new FacebookServiceException(e);
		}
	}

	@Override
	public void publishFeed(String connection, String message) throws FacebookServiceException {
		try {
			getFacebookClient().publish(connection + "/feed", FacebookType.class, Parameter.with("message", message));
		} catch(FacebookOAuthException foae){
			throw new FacebookServiceOAuthException(foae.getErrorMessage(), foae.getErrorType());
		} catch (FacebookException e) {
			throw new FacebookServiceException(e);
		}
	}

	@Override
	public List<FacebookUser> getFriends() throws FacebookServiceException {
		List<FacebookUser> friends = new ArrayList<FacebookUser>();
		Connection<User> friendConnections = getFacebookClient().fetchConnection("me/friends", User.class);
		for (User user : friendConnections.getData()) {
			friends.add(adapt(user));
		}
		return friends;
	}

	private FacebookUser adapt(User user) {
		return new FacebookUserImpl(user.getId(), user.getName());
	}

	private FacebookClient getFacebookClient() throws FacebookServiceException {
		if (facebookClient == null) {
			if (accessToken == null) {
				throw new FacebookServiceException("Unable to create facebook client without an access token");
			}
			facebookClient = new DefaultFacebookClient(accessToken);
		}
		return facebookClient;
	}
}
