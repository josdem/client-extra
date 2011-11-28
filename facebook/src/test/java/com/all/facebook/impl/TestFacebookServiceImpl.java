package com.all.facebook.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import com.all.facebook.FacebookServiceException;
import com.restfb.FacebookClient;
import com.restfb.exception.FacebookException;
import com.restfb.types.User;

public class TestFacebookServiceImpl {

	@InjectMocks
	private FacebookServiceImpl facebookService = new FacebookServiceImpl();
	@Mock
	private FacebookClient facebookClient;
	
	private String accessToken = "159880797410187|c201db9b23175e2dc1bedb7a.1-100002261422146|Y-bBYZ8Xs4DPWQ3qYvZg_oJoc8k";

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void shouldGetAuthUrl() throws Exception {
		//https://graph.facebook.com/oauth/authorize?client_id=159880797410187&redirect_uri=http://www.facebook.com/connect/login_success.html&scope=xmpp_login,offline_access,publish_stream&display=popup&response_type=token
		String authorizationUrl = facebookService.authorizationUrl();
		new URL(authorizationUrl); // no exception if url well constructed
	}

	@Test
	public void shouldGetRedirectUrl() throws Exception {
		String redirectUrl = "http://" + facebookService.redirectUrl();
		new URL(redirectUrl); // no exception if url well constructed
	}
	
	@Test
	public void shouldAuthorizeUrl() throws Exception {
		String accessToken = "159880797410187|c86aeca09058f40ced0a5203.1-100002261422146|AbYOi2e-4oqy49De2sgC7SA72Ko";
		String responseUrl = new StringBuilder("http://www.facebook.com/connect/login_success.html#access_token=")
				.append(URLEncoder.encode(accessToken, "ISO-8859-1")).append("&expires_in=0").toString();

		String result = facebookService.authorizeUrl(responseUrl);

		verify(facebookClient).fetchObject("me", User.class);
		
		assertEquals(accessToken, result);
	}

	@Test(expected = FacebookServiceException.class)
	public void shouldFailAuthorizeUrlBecauseRedirectUriDoesNotMatch() throws Exception {
		String responseUrl = "http://other.uri.com/";
		facebookService.authorizeUrl(responseUrl);
	}

	@Test
	public void shouldFailAuthorizeBecauseMalformedUrl() {
		try {
			String responseUrl = "not.an.url";
			facebookService.authorizeUrl(responseUrl);
		} catch (FacebookServiceException e) {
			assert e.getCause() instanceof MalformedURLException;
		}
	}

	@Test(expected = FacebookServiceException.class)
	public void shouldAuthorizeBecasueNoAccessTokenFound() throws Exception {
		String responseUrl = "http://www.facebook.com/connect/login_success.html#expires_in=0";
		facebookService.authorizeUrl(responseUrl);
	}

	@Test(expected = FacebookServiceException.class)
	public void shouldFailAuthorizeBecauseUserDeny() throws Exception {
		String responseUrl = "http://www.facebook.com/connect/login_success.html?error_reason=user_denied&error=access_denied&error_description=The+user+denied+your+request.";
		facebookService.authorizeUrl(responseUrl);
	}

	@Test
	public void shouldGetXmppUser() throws Exception {
		String xmppUser = facebookService.getXmppUser(accessToken);
		assertEquals("455cd6bc104968b10e9b4ea1335dc57e|c201db9b23175e2dc1bedb7a.1-100002261422146", xmppUser);
	}

	@Test(expected=FacebookServiceException.class)
	public void shouldFailBecasueInvalidAccessTokenFormat() throws Exception {
		facebookService.getXmppUser("invalidAccessToken");
	}
	
	@Test
	public void shouldGetXmppPassword() throws Exception {
		String xmppPassword = facebookService.getXmppPassword(accessToken);
		assertNotNull(xmppPassword);
	}
	
	@Test
	public void shouldAuthorize() throws Exception {
		facebookService.authorize(accessToken);
		verify(facebookClient).fetchObject("me", User.class);
	}
	
	@Test(expected=FacebookServiceException.class)
	public void shouldThrowExcpetionIfAccessTokenInvalid() throws Exception {
		FacebookException mock = mock(FacebookException.class);
		when(facebookClient.fetchObject("me", User.class)).thenThrow(mock);
		facebookService.authorize(accessToken);
	}
}
