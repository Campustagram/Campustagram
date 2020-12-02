package com.campustagram.core.controller.user;

import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "userProfileEditController")
@Scope(value = "session")
@Component(value = "userProfileEditController")
@Join(path = "/userprofileedit", to = "/pages/core/userManagement/userprofileedit.jsf")
public class UserProfileEditController {
	@Autowired
	private Server server;
	@Autowired
	private UserProfileController userProfileController;
	@Autowired
	private LoggerService loggerService;
	
	private static final String ACTIVE_CLASS_NAME = "UserProfileEditController";

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.START);
		userProfileController.loadData();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.END);
	}

	public String startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);
		if (!isUrlAllowedForEditUserVisibility()) {
			return NavigationUtils.buildRedirectionString("/userlist");
		}
		
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
		return userProfileController.startUpChecks();
	}

	public boolean checkValidTmpUserSession() {
		if (null == userProfileController.getTmpUser() || null == userProfileController.getTmpUser().getId()) {
			return false;
		}
		return true;
	}

	public boolean isUrlAllowedForEditUserVisibility() {
		if (CommonFunctions.getClientUrl(server.getHttpServletRequest()).contains("userprofileedit")) {
			if (checkValidTmpUserSession()) {
				return true;
			}
		}
		return false;
	}
}
