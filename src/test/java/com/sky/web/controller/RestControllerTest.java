package com.sky.web.controller;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

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

import com.sky.web.AppConfig;
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

	public static void main(String[] args) throws Exception {
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
		requestHeaders.set(AppConfig.SIGNATURE_HEADER_NAME, "123");
		requestHeaders.set(AppConfig.ACCESS_TOKEN_HEADER_NAME, "123");

		MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<String, String>();
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(postParameters, requestHeaders);

		RestTemplate template = new RestTemplate();
		HttpEntity<String> response = template.exchange(BASE_URL + "/rest/v1.0/message", HttpMethod.GET, requestEntity, String.class);
	}

	@Test
	public void testUserGet() throws Exception {

		String accessToken = "accesskey1";
		String secretKey = "secretkey1";
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set(AppConfig.SIGNATURE_HEADER_NAME, RestUtils.generateHmacSHA256Signature("", secretKey));
		requestHeaders.set(AppConfig.ACCESS_TOKEN_HEADER_NAME, accessToken);

		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(parameters, requestHeaders);

		RestTemplate template = new RestTemplate();
		HttpEntity<String> response = template.exchange(BASE_URL + "/rest/v1.0/users/1", HttpMethod.GET, requestEntity, String.class);
		logger.info(response.getBody());

	}

	@Test
	public void testMessageGet() throws Exception {

		String accessToken = "accesskey1";
		String secretKey = "secretkey1";
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set(AppConfig.SIGNATURE_HEADER_NAME, RestUtils.generateHmacSHA256Signature("", secretKey));
		requestHeaders.set(AppConfig.ACCESS_TOKEN_HEADER_NAME, accessToken);

		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(parameters, requestHeaders);

		RestTemplate template = new RestTemplate();
		HttpEntity<String> response = template.exchange(BASE_URL + "/rest/v1.0/users/1/messages", HttpMethod.GET, requestEntity, String.class);
		logger.info(response.getBody());

	}

	@Test
	public void testMessagePost() throws Exception {
		String accessToken = "accesskey1";
		String secretKey = "secretkey1";

		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("message", "TestEric");
		parameters.add("userId", "1");

		StringBuilder tmp = new StringBuilder();
		for (Iterator<Entry<String, List<String>>> itr = parameters.entrySet().iterator(); itr.hasNext();) {
			Entry<String, List<String>> entry = itr.next();
			tmp.append("&").append(entry.getKey()).append("=").append(entry.getValue().get(0));
		}
		String queryString = tmp.substring(1);
		logger.info("queryString : " + queryString);

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set(AppConfig.SIGNATURE_HEADER_NAME, RestUtils.generateHmacSHA256Signature(queryString, secretKey));
		requestHeaders.set(AppConfig.ACCESS_TOKEN_HEADER_NAME, accessToken);

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(parameters, requestHeaders);

		RestTemplate template = new RestTemplate();
		HttpEntity<String> response = template.exchange(BASE_URL + "/rest/v1.0/messages", HttpMethod.POST, requestEntity, String.class);
		String etag = response.getHeaders().getETag();
		logger.info("=====================================");
		logger.info("Etag     : {}", etag);
		logger.info("Location : {}", response.getHeaders().get("location"));
		logger.info("Response : {}", response.getBody());

	}

	@Test
	public void testEtag() throws Exception {
		String accessToken = "accesskey1";
		String secretKey = "secretkey1";
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set(AppConfig.SIGNATURE_HEADER_NAME, RestUtils.generateHmacSHA256Signature("", secretKey));
		requestHeaders.set(AppConfig.ACCESS_TOKEN_HEADER_NAME, accessToken);

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
		requestHeaders.set(AppConfig.SIGNATURE_HEADER_NAME, RestUtils.generateHmacSHA256Signature("", secretKey));
		requestHeaders.set(AppConfig.ACCESS_TOKEN_HEADER_NAME, accessToken);

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
