package com.sky.web.resource;

import org.springframework.hateoas.ResourceSupport;

public class MessageResource extends ResourceSupport {
	private String message = "";
	private String type = "";

	public MessageResource() {
	}

	public MessageResource(String type, String message) {
		this.message = message;
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
