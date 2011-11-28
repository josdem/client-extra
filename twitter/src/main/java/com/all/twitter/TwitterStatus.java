package com.all.twitter;

import java.net.URL;
import java.util.Date;

/**
 * Understands data about twitter time line
 */

public interface TwitterStatus {
	enum TwitterStatusType {
		FRIENDS, MENTIONS, DIRECT
	}

	long getId();

	URL getProfileImageUrl();

	Date getDateCreatedAt();

	String getScreenName();

	String getText();

	String getSource();

	long getSenderId();

	TwitterStatusType getType();

	boolean isRetweeted();

	boolean isReplied();

	boolean isDirect();

	String getRetweeterScreenName();

	URL getRetweeterProfileImageUrl();

	String getInReplyToScreenName();

	UserProfile getUserProfile();
}
