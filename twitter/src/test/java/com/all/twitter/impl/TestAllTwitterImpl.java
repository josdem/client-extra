package com.all.twitter.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.Authorization;

import com.all.twitter.AllTwitterException;
import com.all.twitter.TwitterStatus;
import com.all.twitter.TwitterStatus.TwitterStatusType;
import com.all.twitter.UserProfile;

public class TestAllTwitterImpl {
	private static final int COUNT = 100;
	private static final int PAGE = 1;
	@InjectMocks
	private AllTwitterImpl allTwitter = new AllTwitterImpl();
	@Mock
	private Twitter twitter;
	@Mock
	private XAuthTwitterFactory xAuthTwitterFactory;
	@Mock
	private Authorization authorization;
	@Mock
	private User user;
	@Mock
	private ResponseList<Status> responseList;
	@Mock
	private ResponseList<DirectMessage> directMessagesList;
	@Mock
	private Status status;
	@Mock
	private DirectMessage directMessage;
	@Mock
	private UserProfileAdapter userProfileAdapter;
	@SuppressWarnings("unused")
	@Mock
	private ResponseList<Status> userTimeline;
	@Mock
	private TwitterAdapter twitterAdapter;
	@Captor
	private ArgumentCaptor<Paging> pagingCaptor;
	// no annotation as we don't want mock being injected
	private UserProfile userProfile = mock(UserProfile.class);

	private TwitterException twitterException = new TwitterException("");

	private String screenName = "josdem";

	@Before
	public void setup() throws TwitterException {
		MockitoAnnotations.initMocks(this);

		when(twitter.verifyCredentials()).thenReturn(user);
		when(userProfileAdapter.convertFrom(user)).thenReturn(userProfile);
	}

	@Test
	public void shouldGetUserProfile() throws Exception {
		UserProfile userProfile = mock(UserProfile.class);
		when(twitter.verifyCredentials()).thenReturn(user);
		when(userProfileAdapter.convertFrom(user)).thenReturn(userProfile);

		assertEquals(userProfile, allTwitter.getUserProfile());
	}

	@Test
	public void shouldGetUserProfilewithGivenScreeName() throws Exception {
		when(twitter.showUser(screenName)).thenReturn(user);

		allTwitter.getUserProfile(screenName);

		verify(twitter).showUser(screenName);
		verify(userProfileAdapter).convertFrom(user);
	}

	@Test(expected = AllTwitterException.class)
	public void shouldThrowExcpetionOnGetUserProfilewithGivenScreeName() throws Exception {
		doThrow(twitterException).when(twitter).showUser(screenName);
		allTwitter.getUserProfile(screenName);
	}

	@Test(expected = AllTwitterException.class)
	public void shouldNotGetUserProfile() throws Exception {
		when(twitter.verifyCredentials()).thenThrow(new TwitterException(""));

		allTwitter.getUserProfile();
	}

	private void setTimeLineExpectations() {
		List<Status> statusList = new ArrayList<Status>();
		statusList.add(status);
		when(status.getUser()).thenReturn(user);
		when(responseList.iterator()).thenReturn(statusList.iterator());
	}

	@Test
	public void shouldGetFriendsTimeline() throws Exception {
		setTimeLineExpectations();
		when(twitter.getHomeTimeline()).thenReturn(responseList);

		List<TwitterStatus> twitterStatusList = allTwitter.getFriendsTimeline();
		assertEquals(1, twitterStatusList.size());
		twitterAdapter.convertFrom(status, TwitterStatusType.FRIENDS);
	}

	@Test(expected = AllTwitterException.class)
	public void shouldNotifyErrorOnGetFriendsTimeline() throws Exception {
		doThrow(twitterException).when(twitter).getHomeTimeline();
		allTwitter.getFriendsTimeline();
	}

	@Test
	public void shouldGetFriendsTimelineWithPageAndCount() throws Exception {
		setTimeLineExpectations();
		when(twitter.getHomeTimeline(isA(Paging.class))).thenReturn(responseList);

		List<TwitterStatus> twitterStatusList = allTwitter.getFriendsTimeline(PAGE, COUNT);
		assertEquals(1, twitterStatusList.size());
		twitterAdapter.convertFrom(status, TwitterStatusType.FRIENDS);
		verify(twitter).getHomeTimeline(pagingCaptor.capture());
		assertEquals(PAGE, pagingCaptor.getValue().getPage());
		assertEquals(COUNT, pagingCaptor.getValue().getCount());
	}

