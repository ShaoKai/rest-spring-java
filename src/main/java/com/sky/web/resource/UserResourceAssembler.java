package com.sky.web.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.sky.web.controller.UserController;
import com.sky.web.service.User;
@Component
public class UserResourceAssembler extends ResourceAssemblerSupport<User, UserResource> {
	public UserResourceAssembler() {
		super(UserController.class, UserResource.class);
	}

	@Override
	protected UserResource instantiateResource(User entity) {
		UserResource resource = new UserResource();
		// resource.add(linkTo(UserController.class).slash("messages").withRel("messages"));
		try {
			resource.add(linkTo(methodOn(UserController.class).messages(entity.getUserId())).withRel("messages"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resource;
	}

	@Override
	public UserResource toResource(User entity) {
		UserResource resource = new UserResource();
		resource.setUserId(entity.getUserId());
		resource.setUserName(entity.getAccount());
		try {
			resource.add(linkTo(methodOn(UserController.class).messages(entity.getUserId())).withRel("messages"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resource;
	}
}