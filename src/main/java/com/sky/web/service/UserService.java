package com.sky.web.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class UserService {
	private Map<String, User> userData;

	private Map<String, User> getUserData() {
		if (userData == null) {
			HashMap<String, User> users = new HashMap<String, User>();
			User admin = new User("Eirc", "secretkey1", "accesskey1");
			User user = new User("Elaine", "secretkey2", "accesskey2");
			users.put(admin.getAccount(), admin);
			users.put(user.getAccount(), user);
			userData = Collections.unmodifiableMap(users);
		}
		return userData;
	}

	public String getUserAccessKey(String String) {
		return getUserData().get(String).getAccessKey();
	}

	public String getSecretKey(String accessKey) {
		for (User user : getUserData().values()) {
			if (accessKey.equals(user.getAccessKey())) {
				return user.getSecretKey();
			}
		}
		return null;
	}
}
