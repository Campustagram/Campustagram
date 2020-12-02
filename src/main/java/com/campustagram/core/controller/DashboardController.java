package com.campustagram.core.controller;

import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.service.LoggerService;

@ManagedBean(name = "dashboardController")
@Scope(value = "session")
@Component(value = "dashboardController")
@Join(path = "/dashboard", to = "/pages/core/dashboard.jsf")
public class DashboardController {

	@Autowired
	private LoggerService loggerService;
	
	@Autowired
	private UserRepository userRepository;

	private static final String ACTIVE_CLASS_NAME = "DashboardController";

	public int getTotalUsers() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "getTotalUsers", null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "getTotalUsers", null, CommonConstants.END);
		return userRepository.countAllNotDeleted();
	}

	public int getOnlineUsers() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "getOnlineUsers", null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "getOnlineUsers", null, CommonConstants.END);
		return userRepository.countAllOnline();
	}
}