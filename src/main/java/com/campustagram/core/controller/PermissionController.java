package com.campustagram.core.controller;

import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "permissionController")
@Scope(value = "request")
@Component(value = "permissionController")
@Join(path = "/permissiondenied", to = "/pages/core/errorpage/403.jsf")
public class PermissionController {

	@Autowired
	private Server server;
	@Autowired
	private LoggerService loggerService;
	
	private static final String ACTIVE_CLASS_NAME = "PermissionController";

	public String redirectPermDenied() {
		if (null != server) {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, "redirectPermDenied", null, CommonConstants.START);
			loggerService.writeInfo(ACTIVE_CLASS_NAME, "redirectPermDenied", null, CommonConstants.END);

		}

		return NavigationUtils.buildRedirectionString("/403");
	}

	public void startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);
		server.createPageVisitLog(null, "redirectTo403", CommonConstants.LOG_INFO);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
	}

}
