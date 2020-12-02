package com.campustagram.core.controller.user.profile;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "themeController")
@Scope(value = "session")
@Component(value = "themeController")
@ELBeanName(value = "themeController")
@Join(path = "/profile/theme", to = "/pages/core/userManagement/profile/theme.jsf")
public class ThemeController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Server server;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;
	
	private User tmpUser;

	private String theme_darkMenu_option;
	private String theme_horizontal_option;
	private String theme_orientationRTL_option;

	private static final String ACTIVE_CLASS_NAME = "ThemeController";

	public String refresh() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "refresh", null, CommonConstants.START);
		tmpUser = (User) CommonFunctions.deepObjectClone(activeUserService.fetchActiveUser());
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "refresh", null, CommonConstants.END);
		return null;
	}

	public String saveUser() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "saveUser", null, CommonConstants.START);
		userRepository.save(tmpUser);
		CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "onYourRequestThemeChanged", "whiteSpaceChar");
		server.writeLog("themeChanged", CommonConstants.LOG_UPDATE);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "saveUser", null, CommonConstants.END);
		return null;
	}

	public void theme_darkMenu(String option) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "theme_darkMenu", null, CommonConstants.START);
		if (option.equals("darkMenu")) {
			tmpUser.setTheme_darkMenu(true);
		} else {
			tmpUser.setTheme_darkMenu(false);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "theme_darkMenu", null, CommonConstants.END);
	}

	public void theme_horizontal(String option) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "theme_horizontal", null, CommonConstants.START);
		if (option.equals("horizontal")) {
			tmpUser.setTheme_horizontal(true);
		} else {
			tmpUser.setTheme_horizontal(false);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "theme_horizontal", null, CommonConstants.END);
	}

	public void theme_orientationRTL(String option) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "theme_orientationRTL", null, CommonConstants.START);
		if (option.equals("active")) {
			tmpUser.setTheme_orientationRTL(true);
		} else {
			tmpUser.setTheme_orientationRTL(false);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "theme_orientationRTL", null, CommonConstants.END);
	}

	public String startUpCheck() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, null, CommonConstants.START);
		if (null != activeUserService.fetchActiveUser()) {
			tmpUser = (User) CommonFunctions.deepObjectClone(activeUserService.fetchActiveUser());
			loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, null, CommonConstants.END);
			return null;
		} else {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, "redirectLogin",
					CommonConstants.END);
			return NavigationUtils.buildRedirectionString("/login");
		}
	}

	public String getTheme_darkMenu_option() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "getTheme_darkMenu_option", null, CommonConstants.START);
		if (this.tmpUser.isTheme_darkMenu())

			try {
				if (null != this.tmpUser) {
					if (this.tmpUser.isTheme_darkMenu())
						this.theme_darkMenu_option = "darkMenu";
					else {
						this.theme_darkMenu_option = "lightMenu";
					}
					loggerService.writeInfo("ThemeController", "getTheme_darkMenu_option", null, "END");
				} else {
					// default menu option
					this.theme_darkMenu_option = "lightMenu";
					loggerService.writeInfo("ThemeController", "getTheme_darkMenu_option", null, "END");
				}
			} catch (Exception e) {
				// default menu option
				this.theme_darkMenu_option = "lightMenu";
			}
		loggerService.writeInfo("ThemeController", "getTheme_darkMenu_option", null, "END");
		return theme_darkMenu_option;
	}

	public void setTheme_darkMenu_option(String theme_darkMenu_option) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "setTheme_darkMenu_option", null, CommonConstants.START);
		this.theme_darkMenu_option = theme_darkMenu_option;
		if (this.theme_darkMenu_option.equals("darkMenu")) {
			tmpUser.setTheme_darkMenu(true);
		} else {
			tmpUser.setTheme_darkMenu(false);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "setTheme_darkMenu_option", null, CommonConstants.END);
	}

	public String getTheme_horizontal_option() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "getTheme_horizontal_option", null, CommonConstants.START);
		try {
			if (null != this.tmpUser) {
				if (tmpUser.isTheme_horizontal()) {
					this.theme_horizontal_option = "horizontal";
				} else {
					this.theme_horizontal_option = "vertical";
				}
			} else {
				// default menu option
				this.theme_horizontal_option = "vertical";
				loggerService.writeInfo("ThemeController", "getTheme_horizontal_option", null, "END");

			}
		} catch (Exception e) {
			this.theme_horizontal_option = "vertical";
		}
		loggerService.writeInfo("ThemeController", "getTheme_horizontal_option", null, "END");
		return theme_horizontal_option;
	}

	public void setTheme_horizontal_option(String theme_horizontal_option) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "setTheme_horizontal_option", null, CommonConstants.START);
		this.theme_horizontal_option = theme_horizontal_option;
		if (this.theme_horizontal_option.equals("horizontal")) {
			tmpUser.setTheme_horizontal(true);
		} else {
			tmpUser.setTheme_horizontal(false);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "setTheme_horizontal_option", null, CommonConstants.END);
	}

	public String getTheme_orientationRTL_option() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "getTheme_orientationRTL_option", null, CommonConstants.START);
		try {
			if (null != this.tmpUser) {
				if (tmpUser.isTheme_orientationRTL()) {
					this.theme_orientationRTL_option = "orientationRTL";
				} else {
					this.theme_orientationRTL_option = "orientationLTR";
				}
			} else {
				this.theme_orientationRTL_option = "orientationLTR";
				loggerService.writeInfo("ThemeController", "getTheme_orientationRTL_option", null, "END");
			}
		} catch (Exception e) {
			this.theme_orientationRTL_option = "orientationLTR";
		}
		loggerService.writeInfo("ThemeController", "getTheme_orientationRTL_option", null, "END");
		return theme_orientationRTL_option;
	}

	public void setTheme_orientationRTL_option(String theme_orientationRTL_option) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "setTheme_orientationRTL_option", null, CommonConstants.START);
		this.theme_orientationRTL_option = theme_orientationRTL_option;
		if (this.theme_orientationRTL_option.equals("orientationRTL")) {
			tmpUser.setTheme_orientationRTL(true);
		} else {
			tmpUser.setTheme_orientationRTL(false);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "setTheme_orientationRTL_option", null, CommonConstants.END);
	}

	public User getTmpUser() {
		return tmpUser;
	}

	public void setTmpUser(User tmpUser) {
		this.tmpUser = tmpUser;
	}

}
