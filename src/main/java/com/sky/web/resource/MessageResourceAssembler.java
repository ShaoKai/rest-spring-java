package com.sky.web.resource;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.sky.web.controller.MessageController;
import com.sky.web.controller.UserController;
import com.sky.web.service.Message;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
@Component
public class MessageResourceAssembler extends ResourceAssemblerSupport<Message, MessageResource> {
	public MessageResourceAssembler() {
		super(MessageController.class, MessageResource.class);
	}

	@Override
	protected MessageResource instantiateResource(Message entity) {
		MessageResource resource = new MessageResource();
		try {
			resource.add(linkTo(methodOn(UserController.class).users(entity.getUserId())).withRel("users"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resource;
	}

	@Override
	public MessageResource toResource(Message entity) {
		return new MessageResource("message", entity.getMessage());
	}
}