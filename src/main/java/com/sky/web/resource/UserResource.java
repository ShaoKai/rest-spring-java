package com.sky.web.resource;

import org.springframework.hateoas.ResourceSupport;

public class UserResource extends ResourceSupport {
	private long userId;
	private String userName;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
