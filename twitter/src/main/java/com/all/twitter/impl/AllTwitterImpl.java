package com.all.twitter.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import com.all.twitter.AllTwitter;
import com.all.twitter.AllTwitterException;
import com.all.twitter.TwitterStatus;
import com.all.twitter.TwitterStatus.TwitterStatusType;
import com.all.twitter.UserProfile;

@Service
public class AllTwitterImpl implements AllTwitter {
	// private final Log log = LogFactory.getLog(this.getClass());
	private final TwitterAdapter twitterAdapter = new TwitterAdapter();
	private final UserProfileAdapter userProfileAdapter = new UserProfileAdapter();
	private XAuthTwitterFactory xAuthTwitterFactory = new XAuthTwitterFactory();
	private Twitter twitter = new TwitterFactory().getInstance();
	private UserProfile userProfile;

	@Override
	public final void login(String username, String password) throws AllTwitterException {
		try {
			twitter = xAuthTwitterFactory.create();
			twitter.getOAuthAccessToken(username, password);// this does the xAuth flow
		} catch (Exception e) {
			e.printStackTrace();
			throw new AllTwitterException("An error ocurred while login.", e);
		}
	}

	private List<TwitterStatus> iterateResponseList(ResponseList<Status> responseList, TwitterStatusType type)
			throws AllTwitterException {
		List<TwitterStatus> statusList = new ArrayList<TwitterStatus>(responseList.size());
		for (Status status : responseList) {

			TwitterStatus twitterStatus = twitterAdapter.convertFrom(status, type);

			statusList.add(twitterStatus);
		}
		return statusList;
	}

	private List<TwitterStatus> iterateDirectMessages(ResponseList<DirectMessage> responseList) {
		List<TwitterStatus> statusList = new ArrayList<TwitterStatus>(responseList.size());
		for (DirectMessage directMessage : responseList) {
			statusList.add(twitterAdapter.convertFrom(directMessage));
		}
		return statusList;
	}

	@Override
	public final List<TwitterStatus> getFriendsTimeline() throws AllTwitterException {
		try {

			ResponseList<Status> responseList = twitter.getHomeTimeline();
			List<TwitterStatus> statusList = iterateResponseList(responseList, TwitterStatusType.FRIENDS);
			return statusList;

		} catch (Exception e) {
			throw new AllTwitterException(e.getMessage(), e);
		}
	}

	@Override
	public List<TwitterStatus> getFriendsTimeline(int page, int count) throws AllTwitterException {
		try {

			ResponseList<Status> responseList = twitter.getHomeTimeline(new Paging(page, count));
			List<TwitterStatus> statusList = iterateResponseList(responseList, TwitterStatusType.FRIENDS);
			return statusList;

		} catch (Exception e) {
			throw new AllTwitterException(e.getMessage(), e);
		}
	}

	@Override
	public final List<TwitterStatus> getMentions() throws AllTwitterException {
		try {

			ResponseList<Status> responseList = twitter.getMentions();
			List<TwitterStatus> statusList = iterateResponseList(responseList, TwitterStatusType.MENTIONS);
			return statusList;

		} catch (Exception e) {
			throw new AllTwitterException(e.getMessage(), e);
		}
	}

	@Override
	public final List<TwitterStatus> getDirectMessages() throws AllTwitterException {
		try {

			ResponseList<DirectMessage> responseList = twitter.getDirectMessages();
			List<TwitterStatus> statusList = iterateDirectMessages(responseList);
			return statusList;

		} catch (Exception e) {
			throw new AllTwitterException(e.getMessage(), e);
		}
	}

	@Override
	public List<TwitterStatus> getUserTimeline(final String screeName) throws AllTwitterException {
		try {

			ResponseList<Status> userTimeline = twitter.getUserTimeline(screeName);
			List<TwitterStatus> statusList = iterateResponseList(userTimeline, TwitterStatusType.FRIENDS);
			return statusList;

		} catch (Exception e) {
			throw new AllTwitterException(e.getMessage(), e);
		}
	}

	@Override
	public final TwitterStatus updateStatus(final String status) throws AllTwitterException {
		try {

			Status updatedStatus = twitter.updateStatus(status);
			return twitterAdapter.convertFrom(updatedStatus, TwitterStatusType.FRIENDS);

		} catch (Exception e) {
			throw new AllTwitterException(e.getMessage(), e);
		}
	}

	@Override
	public final boolean isLoggedIn() {
		return twitter.getAuthorization().isEnabled();
	}

	@Override
	public UserProfile getUserProfile() throws AllTwitterException {
		try {

			if (userProfile == null) {
				User user = twitter.verifyCredentials();
				userProfile = userProfileAdapter.convertFrom(user);
			}
			return userProfile;

		} catch (Exception e) {
			throw new AllTwitterException(e.getMessage(), e);
		}
	}

	@Override
	public UserProfile getUserProfile(final String screenName) throws AllTwitterException {
		try {

			User user = twitter.showUser(screenName);
			return userProfileAdapter.convertFrom(user);

		} catch (Exception e) {
			throw new AllTwitterException(e.getMessage(), e);
		}
	}

	@Override
	public boolean followUser(String screenName) throws AllTwitterException {
		try {
			User user = twitter.createFriendship(screenName);
			return user.getScreenName().equals(screenName);
		} catch (TwitterException twe) {
			throw new AllTwitterException("Can not create a friendship");
		}
	}

	@Override
	public boolean isFollowingToUser(String userScreenName) throws AllTwitterException {
		try {
			return twitter.getScreenName() != userScreenName ? twitter.existsFriendship(twitter.getScreenName(),
					userScreenName) : true;
		} catch (IllegalStateException ile) {
			throw new AllTwitterException("Can not get logged in user screen name", ile);
		} catch (TwitterException twe) {
			throw new AllTwitterException("Can not create a friendship", twe);
		}
	}

	@Override
	public boolean unfollowUser(String screenName) throws AllTwitterException {
		try {
			User user = twitter.destroyFriendship(screenName);
			return user.getScreenName().equals(screenName);
		} catch (TwitterException twe) {
			throw new AllTwitterException("Can not destroy a friendship");
		}
	}

}

class XAuthTwitterFactory {
	private static final String ALLDOTCOM_CONSUMER_KEY = "44nit7jBrxJXvZeWYvQ";
	private static final String ALLDOTCOM_CONSUMER_SECRET = "apOEvwADZmpq3nTnZLa2z1QumZZeXwmPvI3xt1UeMc";

	public Twitter create() {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setOAuthConsumerKey(ALLDOTCOM_CONSUMER_KEY);
		configurationBuilder.setOAuthConsumerSecret(ALLDOTCOM_CONSUMER_SECRET);

		Configuration configuration = configurationBuilder.build();

		return new TwitterFactory(configuration).getInstance();
	}

}
