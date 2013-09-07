package com.sky.web.controller;

import javax.servlet.http.HttpServletResponse;

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
	public JsonMessage get() throws Exception {
		return new JsonMessage("message", "You've got message.");
	}

	@ResponseBody
	@RequestMapping(value = "/rest/v1.0/message", method = RequestMethod.POST)
	public JsonMessage post() throws Exception {
		return new JsonMessage("message", "You've got message.");
	}

	@RequestMapping(value = "/test")
	public void test(HttpServletResponse response) throws Exception {
		response.getWriter().write("123");
		response.getWriter().flush();
		return;
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
