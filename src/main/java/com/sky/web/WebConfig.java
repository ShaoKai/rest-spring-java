package com.sky.web;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.sky.web.support.RestAuthInterceptor;

@Configuration
@EnableWebMvc
@ComponentScan("com.sky.web.controller")
public class WebConfig extends WebMvcConfigurerAdapter {
	public static final String SIGNATURE_HEADER_NAME = "X-Signature-Hmac-Sha-256";
	public static final String ACCESS_TOKEN_HEADER_NAME = "X-Access-Token";

	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new MappingJackson2HttpMessageConverter());
	}

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	@Bean
	public RestAuthInterceptor restLoginAuthInterceptor() {
		return new RestAuthInterceptor(SIGNATURE_HEADER_NAME, ACCESS_TOKEN_HEADER_NAME);
	}

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(restLoginAuthInterceptor()).addPathPatterns("/rest/v1.0/*").addPathPatterns("/rest/v1.0/**");
	}
}
