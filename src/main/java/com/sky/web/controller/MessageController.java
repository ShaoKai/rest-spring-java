package com.sky.web.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sky.web.AppConfig;
import com.sky.web.resource.MessageResource;
import com.sky.web.resource.MessageResourceAssembler;
import com.sky.web.service.Message;
import com.sky.web.service.MessageService;

@Controller
public class MessageController {
	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

	private @Autowired MessageResourceAssembler messageResourceAssembler;
	private @Autowired MessageService 			messageService;

	@ResponseBody
	@RequestMapping(value = AppConfig.REST_BASE_URL+"/messages", method = RequestMethod.GET)
	public MessageResource message(@RequestParam(required = false) long messageId) throws Exception {
		return messageResourceAssembler.toResource(messageService.getMessage(messageId));
	}

	@ResponseBody
	@RequestMapping(value = AppConfig.REST_BASE_URL+ "/messages", method = RequestMethod.POST)
	public ResponseEntity<Void> addMessage(@RequestParam long userId, @RequestParam String message) throws Exception {
		Message msg = new Message();
		msg.setUserId(userId);
		msg.setMessage(message);
		msg.setMessageId(101);
		messageService.addMessage(msg);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(linkTo(methodOn(MessageController.class).message(msg.getMessageId())).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
}
