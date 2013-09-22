package com.sky.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sky.web.AppConfig;
import com.sky.web.resource.MessageResource;
import com.sky.web.resource.MessageResourceAssembler;
import com.sky.web.resource.UserResource;
import com.sky.web.resource.UserResourceAssembler;
import com.sky.web.service.MessageService;
import com.sky.web.service.UserService;

@Controller

public class UserController {
	
	private @Autowired MessageResourceAssembler messageResourceAssembler;
	private @Autowired MessageService 			messageService;
	private @Autowired UserResourceAssembler 	userResourceAssembler;
	private @Autowired UserService 				userService;
	
	@ResponseBody
	@RequestMapping(value = AppConfig.REST_BASE_URL + "/users/{userId}", method = RequestMethod.GET)
	public UserResource users(@PathVariable long userId) throws Exception {
		return userResourceAssembler.toResource(userService.getUser(userId));
	}

	@ResponseBody
	@RequestMapping(value = AppConfig.REST_BASE_URL + "/users/{userId}/messages", method = RequestMethod.GET)
	public List<MessageResource> messages(@PathVariable long userId) throws Exception {
		return messageResourceAssembler.toResources(messageService.getUsersMessages(userId));
	}


}
