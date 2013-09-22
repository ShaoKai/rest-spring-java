package com.sky.web;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.sky.web.support.RestAuthInterceptor;

@Configuration
@EnableWebMvc
@ComponentScan({ "com.sky.web.controller", "com.sky.web.service", "com.sky.web.resource", "com.sky.web.support" })
@Import(AppConfig.class)
public class WebConfig extends WebMvcConfigurerAdapter {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new MappingJackson2HttpMessageConverter());
	}

	@Bean
	public RestAuthInterceptor restLoginAuthInterceptor() {
		return new RestAuthInterceptor(AppConfig.SIGNATURE_HEADER_NAME, AppConfig.ACCESS_TOKEN_HEADER_NAME);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(restLoginAuthInterceptor()).addPathPatterns(AppConfig.REST_BASE_URL + "/*").addPathPatterns(AppConfig.REST_BASE_URL);
	}

}
