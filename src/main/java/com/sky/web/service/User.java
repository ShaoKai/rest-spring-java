package com.sky.web.service;

public class User {
	private long userId;
	private String account;
	private String secretKey;
	private String accessKey;

	public User() {
		super();
	}

	public User(long userId, String account, String secretKey, String accessKey) {
		super();
		this.userId = userId;
		this.account = account;
		this.secretKey = secretKey;
		this.accessKey = accessKey;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

}
