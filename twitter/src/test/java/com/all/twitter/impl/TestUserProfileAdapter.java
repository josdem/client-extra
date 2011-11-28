package com.all.twitter.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.User;

import com.all.testing.MockInyectRunner;
import com.all.testing.UnderTest;
import com.all.twitter.TwitterStatus;
import com.all.twitter.UserProfile;

@RunWith(MockInyectRunner.class)
public class TestUserProfileAdapter {
	@UnderTest
	UserProfileAdapter adapter;
	@Mock
	private User user;
	@Mock
	Status status;
	@Mock
	TwitterStatus twitterStatus;
	@Mock
	TwitterAdapter twitterAdapter;
	@Mock
	ResponseList<Status> userTimeline;

	private String name = "name";
	private String screenName = "screenName";
	private String description = "description";
	private String location = "location";
	private long id = 1;
	private int followersCount = 7;
	private int friendsCount = 8;
	private int tweetCount = 5000;
	private URL url;

	@Before
	public void setup() throws Exception {
		url = new URL("http://localhost");
	}

	@Test
	public void shouldConvertFromUserToUserProfile() throws Exception {
		setExpectations();
		UserProfile userProfile = adapter.convertFrom(user);

		assertEquals(name, userProfile.getName());
		assertEquals(id, userProfile.getId());
		assertEquals(screenName, userProfile.getScreenName());
		assertEquals(description, userProfile.getDescription());
		assertEquals(location, userProfile.getLocation());
		assertEquals(url.toString(), userProfile.getURL());
		assertEquals(followersCount, userProfile.getFollowersCount());
		assertEquals(friendsCount, userProfile.getFriendsCount());
		assertEquals(tweetCount, userProfile.getTweetsCount());
		assertEquals(url, userProfile.getImageProfileUrl());
		assertFalse(userProfile.isFollowing());
	}

	private void setExpectations() {
		when(user.getName()).thenReturn(name);
		when(user.getId()).thenReturn(id);
		when(user.getScreenName()).thenReturn(screenName);
		when(user.getDescription()).thenReturn(description);
		when(user.getLocation()).thenReturn(location);
		when(user.getURL()).thenReturn(url);
		when(user.getFollowersCount()).thenReturn(followersCount);
		when(user.getFriendsCount()).thenReturn(friendsCount);
		when(user.getStatusesCount()).thenReturn(tweetCount);
		when(user.getProfileImageURL()).thenReturn(url);
	}
}
