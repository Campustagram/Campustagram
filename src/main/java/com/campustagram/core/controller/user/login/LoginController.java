package com.campustagram.core.controller.user.login;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.ocpsoft.rewrite.annotation.Join;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonDate;
import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.model.RememberMe;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.RememberMeRepository;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "loginController")
@Scope(value = "request")
@Component(value = "loginController")
@Join(path = "/login", to = "/pages/core/login/login.jsf")
public class LoginController {
	@Autowired
	private Server server;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RememberMeRepository rememberMeRepository;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;
	
	private User user = new User(); // logged user

	private boolean remember = false;

	private HttpServletResponse response;
	private boolean isResetPassword = false;

	private static final String ACTIVE_CLASS_NAME = "LoginController";

	@PostConstruct
	public void init() {
		this.response = server.getHttpServletResponse();
	}

	public String startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);
		if (null != activeUserService.fetchActiveUser()) {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
			return NavigationUtils.buildRedirectionString("/dashboard");
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
		return null;
	}

	public String clearSessionReturnLogin() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "clearSessionReturnLogin", null, CommonConstants.START);
		clearSession();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "clearSessionReturnLogin", null, CommonConstants.END);
		return NavigationUtils.buildRedirectionString("/login");
	}

	public void clearSession() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "clearSession", null, CommonConstants.START);
		CommonFunctions.killSession("resetpasswordController");
		CommonFunctions.killSession("verifycodeController");
		CommonFunctions.killSession("createnewpasswordController");
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "clearSession", null, CommonConstants.END);
	}

	public void setUserAsOnline(User user) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "setUserAsOnline", null, CommonConstants.START);
		user.setOnline(true);
		userRepository.save(user);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "setUserAsOnline", null, CommonConstants.END);
	}

	/**
	 * If login page comes after sign up page there is a welcoming message for new
	 * user To show this message we control Register user in Server. If register
	 * user mail is not null the message will be shown
	 * 
	 * This function set the null to register user mail after showing message
	 */
	public void updateRegisterUserForReload() {
		server.setRegisterUser(null);
	}

	public void updatepasswordChangedUserForReload() {
		server.setPasswordChangedUser(null);
	}

	public void updateBlockedUserForReload() {
		server.setBlockedUser(null);
	}

	public void decideReload() {
		
		System.err.println("Startasdasfsdfsfd");
		
		if(null == activeUserService.fetchActiveUser()) {
			PrimeFaces.current().executeScript("console.log('reloaded in backing bean');window.location.reload();");
		}
		
		//PrimeFaces.current().executeScript("console.log('reloaded in backing bean');window.location.reload();");
		
		System.err.println("END");
	}

	/*public boolean checkUsersForMaintenance() {
		final String ACTIVE_METHOD_NAME = "checkUsersForMaintenance";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		if ((server.getSystemProperties().isOnMaintenance() == false)
				|| (!server.getSystemProperties().isEndDateActive())) {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
			return false;
		}
		if (server.getSystemProperties().isOnMaintenance() == true && server.getSystemProperties().isEndDateActive()
				&& roleRightController.checkIfActiveUserHasRoleType(RoleType.ROLE_SYSTEM_MANAGEMENT)) {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
			return false;
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return true;
	}*/

	public String redirectLogin() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "redirectLogin", null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "redirectLogin", null, CommonConstants.END);
		return NavigationUtils.buildRedirectionString("/login");
	}

	public void updateUserLastSeen() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "updateUserLastSeen", null, CommonConstants.START);
		if (null != activeUserService.fetchActiveUser()) {
			userRepository.updateUserLastSeen(activeUserService.fetchActiveUser().getId(), CommonDate.currentDate());
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "updateUserLastSeen", null, CommonConstants.END);
	}

	public boolean allowContinueInPanel() {
		final String ACTIVE_METHOD_NAME = "allowContinueInPanel";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

		if (null != activeUserService.fetchActiveUser()
				&& (!activeUserService.fetchActiveUser().isActive() || activeUserService.fetchActiveUser().isDeleted())) {
			removeCookies();
			server.createPageVisitLog(activeUserService.fetchActiveUser(), "blockedUserPageEntryAttempts",
					CommonConstants.LOG_WARNING);
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
			return false;
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return true;
	}

	public void verifyVisitLoginPages() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "verifyVisitLoginPages", null, CommonConstants.START);
		server.createPageVisitLog(null, "pageEntry", CommonConstants.LOG_INFO);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "verifyVisitLoginPages", null, CommonConstants.END);
	}

	/**
	 * Check is there any cookie which exists in database. Create Remember me object
	 * and set object which exists in database
	 * 
	 * @return RememberMe object from database
	 */
	private RememberMe checkCookie() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "checkCookie", null, CommonConstants.START);
		RememberMe rememberMeReturn = null;

		String hash = "";
		String id = "";
		Cookie[] cookies = server.getHttpServletRequest().getCookies(); // get all cookies
		if (cookies != null) {
			for (Cookie ck : cookies) {
				if (ck.getName().equalsIgnoreCase("cookieHash"))
					hash = ck.getValue();
				if (ck.getName().equalsIgnoreCase("cookieId"))
					id = ck.getValue();
			}
		}

		if (!hash.isEmpty() && !id.isEmpty()) { // if cookies are not empty
			RememberMe rememberme = rememberMeRepository.findByHashAndHashId(hash, id);
			// find remember objects saved in cookies
			if (null != rememberme) {
				if (rememberme.getHash().equals(hash) && rememberme.getHashOfUserId().equals(id)) {
					// find an object which equals hash and hash id
					rememberMeReturn = new RememberMe();
					rememberMeReturn = rememberme;
				}
			}

		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "checkCookie", null, CommonConstants.END);
		return rememberMeReturn; // if there is no cookies it will return null object
	}

	/**
	 * First check is there any cookies. Then read hash and hash id from cookies
	 * also remove the cookies Control if hash and hash id are exists in database
	 * then remove
	 * 
	 */
	public void removeCookies() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "removeCookies", null, CommonConstants.START);
		String hash = "";
		String id = "";
		for (Cookie ck : server.getHttpServletRequest().getCookies()) {
			if (ck.getName().equalsIgnoreCase("cookieHash")) {
				hash = ck.getValue();
				ck.setMaxAge(0);
				response.addCookie(ck);
			}
			if (ck.getName().equalsIgnoreCase("cookieId")) {
				id = ck.getValue();
				ck.setMaxAge(0);
				response.addCookie(ck);
			}
		}
		if (!hash.isEmpty() && !id.isEmpty()) { // if cookies not empty
			RememberMe rememberme = rememberMeRepository.findByHashAndHashId(hash, id);

			if (null != rememberme) {
				if (rememberme.getHash().equals(hash) && rememberme.getHashOfUserId().equals(id)) {
					System.out.println("Hash: " + rememberme.getHash() + " Hash id: " + rememberme.getHashOfUserId());
					rememberMeRepository.delete(rememberme);
				}
			}

		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "removeCookies", null, CommonConstants.END);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isRemember() {
		return remember;
	}

	public void setRemember(boolean remember) {
		this.remember = remember;
	}

	public boolean getResetPassword() {
		return isResetPassword;
	}

	public void setResetPassword(boolean isResetPassword) {
		this.isResetPassword = isResetPassword;
	}
}
