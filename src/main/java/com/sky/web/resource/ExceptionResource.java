package com.sky.web.resource;

import org.springframework.hateoas.ResourceSupport;

public class ExceptionResource extends ResourceSupport {
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
