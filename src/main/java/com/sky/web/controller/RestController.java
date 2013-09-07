package com.sky.web.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sky.web.controller.json.JsonMessage;

@Controller
public class RestController {
	private static final Logger logger = LoggerFactory.getLogger(RestController.class);

	@ResponseBody
	@RequestMapping(value = "/rest/v1.0/message", method = RequestMethod.GET)
	public JsonMessage get(@RequestParam(required = false) String message) throws Exception {
		return new JsonMessage("message", "You've got message." + StringUtils.defaultString(message, ""));
	}

	@ResponseBody
	@RequestMapping(value = "/rest/v1.0/message", method = RequestMethod.POST)
	public JsonMessage post(@RequestParam String account) throws Exception {
		return new JsonMessage("message", account + ", You've sent message.");
	}

	// @ExceptionHandler(HttpClientErrorException.class)
	// @ResponseBody
	// @ResponseStatus(HttpStatus.BAD_REQUEST)
	// public JsonException handleException(RuntimeException ex,
	// HttpServletRequest request, HttpServletResponse response) {
	// JsonException exception = new JsonException();
	// exception.setErrorMessage(ex.getMessage());
	// return exception;
	// }
}
