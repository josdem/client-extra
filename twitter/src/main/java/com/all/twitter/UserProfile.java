package com.all.twitter;

import java.net.URL;

public interface UserProfile {
	String getName();

	String getScreenName();

	String getDescription();

	String getLocation();

	String getURL();

	int getFollowersCount();

	int getFriendsCount();

	int getTweetsCount();

	long getId();

	URL getImageProfileUrl();

	boolean isFollowing();
}
