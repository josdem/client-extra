package com.all.facebook.impl;

import com.all.facebook.FacebookUser;

public class FacebookUserImpl implements FacebookUser {

	private final String id;
	
	private final String name;

	public FacebookUserImpl(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
}
