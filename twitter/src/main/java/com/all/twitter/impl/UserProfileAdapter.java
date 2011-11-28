package com.all.twitter.impl;

import java.net.URL;

import twitter4j.User;

import com.all.twitter.UserProfile;

public class UserProfileAdapter {

	public UserProfile convertFrom(User user) {
		URL url = user.getURL();
		UserProfileImpl userProfileImpl = new UserProfileImpl(user.getName(), user.getScreenName(), user
				.getDescription(), user.getLocation(), url != null ? url.toString() : "", user.getFollowersCount(), user
				.getFriendsCount(), user.getStatusesCount(), user.getId(), user.getProfileImageURL(), false);
		return userProfileImpl;
	}
	
}
