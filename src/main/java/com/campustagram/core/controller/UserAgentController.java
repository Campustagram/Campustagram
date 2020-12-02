package com.campustagram.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.controller.log.ILogger;
import com.campustagram.core.model.UserAgent;
import com.campustagram.core.persistence.UserAgentRepository;
import com.campustagram.core.service.LoggerService;

@Scope(value = "prototype")
@Component(value = "userAgentController")
public class UserAgentController {
	@Autowired
	private UserAgentRepository userAgentRepository;
	@Autowired
	private LoggerService loggerService;

	private static final String ACTIVE_CLASS_NAME = "ProxyController";

	public UserAgentController() {
		super();
	}

	public UserAgent getRandomUserAgent() throws Exception {
		if (null != userAgentRepository) {
			return userAgentRepository.getRandomUserAgent();
		}
		return null;
	}

}
