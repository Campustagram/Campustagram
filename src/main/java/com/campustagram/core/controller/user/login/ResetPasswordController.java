package com.campustagram.core.controller.user.login;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonCryptographicHash;
import com.campustagram.core.common.CommonDate;
import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.model.EmailTemplate;
import com.campustagram.core.model.ResetPassword;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.EmailTemplateRepository;
import com.campustagram.core.persistence.ResetPasswordRepository;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.service.MailSenderService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "resetpasswordController")
@Scope(value = "session")
@Component(value = "resetpasswordController")
@Join(path = "/resetpassword", to = "/pages/core/login/resetpassword.jsf")
public class ResetPasswordController {
	@Autowired
	private Server server;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ResetPasswordRepository resetPasswordRepository;
	@Autowired
	private LoginController loginController;
	@Autowired
	private EmailTemplateRepository emailTemplateRepository;
	@Autowired
	private MailSenderService mailSenderService;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;
	
	private User user = new User();
	private User tmpUser;

	private ResetPassword resetPassword;

	private boolean isTimeOver = false;

	private boolean isCounterOver = false;

	private static final String ACTIVE_CLASS_NAME = "ResetPasswordController";

	/**
	 * This method send email if the user information is stored in the
	 * userRepository database.
	 * 
	 * @return page_path
	 */
	public String send() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "send", null, CommonConstants.START);
		try {
			if (null != (tmpUser = userRepository.findByEmailNotDeleted(this.user.getEmail()))) {
				// if the user information is stored in the userRepository database.
				/*String returnPage;
				if (null != (returnPage = isOnMaintainceVerifyPage(tmpUser))) {
					return returnPage;
				}*/

				user = (User) CommonFunctions.deepObjectClone(tmpUser);
				if (null != (resetPassword = resetPasswordRepository.findByUserId(tmpUser.getId()))) {
					// if the user information is stored in the resetPasswordRepository database.
					resendCode(resetPassword);
				} else {
					// if the user information is not stored in the userRepository database.
					sendCode();
				}
				loggerService.writeInfo(ACTIVE_CLASS_NAME, "send", null, CommonConstants.END);
				return NavigationUtils.buildRedirectionString("/verifycode");
			} else {
				// if the user information is not stored in the userRepository database.
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "werentIdentifyInformation",
						CommonConstants.WHITE_SPACE_CHAR);
				loggerService.writeInfo(ACTIVE_CLASS_NAME, "send", "werentIdentifyInformation",
						CommonConstants.END);
				return null;
			}
		} catch (Exception e) {
			// in case of any error.
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "unableProvideServices",
					CommonConstants.WHITE_SPACE_CHAR);
			loggerService.writeError(ACTIVE_CLASS_NAME, "send", "unableProvideServices", CommonConstants.ERROR);
			return null;
		}
	}

	/*public String isOnMaintainceVerifyPage(User user) {
		final String ACTIVE_METHOD_NAME = "isOnMaintainceVerifyPage";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		if ((server.getSystemProperties().isOnMaintenance()) && (server.getSystemProperties().isEndDateActive())
				&& !(roleRightController.checkIfUserHasRoleType(user , RoleType.ROLE_SYSTEM_MANAGEMENT))) {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, "redirect maintenance",
					CommonConstants.START);
			return "/pages/core/maintenance/maintenance.xhtml?faces-redirect=true";
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;
	}*/

	/**
	 * This function generate verification code. Encrypts the verification code.
	 * This function ADDs time , encrypted verification code and user information to
	 * the resetPasswordRepository database. The verification code is sent with
	 * e-mail.
	 */
	private void sendCode() {
		final String ACTIVE_METHOD_NAME = "sendCode";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		// Code send successful.
		// The user information added to resetPasswordRepository database.
		try {
			ResetPassword rp = new ResetPassword();
			rp.setCreateDate(CommonDate.currentDate());
			rp.setHash(CommonFunctions.generateRandomStringBySize(6));
			rp.setUser(user);
			sendHTMLMailForPasswordVerifyCode(user, rp.getHash());
			rp.setHash(CommonCryptographicHash.encryptChar(rp.getHash().toCharArray()));
			resetPasswordRepository.save(rp);
			resetPassword = rp;
			server.writeLogWithUser(user, "resetPasswordCodeSent", CommonConstants.LOG_INFO);
			CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "verificationCodeResent",
					CommonConstants.WHITE_SPACE_CHAR);
		}
		// in case of any error.
		catch (Exception e) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "verificationCodeNotResent",
					CommonConstants.WHITE_SPACE_CHAR);
			loggerService.writeError(ACTIVE_CLASS_NAME, "verificationCodeNotResent",
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	/**
	 * This function generate verification code. Encrypts the verification code.
	 * This function UPDATEs time , encrypted verification code and user information
	 * to the resetPasswordRepository database. The verification code is sent with
	 * e-mail.
	 */
	public void resendCode(ResetPassword resetPassword) {
		final String ACTIVE_METHOD_NAME = "resendCode";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		// Code send successful.
		// The user information updated to resetPasswordRepository database.
		try {
			resetPassword.setCounter(0);
			resetPassword.setCreateDate(CommonDate.currentDate());
			resetPassword.setHash(CommonFunctions.generateRandomStringBySize(6));
			sendHTMLMailForPasswordVerifyCode(user, resetPassword.getHash());
			resetPassword.setHash(CommonCryptographicHash.encryptChar(resetPassword.getHash().toCharArray()));
			resetPasswordRepository.save(resetPassword);
			server.writeLogWithUser(user, "resetPasswordCodeResent", CommonConstants.LOG_INFO);
			CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "verificationCodeResent",
					CommonConstants.WHITE_SPACE_CHAR);
		}
		// in case of any error.
		catch (Exception e) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "verificationCodeNotResent",
					CommonConstants.WHITE_SPACE_CHAR);
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	// Use it to send HTML emails
	public void sendHTMLMailForPasswordVerifyCode(User user, String code) throws Exception {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "sendHTMLMailForPasswordVerifyCode", null,
				CommonConstants.START);
		EmailTemplate emailTemplate = mailSenderService.mailGenerator(user);
		emailTemplate.setSubject(server.getMultiLanguageStringWithKey("verifyCode"));
		emailTemplate.setContent("<p>" + server.getMultiLanguageStringWithKey("hello") + " " + user.getName() + "</p>"
				+ "<p>" + server.getMultiLanguageStringWithKey("verifyCode") + " :  <strong>" + code + "</strong></p>");
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "verificationCodeNotResent", "Reset Code: " + code, "INFO");

		emailTemplateRepository.save(emailTemplate);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "sendHTMLMailForPasswordVerifyCode", null, CommonConstants.END);
	}

	/**
	 * This function runs when resetpassword.xhtml is first opened.
	 */
	public String startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);
		if (null != activeUserService.fetchActiveUser()) {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, "redirectResetpassword", null, CommonConstants.END);
			return NavigationUtils.buildRedirectionString("/dashboard");
		}
		if (isCounterOver) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "enterWrongCode", CommonConstants.WHITE_SPACE_CHAR);
			loginController.clearSession();
		}

		if (isTimeOver) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "codeTimeOver", CommonConstants.WHITE_SPACE_CHAR);
			loginController.clearSession();
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
		return null;
	}

	// getters and setters methods
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ResetPassword getResetPassword() {
		return resetPassword;
	}

	public void setResetPassword(ResetPassword resetPassword) {
		this.resetPassword = resetPassword;
	}

	public boolean isTimeOver() {
		return isTimeOver;
	}

	public void setTimeOver(boolean isTimeOver) {
		this.isTimeOver = isTimeOver;
	}

	public boolean isCounterOver() {
		return isCounterOver;
	}

	public void setCounterOver(boolean isCounterOver) {
		this.isCounterOver = isCounterOver;
	}

	public User getTmpUser() {
		return tmpUser;
	}

	public void setTmpUser(User tmpUser) {
		this.tmpUser = tmpUser;
	}

}
