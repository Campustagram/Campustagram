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
import com.campustagram.core.security.service.BCryptEncoderService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "securityController")
@Scope(value = "session")
@Component(value = "securityController")
@ELBeanName(value = "securityController")
@Join(path = "/profile/security", to = "/pages/core/userManagement/profile/security.jsf")
public class SecurityController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Server server;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private BCryptEncoderService bCryptEncoderService;
	@Autowired
	private ActiveUserService activeUserService;
	
	private User tmpUser;
	private boolean isEditMode = false;

	private String password = null;
	private String newPassword = null;
	private String reEnterNewPassword = null;

	private static final String ACTIVE_CLASS_NAME = "SecurityController";

	public String refresh() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "refresh", null, CommonConstants.START);
		password = null;
		newPassword = null;
		reEnterNewPassword = null;
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "refresh", null, CommonConstants.END);
		return null;
	}

	public String saveUser() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "saveUser", null, CommonConstants.START);
		if (null != activeUserService.fetchActiveUser()) {
			if (!bCryptEncoderService.matches(password , activeUserService.fetchActiveUser().getPassword())) {
				// eski şifreyi yanlış girdi
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "wrongPassword", "whiteSpaceChar");
			}
			if (!newPassword.equals(reEnterNewPassword)) {
				// şifreler aynı değil
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "passwordsAreNotSame", "whiteSpaceChar");
			}
			if (password.equals(newPassword)) {
				// eski şifre ile yeni şifre aynı olamaz
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR,
						"theOldPasswordAndTheNewPasswordCannotBeTheSame", "whiteSpaceChar");
			}
			if (bCryptEncoderService.matches(password , activeUserService.fetchActiveUser().getPassword())
					&& newPassword.equals(reEnterNewPassword) && (!password.equals(newPassword))) {
				tmpUser.setPassword(bCryptEncoderService.encode(newPassword));
				userRepository.save(tmpUser);
				CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "passwordChanged", "whiteSpaceChar");
				// server.writeLog("theUserChangedThePassword", CommonConstants.LOG_UPDATE);
				server.writeLogWithUser(activeUserService.fetchActiveUser(), "theUserChangedThePassword",
						CommonConstants.LOG_UPDATE);
				loggerService.writeInfo("SecurityController", "saveUser", null, "END");
			} else {
				loggerService.writeInfo("SecurityController", "saveUser", "fail", "END");
			}
		} else {
			loggerService.writeInfo("SecurityController", "saveUser", "fail", "END");
		}
		return null;
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

	public User getTmpUser() {
		return tmpUser;
	}

	public void setTmpUser(User tmpUser) {
		this.tmpUser = tmpUser;
	}

	public boolean isEditMode() {
		return isEditMode;
	}

	public void setEditMode(boolean isEditMode) {
		this.isEditMode = isEditMode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getReEnterNewPassword() {
		return reEnterNewPassword;
	}

	public void setReEnterNewPassword(String reEnterNewPassword) {
		this.reEnterNewPassword = reEnterNewPassword;
	}

}
