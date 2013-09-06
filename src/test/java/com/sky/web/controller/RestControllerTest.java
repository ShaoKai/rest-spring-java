package com.sky.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import com.sky.web.WebConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
public class RestControllerTest {
	@Autowired
	protected WebApplicationContext wac;
	protected MockMvc mockMvc;

	@Before
	public final void onSetup() throws Exception {
		mockMvc = webAppContextSetup(wac).build();
	}

	@Test
	public void test1() throws Exception {
		// @formatter:off
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(WebConfig.SIGNATURE_HEADER_NAME, "");
		httpHeaders.set(WebConfig.ACCESS_TOKEN_HEADER_NAME, "");
		
		MockHttpServletRequestBuilder requestBuilder = get("/rest/v1.0/message");
		requestBuilder.headers(httpHeaders);
		
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isBadRequest());
									   
		// @formatter:on
	}

	@Test
	public void test() throws Exception {
		// @formatter:off
		mockMvc.perform(get("/rest/v1.0/message").header(WebConfig.SIGNATURE_HEADER_NAME, "")
												.header(WebConfig.ACCESS_TOKEN_HEADER_NAME, ""))
												.andDo(print()).andExpect(status().isBadRequest());
									   
		// @formatter:on
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
}
