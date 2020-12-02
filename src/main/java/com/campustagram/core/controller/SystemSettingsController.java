package com.campustagram.core.controller;

import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "systemSettingsController")
@Scope(value = "session")
@Component(value = "systemSettingsController")
@Join(path = "/system/settings", to = "/pages/core/systemManagement/systemConfiguration/settings.jsf")
public class SystemSettingsController {

	@Autowired
	private ActiveUserService activeUserService;
	@Autowired
	private LoggerService loggerService;
	
	private static final String ACTIVE_CLASS_NAME = "SystemSettingsController";

	public String generalSettings() {
		final String ACTIVE_METHOD_NAME = "generalSettings";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return NavigationUtils.buildRedirectionString("/system/settings/generalsettings");
	}

	public String systemEmail() {
		final String ACTIVE_METHOD_NAME = "systemEmail";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return NavigationUtils.buildRedirectionString("/system/settings/systememail");
	}

	public String startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, null, CommonConstants.START);
		if (null != activeUserService.fetchActiveUser()) {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, null, CommonConstants.END);
			return null;
		} else {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, "redirectLogin", CommonConstants.END);
			return NavigationUtils.buildRedirectionString("/login");
		}
	}

}
