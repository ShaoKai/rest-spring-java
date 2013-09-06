package com.sky.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sky.web.controller.json.JsonMessage;

@Controller
public class RestController {
	private static final Logger logger = LoggerFactory.getLogger(RestController.class);

	@ResponseBody
	@RequestMapping(value = "/rest/v1.0/message", method = RequestMethod.GET)
	public JsonMessage message() throws Exception {
		return new JsonMessage("message", "You've got message.");
	}

}
