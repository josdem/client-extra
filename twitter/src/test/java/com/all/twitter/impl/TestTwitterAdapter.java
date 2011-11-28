package com.all.twitter.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.User;

import com.all.testing.MockInyectRunner;
import com.all.testing.UnderTest;
import com.all.twitter.TwitterStatus;
import com.all.twitter.TwitterStatus.TwitterStatusType;
import com.all.twitter.UserProfile;

@RunWith(MockInyectRunner.class)
public class TestTwitterAdapter {
	@UnderTest
	private TwitterAdapter twitterState;
	@Mock
	private Status status;
	@Mock
	private DirectMessage directMessage;
	@Mock
	private User user;
	@Mock
	private Date date;

	private URL url;
	private String screenName = "josdem";
	private String text = "twitter";
	private String source = "source";
	private long contactId = 10;
	private long id = 10;
	private final static String directMessageSource = "Direct Message";

	@Before
	public void setup() throws Exception {
		url = new URL("http://localhost/");
		when(user.getProfileImageURL()).thenReturn(url);
		when(user.getScreenName()).thenReturn(screenName);
		when(user.getId()).thenReturn(contactId);
		when(user.getName()).thenReturn("username");
		when(user.getDescription()).thenReturn("description");
		when(user.getLocation()).thenReturn("location");
		when(user.getURL()).thenReturn(url);
		when(status.getUser()).thenReturn(user);
		when(status.getCreatedAt()).thenReturn(date);
		when(status.getText()).thenReturn(text);
		when(status.getSource()).thenReturn(source);
		when(status.getId()).thenReturn(id);

		when(directMessage.getSender()).thenReturn(user);
		when(directMessage.getCreatedAt()).thenReturn(date);
		when(directMessage.getText()).thenReturn(text);
		when(directMessage.getSenderId()).thenReturn(contactId);
	}

	@Test
	public void shouldConvertFromTwitterStatusToAllTwitterStatus() throws Exception {
		TwitterStatus twitterStatus = twitterState.convertFrom(status, TwitterStatusType.FRIENDS);
		assertEquals(url, twitterStatus.getProfileImageUrl());
		assertEquals(date, twitterStatus.getDateCreatedAt());
		assertEquals(screenName, twitterStatus.getScreenName());
		assertEquals(text, twitterStatus.getText());
		assertEquals(source, twitterStatus.getSource());
		assertEquals(contactId, twitterStatus.getSenderId());
		assertEquals(TwitterStatusType.FRIENDS, twitterStatus.getType());
		assertEquals(id, twitterStatus.getId());
	}

	@Test
	public void shouldConvertFromDirectMessageToAllTwitterStatus() throws Exception {
		TwitterStatus twitterStatus = twitterState.convertFrom(directMessage);
		assertEquals(url, twitterStatus.getProfileImageUrl());
		assertEquals(date, twitterStatus.getDateCreatedAt());
		assertEquals(screenName, twitterStatus.getScreenName());
		assertEquals(text, twitterStatus.getText());
		assertEquals(directMessageSource, twitterStatus.getSource());
		assertEquals(contactId, twitterStatus.getSenderId());
	}

	@Test
	public void shouldConvertFromContact() throws Exception {
		TwitterStatus twitterStatus = twitterState.convertFromContact(user, status);
		assertEquals(url, twitterStatus.getProfileImageUrl());
		assertEquals(date, twitterStatus.getDateCreatedAt());
		assertEquals(screenName, twitterStatus.getScreenName());
		assertEquals(text, twitterStatus.getText());
		assertEquals(source, twitterStatus.getSource());
		assertEquals(contactId, twitterStatus.getSenderId());
	}

	@Test
	public void shouldNotExtractURLifNotExist() throws Exception {
		User user = mock(User.class);
		UserProfile userProfile = twitterState.convertFrom(user);
		assertEquals(StringUtils.EMPTY, userProfile.getURL());
	}
}
