package com.sky.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MessageService {
	public void addMessage(Message message) {
		
	}
	public Message getMessage(long messageId) {
		long userId = messageId / 100;
		Message message = new Message();
		message.setUserId(userId);
		message.setMessage("Test : " + userId);
		message.setMessageId(messageId);
		return message;
	}

	public List<Message> getUsersMessages(long userId) {
		List<Message> messages = new ArrayList<Message>();
		for (int i = 0; i < 3; i++) {
			Message message = new Message();
			message.setUserId(userId);
			message.setMessage("Test : " + userId);
			message.setMessageId((userId * 100) + i);
			messages.add(message);
		}
		return messages;
	}
}
