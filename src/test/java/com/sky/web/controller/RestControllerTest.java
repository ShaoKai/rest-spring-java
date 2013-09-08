package com.sky.web.controller;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.sky.web.WebConfig;
import com.sky.web.tools.RestUtils;

public class RestControllerTest {
	private static Logger logger = LoggerFactory.getLogger(RestControllerTest.class);
	private static Server server;
	private final static String BASE_URL = "http://localhost:8080";
	private final static String RESOURCE_BASE = "src/main/webapp";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		server = new Server(8080);

		WebAppContext context = new WebAppContext();
		context.setDescriptor(RESOURCE_BASE + "/WEB-INF/web.xml");
		context.setResourceBase(RESOURCE_BASE);
		context.setContextPath("/");
		context.setParentLoaderPriority(true);

		server.setHandler(context);

		server.start();
		logger.info("Startup the jetty server");
	}

	// SC_BAD_REQUEST
	@Test(expected = HttpClientErrorException.class)
	public void testBadRequest() throws Exception {
		RestTemplate template = new RestTemplate();
		HttpEntity<String> response = template.exchange(BASE_URL + "/rest/v1.0/message", HttpMethod.GET, null, String.class);
	}

	// SC_UNAUTHORIZED
	@Test(expected = HttpClientErrorException.class)
	public void testUnauthorized() throws Exception {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set(WebConfig.SIGNATURE_HEADER_NAME, "123");
		requestHeaders.set(WebConfig.ACCESS_TOKEN_HEADER_NAME, "123");

		MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<String, String>();
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(postParameters, requestHeaders);

		RestTemplate template = new RestTemplate();
		HttpEntity<String> response = template.exchange(BASE_URL + "/rest/v1.0/message", HttpMethod.GET, requestEntity, String.class);
	}

	@Test
	public void testMessageGet() throws Exception {

		String accessToken = "accesskey1";
		String secretKey = "secretkey1";
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set(WebConfig.SIGNATURE_HEADER_NAME, RestUtils.generateHmacSHA256Signature("", secretKey));
		requestHeaders.set(WebConfig.ACCESS_TOKEN_HEADER_NAME, accessToken);

		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(parameters, requestHeaders);

		RestTemplate template = new RestTemplate();
		HttpEntity<String> response = template.exchange(BASE_URL + "/rest/v1.0/message", HttpMethod.GET, requestEntity, String.class);
	}

	@Test
	public void testMessagePost() throws Exception {
		String accessToken = "accesskey1";
		String secretKey = "secretkey1";
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set(WebConfig.SIGNATURE_HEADER_NAME, RestUtils.generateHmacSHA256Signature("account=Eirc", secretKey));
		requestHeaders.set(WebConfig.ACCESS_TOKEN_HEADER_NAME, accessToken);

		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("account", "Eirc");
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(parameters, requestHeaders);

		RestTemplate template = new RestTemplate();
		HttpEntity<String> response = template.exchange(BASE_URL + "/rest/v1.0/message", HttpMethod.POST, requestEntity, String.class);
		String etag = response.getHeaders().getETag();
		logger.info("=====================================");
		logger.info("Etag     : {}", etag);
		logger.info("Response : {}", response.getBody());

	}

	@Test
	public void testEtag() throws Exception {
		String accessToken = "accesskey1";
		String secretKey = "secretkey1";
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set(WebConfig.SIGNATURE_HEADER_NAME, RestUtils.generateHmacSHA256Signature("", secretKey));
		requestHeaders.set(WebConfig.ACCESS_TOKEN_HEADER_NAME, accessToken);

		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(parameters, requestHeaders);

		RestTemplate template = new RestTemplate();
		HttpEntity<String> response = template.exchange(BASE_URL + "/rest/v1.0/message", HttpMethod.GET, requestEntity, String.class);
		String etag = response.getHeaders().getETag();
		logger.info("=====================================");
		logger.info("Etag     : {}", etag);
		logger.info("Response : {}", response.getBody());

		// send request with IfNoneMatch header

		requestHeaders = new HttpHeaders();
		requestHeaders.setIfNoneMatch(etag);
		requestHeaders.set(WebConfig.SIGNATURE_HEADER_NAME, RestUtils.generateHmacSHA256Signature("", secretKey));
		requestHeaders.set(WebConfig.ACCESS_TOKEN_HEADER_NAME, accessToken);

		parameters = new LinkedMultiValueMap<String, String>();
		requestEntity = new HttpEntity<MultiValueMap<String, String>>(parameters, requestHeaders);
		response = template.exchange(BASE_URL + "/rest/v1.0/message", HttpMethod.GET, requestEntity, String.class);
		etag = response.getHeaders().getETag();
		logger.info("=====================================");
		logger.info("Etag     : {}", etag);
		logger.info("Response : {}", response.getBody());

		assertTrue(response.getBody() == null);

	}
}
