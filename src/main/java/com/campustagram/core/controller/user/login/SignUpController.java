package com.campustagram.core.controller.user.login;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonCryptographicHash;
import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.controller.LocaleController;
import com.campustagram.core.model.Role;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.RoleRepository;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.security.service.BCryptEncoderService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "signupController")
@Scope(value = "request")
@Component(value = "signupController")
@Join(path = "/signup", to = "/pages/core/login/signup.jsf")
public class SignUpController {
	@Autowired
	private Server server;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LocaleController localeController;
	@Autowired
	private Environment env;
	@Autowired
	private SignupVerifyCodeController signupVerifyCodeController;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;
	@Autowired
	private BCryptEncoderService bCryptEncoderService;
	
	private User user = new User();
	private String tempPassword;
	private boolean isUniqueEmail = true;
	private String verifyCode;

	private String selectedTmpLanguage;

	private static final String ACTIVE_CLASS_NAME = "SignUpController";

	@PostConstruct
	public void init() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "init", null, CommonConstants.START);
		user.setLanguage(server.getLanguageAsObject(String.valueOf(localeController.getLocale())));
		selectedTmpLanguage = String.valueOf(localeController.getLocale());
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "init", null, CommonConstants.END);
	}

	public String startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, null, CommonConstants.START);

		if(null != activeUserService.fetchActiveUser()) {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, null, CommonConstants.END);
			return NavigationUtils.buildRedirectionString("/dashboard");
		}

		loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, null, CommonConstants.END);
		return null;
	}

	/**
	 * This method saves the user in the database.
	 * 
	 * @return page_path
	 */
	public String save() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "save", null, CommonConstants.START);
		// passwords not match
		if (!user.getPassword().equals(tempPassword)) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "passwordsAreNotSame",
					CommonConstants.WHITE_SPACE_CHAR);
		}
		// password length is short
		if (!CommonCryptographicHash.isThePasswordLongEnough(user.getPassword().toCharArray())) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "passwordInvalid",
					CommonConstants.WHITE_SPACE_CHAR);
		}

		// email format is wrong
		if (!CommonFunctions.isEmailUsableFormat(user.getEmail())) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "emailIsNotValid",
					CommonConstants.WHITE_SPACE_CHAR);
		}

		// email isnot unique
		if (CommonFunctions.isEmailUsableFormat(user.getEmail())
				&& (null != userRepository.findByEmailNotDeleted(this.user.getEmail()))) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "emailAddressAlreadyInUse",
					CommonConstants.WHITE_SPACE_CHAR);
			isUniqueEmail = false;
		}

		if (user.getPassword().equals(tempPassword)
				&& CommonCryptographicHash.isThePasswordLongEnough(user.getPassword().toCharArray())
				&& CommonFunctions.isEmailUsableFormat(user.getEmail()) && isUniqueEmail) {
			user.setPassword(bCryptEncoderService.encode(user.getPassword()));
			try {
				user.setLanguage(server.getLanguageAsObject(selectedTmpLanguage));

				if ((null != env.getProperty("system.register.verifyEmail"))
						&& (env.getProperty("system.register.verifyEmail").equals("true"))) {
					signupVerifyCodeController.setTmpUser(user);
					signupVerifyCodeController.setVerifyCode(getVerifyCode());
					signupVerifyCodeController.sendHTMLMailForPasswordVerifyCode(user, getVerifyCode());
					loggerService.writeInfo(ACTIVE_CLASS_NAME, "save", null, CommonConstants.END);
					return NavigationUtils.buildRedirectionString("/signupverifycode");
				} else {
					Role regularUserRole = roleRepository.findByNameNotDeleted("regularUser");
					user.setRole(regularUserRole);
					regularUserRole.getUsers().add(user);
					userRepository.save(user);
					server.setRegisterUser(user);
					server.writeLogWithUser(user, "signedUp", CommonConstants.LOG_INFO);
					loggerService.writeInfo(ACTIVE_CLASS_NAME, "save", null, CommonConstants.END);
					return NavigationUtils.buildRedirectionString("/login");
				}

			} catch (Exception e) {
				loggerService.writeError(ACTIVE_CLASS_NAME, "save",
						com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), "ERROR");
				loggerService.writeInfo(ACTIVE_CLASS_NAME, "save", "error", CommonConstants.END);
				return null;// same page
			}

		} else {
			user.setPassword(null);
			this.setTempPassword(null);
			loggerService.writeInfo(ACTIVE_CLASS_NAME, "save", null, CommonConstants.END);
			return null;// same page
		}

	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTempPassword() {
		return tempPassword;
	}

	public void setTempPassword(String tempPassword) {
		this.tempPassword = tempPassword;
	}

	public String getSelectedTmpLanguage() {
		return selectedTmpLanguage;
	}

	public void setSelectedTmpLanguage(String selectedTmpLanguage) {
		this.selectedTmpLanguage = selectedTmpLanguage;
	}

	public String getVerifyCode() {
		if (null == verifyCode) {
			setVerifyCode(CommonFunctions.generateRandomStringBySize(6));
		}
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

}