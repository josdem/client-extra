package com.all.twitter.impl;

import java.net.URL;

import com.all.twitter.UserProfile;

public class UserProfileImpl implements UserProfile {
	private String name;
	private final String screenName;
	private final String description;
	private final String location;
	private final String url;
	private final int followers;
	private final int friends;
	private final int tweetCount;
	private final long id;
	private final URL imageProfile;
	private final boolean following;


	public UserProfileImpl(String name, String screenName, String description, String location, String url, int followers, int friends, int tweetCount, long id, URL imageProfile, boolean following) {
		this.name = name;
		this.screenName = screenName;
		this.description = description;
		this.location = location;
		this.url = url;
		this.followers = followers;
		this.friends = friends;
		this.tweetCount = tweetCount;
		this.id = id;
		this.imageProfile = imageProfile;
		this.following = following;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getScreenName() {
		return screenName;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getLocation() {
		return location;
	}

	@Override
	public String getURL() {
		return url;
	}

	@Override
	public int getFollowersCount() {
		return followers;
	}

	@Override
	public int getFriendsCount() {
		return friends;
	}

	@Override
	public int getTweetsCount() {
		return tweetCount;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public URL getImageProfileUrl() {
		return imageProfile;
	}

	@Override
	public boolean isFollowing() {
		return following;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UserProfileImpl other = (UserProfileImpl) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

}
