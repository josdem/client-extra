package com.all.twitter.impl;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.all.twitter.AllTwitterException;
import com.all.twitter.TwitterStatus;
import com.all.twitter.impl.AllTwitterImpl;

public class TwitterTestConnection {
	private static Log log = LogFactory.getLog(TwitterTestConnection.class);

	public static void main(String[] args) {
		AllTwitterImpl allTwitter = new AllTwitterImpl();
		try {

			allTwitter.login("allApplication", "4lld0tc0m");
			
			log.info("UserProfile: " + ToStringBuilder.reflectionToString(allTwitter.getUserProfile()));
			log.info("timeLine: " );
			for (TwitterStatus status : allTwitter.getMentions()) {
				log.info("TwitterStatus: " + ToStringBuilder.reflectionToString(status));
			}
			
		} catch (AllTwitterException e) {
			log.error(e,e);
		}
	}
}