	@Test(expected = AllTwitterException.class)
	public void shouldNotifyErrorOnGetFriendsTimelineWithPageAndCount() throws Exception {
		doThrow(twitterException).when(twitter).getHomeTimeline(isA(Paging.class));
		allTwitter.getFriendsTimeline(PAGE, COUNT);
	}

	@Test
	public void shouldGetFriendsTimelineWithMention() throws Exception {
		String twitterScreenName = "TwitterScreenName";
		setTimeLineExpectations();
		when(twitter.getHomeTimeline()).thenReturn(responseList);
		when(twitter.verifyCredentials()).thenReturn(user);
		when(userProfileAdapter.convertFrom(user)).thenReturn(userProfile);
		when(userProfile.getScreenName()).thenReturn(twitterScreenName);
		when(user.getScreenName()).thenReturn(twitterScreenName);

		List<TwitterStatus> twitterStatusList = allTwitter.getFriendsTimeline();
		assertEquals(1, twitterStatusList.size());
		twitterAdapter.convertFrom(status, TwitterStatusType.MENTIONS);
	}

	@Test
	public void shouldGetMentions() throws Exception {
		setTimeLineExpectations();
		when(twitter.getMentions()).thenReturn(responseList);

		List<TwitterStatus> twitterStatusList = allTwitter.getMentions();
		assertEquals(1, twitterStatusList.size());
		twitterAdapter.convertFrom(status, TwitterStatusType.MENTIONS);
	}

	@Test(expected = AllTwitterException.class)
	public void shouldThrowExceptionInGetMentions() throws Exception {
		doThrow(twitterException).when(twitter).getMentions();
		allTwitter.getMentions();
	}

	@Test
	public void shouldGetDirectMessages() throws Exception {
		List<DirectMessage> statusList = new ArrayList<DirectMessage>();
		statusList.add(directMessage);
		when(directMessage.getSender()).thenReturn(user);
		when(directMessagesList.iterator()).thenReturn(statusList.iterator());
		when(twitter.getDirectMessages()).thenReturn(directMessagesList);

		List<TwitterStatus> twitterStatusList = allTwitter.getDirectMessages();
		assertEquals(1, twitterStatusList.size());
		twitterAdapter.convertFrom(status, TwitterStatusType.DIRECT);
	}

	@Test(expected = AllTwitterException.class)
	public void shouldThrowExceptionOnDirectMessages() throws Exception {
		doThrow(twitterException).when(twitter).getDirectMessages();
		allTwitter.getDirectMessages();
	}

	@Test
	public void shouldGetUserTimeLineWithGivenScreenname() throws Exception {
		String screenName = "screenName";
		setTimeLineExpectations();
		when(twitter.getUserTimeline(screenName)).thenReturn(responseList);

		List<TwitterStatus> twitterStatusList = allTwitter.getUserTimeline(screenName);
		assertEquals(1, twitterStatusList.size());
	}

	@Test(expected = AllTwitterException.class)
	public void shouldThrowExceptionOnGetFriendsTimeline() throws Exception {
		doThrow(twitterException).when(twitter).getUserTimeline(screenName);
		allTwitter.getUserTimeline(screenName);
	}

	@Test
	public void shouldUpdateStatus() throws Exception {
		String statusString = "status";
		when(twitter.updateStatus(statusString)).thenReturn(status);

		allTwitter.updateStatus(statusString);

		verify(twitter).updateStatus(statusString);
		verify(twitterAdapter).convertFrom(status, TwitterStatusType.FRIENDS);
	}

	@Test(expected = AllTwitterException.class)
	public void shouldThrowExceptionIfServiceUnavailable() throws Exception {
		when(twitter.updateStatus(anyString())).thenThrow(new TwitterException("Service may be down"));

		allTwitter.updateStatus("meh");
	}

	@Test
	public void shouldLoginUsingXAuth() throws Exception {
		String username = "username";
		String password = "password";

		when(xAuthTwitterFactory.create()).thenReturn(twitter);

		allTwitter.login(username, password);

		verify(twitter).getOAuthAccessToken(username, password);
	}

	@Test(expected = AllTwitterException.class)
	public void shouldThrowExcpetionIfLoginFails() throws Exception {
		allTwitter.login(null, null);
	}

