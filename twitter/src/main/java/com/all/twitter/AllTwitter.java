package com.all.twitter;

import java.util.List;

public interface AllTwitter {

	boolean isLoggedIn();

	void login(String username, String password) throws AllTwitterException;

	TwitterStatus updateStatus(String status) throws AllTwitterException;

	List<TwitterStatus> getFriendsTimeline() throws AllTwitterException;

	List<TwitterStatus> getFriendsTimeline(int page, int count) throws AllTwitterException;

	List<TwitterStatus> getMentions() throws AllTwitterException;
	
	List<TwitterStatus> getDirectMessages() throws AllTwitterException;
	
	List<TwitterStatus> getUserTimeline(String screenName) throws AllTwitterException;
	
	UserProfile getUserProfile() throws AllTwitterException;

	UserProfile getUserProfile(String screenName) throws AllTwitterException;

	boolean followUser(String screenName) throws AllTwitterException;

	boolean unfollowUser(String screenName) throws AllTwitterException;

	boolean isFollowingToUser(String userScreenName) throws AllTwitterException;

}
