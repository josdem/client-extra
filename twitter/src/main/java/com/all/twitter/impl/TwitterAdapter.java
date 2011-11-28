package com.all.twitter.impl;

import java.net.URL;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.User;

import com.all.twitter.TwitterStatus;
import com.all.twitter.TwitterStatus.TwitterStatusType;
import com.all.twitter.UserProfile;

public class TwitterAdapter {
	private final static String directMessageSource = "Direct Message";

	public TwitterStatus convertFrom(Status status, TwitterStatusType type) {
		TwitterStatusImpl twitterStatus = new TwitterStatusImpl(status.getId());
		twitterStatus.setType(type);
		fillCommonUserTwitterStatusValues(status.getUser(), twitterStatus);
		fillCommonTwitterStatusValues(status, twitterStatus);
		twitterStatus.setSource(status.getSource());
		twitterStatus.setRetweeted(status.isRetweet());
		String inReplyToScreenName = status.getInReplyToScreenName();
		twitterStatus.setReplied(inReplyToScreenName != null);
		twitterStatus.setInReplyToScreenName(inReplyToScreenName);
		UserProfile userProfile = convertFromStatus(status);
		twitterStatus.setUserProfile(userProfile);
		Status retweetedStatus = status.getRetweetedStatus();
		if (retweetedStatus != null) {
			twitterStatus.setRetweeterScreenName(retweetedStatus.getUser().getScreenName());
			twitterStatus.setRetweeterProfileImageUrl(retweetedStatus.getUser().getProfileImageURL());
			twitterStatus.setText(retweetedStatus.getText());
		}
		return twitterStatus;
	}

	private UserProfile convertFromStatus(Status status) {
		UserProfile userProfile = convertFrom(status.getUser());
		return userProfile;
	}
	
	 UserProfile convertFrom(User user) {
		URL url = user.getURL();
		UserProfileImpl userProfileImpl = new UserProfileImpl(user.getName(), user.getScreenName(), user
				.getDescription(), user.getLocation(),  url != null ? url.toString() : "", user.getFollowersCount(), user
				.getFriendsCount(), user.getStatusesCount(), user.getId(), user.getProfileImageURL(), false);
		return userProfileImpl;
	}

	public TwitterStatus convertFromContact(User friendUser, Status status) {
		TwitterStatusImpl twitterStatus = new TwitterStatusImpl(status.getId());
		fillCommonUserTwitterStatusValues(friendUser, twitterStatus);
		fillCommonTwitterStatusValues(status, twitterStatus);
		twitterStatus.setSource(status.getSource());
		return twitterStatus;
	}

	private void fillCommonUserTwitterStatusValues(User user, TwitterStatusImpl twitterStatus) {
		twitterStatus.setProfileImage(user.getProfileImageURL());
		twitterStatus.setScreenName(user.getScreenName());
		twitterStatus.setSenderId(user.getId());
	}

	private void fillCommonTwitterStatusValues(Status status, TwitterStatusImpl twitterStatus) {
		twitterStatus.setCreatedAt(status.getCreatedAt());
		twitterStatus.setText(status.getText());
	}

	public TwitterStatus convertFrom(DirectMessage directMessage) {
		TwitterStatusImpl twitterStatus = new TwitterStatusImpl(directMessage.getId());
		twitterStatus.setType(TwitterStatusType.DIRECT);
		twitterStatus.setProfileImage(directMessage.getSender().getProfileImageURL());
		twitterStatus.setScreenName(directMessage.getSender().getScreenName());
		twitterStatus.setCreatedAt(directMessage.getCreatedAt());
		twitterStatus.setText(directMessage.getText());
		twitterStatus.setSource(directMessageSource);
		twitterStatus.setSenderId(directMessage.getSenderId());
		return twitterStatus;
	}

}
