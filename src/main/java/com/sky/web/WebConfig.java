package com.sky.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.sky.web.support.RestLoginAuthInterceptor;

@Configuration
@EnableWebMvc
@ComponentScan("com.sky.web.controller")
public class WebConfig extends WebMvcConfigurerAdapter {
	public static final String SIGNATURE_HEADER_NAME    = "X-Signature-Hmac-Sha-256";
	public static final String ACCESS_TOKEN_HEADER_NAME = "X-Access-Token";

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	@Bean
	public RestLoginAuthInterceptor restLoginAuthInterceptor() {
		return new RestLoginAuthInterceptor(SIGNATURE_HEADER_NAME, ACCESS_TOKEN_HEADER_NAME);
	}

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(restLoginAuthInterceptor()).addPathPatterns("/rest/v1.0/*").addPathPatterns("/rest/v1.0/**");

	}
}
