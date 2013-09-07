package com.sky.web.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sky.web.filter.MultiReadHttpServletRequest;
import com.sky.web.service.UserService;
import com.sky.web.tools.RestUtils;

public class RestAuthInterceptor implements HandlerInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(RestAuthInterceptor.class);
	private String signatureHeaderName;
	private String accessKeyHeaderName;
	@Autowired
	private UserService service;

	public RestAuthInterceptor(String signatureHeaderName, String accessKeyHeaderName) {
		super();
		this.signatureHeaderName = signatureHeaderName;
		this.accessKeyHeaderName = accessKeyHeaderName;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		MultiReadHttpServletRequest httpRequest = new MultiReadHttpServletRequest(request);

		String signature = StringUtils.defaultString(request.getHeader(signatureHeaderName), "");
		String accessKey = StringUtils.defaultString(request.getHeader(accessKeyHeaderName), "");
		if (signature.length() == 0) {
			response.setStatus(response.SC_BAD_REQUEST);
			response.flushBuffer();
			return false;
		}

		String requestBody = httpRequest.getRequestBody();
		String secretKey = service.getSecretKey(accessKey);
		if (StringUtils.isBlank(secretKey)) {
			response.setStatus(response.SC_BAD_REQUEST);
			response.flushBuffer();
			return false;
		}

		String verification = RestUtils.generateHmacSHA256Signature(requestBody, secretKey);
		logger.info("verification : {}", verification);
		logger.info("signature    : {}", signature);

		if (!signature.equals(verification)) {
			response.setStatus(response.SC_UNAUTHORIZED);
			response.flushBuffer();
			return false;
		}

		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}
}