	@Test
	public void shouldFollowUser() throws Exception {
		when(user.getScreenName()).thenReturn(screenName);
		when(twitter.createFriendship(screenName)).thenReturn(user);
		assertTrue(allTwitter.followUser(screenName));
		verify(twitter).createFriendship(screenName);
	}

	@Test(expected = AllTwitterException.class)
	public void shouldExceptionOnFollowUser() throws Exception {
		when(user.getScreenName()).thenReturn(screenName);
		when(twitter.createFriendship(screenName)).thenThrow(new TwitterException(""));
		allTwitter.followUser(screenName);
	}

	@Test
	public void shouldNotFollowUser() throws Exception {
		when(user.getScreenName()).thenReturn("something");
		when(twitter.createFriendship(screenName)).thenReturn(user);
		assertFalse(allTwitter.followUser(screenName));
		verify(twitter).createFriendship(screenName);
	}

	@Test
	public void shouldUnFollowUser() throws Exception {
		when(user.getScreenName()).thenReturn(screenName);
		when(twitter.destroyFriendship(screenName)).thenReturn(user);
		assertTrue(allTwitter.unfollowUser(screenName));
		verify(twitter).destroyFriendship(screenName);
	}

	@Test(expected = AllTwitterException.class)
	public void shouldExceptionOnUnFollowUser() throws Exception {
		when(user.getScreenName()).thenReturn(screenName);
		when(twitter.destroyFriendship(screenName)).thenThrow(new TwitterException(""));
		allTwitter.unfollowUser(screenName);
	}

	@Test
	public void shouldNotUnFollowUser() throws Exception {
		when(user.getScreenName()).thenReturn("something");
		when(twitter.destroyFriendship(screenName)).thenReturn(user);
		assertFalse(allTwitter.unfollowUser(screenName));
		verify(twitter).destroyFriendship(screenName);
	}

	@Test
	public void shouldKnowIfLogged() throws Exception {
		when(authorization.isEnabled()).thenReturn(false, true);
		setValueToPrivateField(allTwitter, "twitter", new TwitterFactory().getInstance(authorization));

		assertFalse(allTwitter.isLoggedIn());
		assertTrue(allTwitter.isLoggedIn());
	}

	@Test
	public void shouldEqualsFollowingToUserIfSameUser() throws Exception {
		when(twitter.getScreenName()).thenReturn(screenName);
		when(twitter.existsFriendship(screenName, screenName)).thenReturn(true);
		assertTrue(allTwitter.isFollowingToUser(screenName));
	}

	@Test
	public void shouldFollowingToUserOk() throws Exception {
		String thaNickName = "josdem";
		when(twitter.getScreenName()).thenReturn(thaNickName);
		when(twitter.existsFriendship(screenName, thaNickName)).thenReturn(true);
		assertTrue(allTwitter.isFollowingToUser(screenName));
	}

	@Test
	public void shouldFollowingToUserNotOk() throws Exception {
		String someScreenName = "felipeCalderon";
		when(twitter.getScreenName()).thenReturn(someScreenName);
		when(twitter.existsFriendship(screenName, someScreenName)).thenReturn(false);
		assertFalse(allTwitter.isFollowingToUser(screenName));
	}

	@Test(expected = AllTwitterException.class)
	public void shouldNotFollowingToUserBecauseIllegalStateException() throws Exception {
		when(twitter.getScreenName()).thenThrow(new IllegalStateException());
		allTwitter.isFollowingToUser(screenName);
	}

	@Test(expected = AllTwitterException.class)
	public void shouldNotFollowingToUserBecauseTwitterException() throws Exception {
		when(twitter.getScreenName()).thenThrow(new TwitterException(""));
		allTwitter.isFollowingToUser(screenName);
	}

	@Test
	public void shouldCreateXAuthTwitter() throws Exception {
		XAuthTwitterFactory xAuthTwitterFactory = new XAuthTwitterFactory();
		Twitter createdTwitterInstance = xAuthTwitterFactory.create();
		assertNotNull(createdTwitterInstance);
	}

	public void setValueToPrivateField(Object object, String fieldName, Object value) throws Exception {
		Field privateField = object.getClass().getDeclaredField(fieldName);
		privateField.setAccessible(true);
		privateField.set(object, value);
	}

}
