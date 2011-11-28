package com.all.twitter.impl;

import java.net.URL;
import java.util.Date;

import com.all.twitter.TwitterStatus;
import com.all.twitter.UserProfile;

public class TwitterStatusImpl implements TwitterStatus {
	private long id;
	private Date createdAt;
	private URL profileImage;
	private String screenName;
	private String text;
	private String source;
	private long senderId;
	private TwitterStatusType type;
	private boolean retweeted;
	private boolean replied;
	private String inReplyToScreenName;
	private String retweeterScreenName;
	private URL retweeterProfileImage;
	private UserProfile userProfile;

	public TwitterStatusImpl(long id) {
		this.id = id;
	}

	public void setType(TwitterStatusType type) {
		this.type = type;
	}

	public final void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public final void setProfileImage(URL profileImage) {
		this.profileImage = profileImage;
	}

	public final void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public final void setText(String text) {
		this.text = text;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setSenderId(long id) {
		this.senderId = id;

	}

	@Override
	public final Date getDateCreatedAt() {
		return createdAt;
	}

	@Override
	public final URL getProfileImageUrl() {
		return profileImage;
	}

	@Override
	public final String getScreenName() {
		return screenName;
	}

	@Override
	public final String getText() {
		return text;
	}

	@Override
	public String getSource() {
		return source;
	}

	@Override
	public long getSenderId() {
		return senderId;
	}

	@Override
	public TwitterStatusType getType() {
		return type;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		TwitterStatusImpl other = (TwitterStatusImpl) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isRetweeted() {
		return retweeted;
	}

	@Override
	public boolean isReplied() {
		return replied;
	}

	@Override
	public boolean isDirect() {
		return type.equals(TwitterStatusType.DIRECT);
	}

	public void setReplied(boolean replied) {
		this.replied = replied;
	}

	public void setRetweeted(boolean retweeted) {
		this.retweeted = retweeted;
	}

	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	@Override
	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}

	public void setRetweeterScreenName(String retweeterScreenName) {
		this.retweeterScreenName = retweeterScreenName;
	}

	@Override
	public String getRetweeterScreenName() {
		return retweeterScreenName;
	}

	public void setRetweeterProfileImageUrl(URL profileImageURL) {
		this.retweeterProfileImage = profileImageURL;
	}

	@Override
	public URL getRetweeterProfileImageUrl() {
		return retweeterProfileImage;
	}

	@Override
	public UserProfile getUserProfile() {
		return userProfile;
	}

}
