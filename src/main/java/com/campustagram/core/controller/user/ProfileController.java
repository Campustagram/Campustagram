package com.campustagram.core.controller.user;

import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "profileController")
@Scope(value = "session")
@Component(value = "profileController")
@Join(path = "/profile", to = "/pages/core/userManagement/profile.jsf")
public class ProfileController {
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;
	
	private static final String ACTIVE_CLASS_NAME = "ProfileController";

	public String editInformation() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "editInformation", null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "editInformation", null, CommonConstants.END);
		return NavigationUtils.buildRedirectionString("/profile/editinformation");
	}

	public String security() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "security", null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "security", null, CommonConstants.END);
		return NavigationUtils.buildRedirectionString("/profile/security");
	}

	public String themeSettings() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "themeSettings", null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "themeSettings", null, CommonConstants.END);
		return NavigationUtils.buildRedirectionString("/profile/theme");
	}

	public String startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, null, CommonConstants.START);
		if (null != activeUserService.fetchActiveUser()) {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, null, CommonConstants.END);
			return null;
		} else {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, "redirectLogin",
					CommonConstants.END);
			return NavigationUtils.buildRedirectionString("/login");
		}
	}

}
