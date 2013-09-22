package com.sky.web;

import org.springframework.context.annotation.Configuration;

@Configuration
public interface AppConfig {
	public static final String SIGNATURE_HEADER_NAME = "X-Signature-Hmac-Sha-256";
	public static final String ACCESS_TOKEN_HEADER_NAME = "X-Access-Token";
	public static final String REST_BASE_URL = "/rest/v1.0";
}
