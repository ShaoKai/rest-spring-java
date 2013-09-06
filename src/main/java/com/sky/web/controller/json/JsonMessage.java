package com.sky.web.controller.json;

public class JsonMessage {
	private String message = "";
	private String type = "";

	public JsonMessage() {
	}

	public JsonMessage(String type, String message) {
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
