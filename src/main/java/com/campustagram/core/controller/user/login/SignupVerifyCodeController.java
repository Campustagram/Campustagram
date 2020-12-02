package com.campustagram.core.controller.user.login;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.model.EmailTemplate;
import com.campustagram.core.model.Role;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.EmailTemplateRepository;
import com.campustagram.core.persistence.RoleRepository;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.service.MailSenderService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "signupVerifyCodeController")
@Scope(value = "session")
@Component(value = "signupVerifyCodeController")
@Join(path = "/signupverifycode", to = "/pages/core/login/signupverifycode.jsf")
public class SignupVerifyCodeController {
	@Autowired
	private Server server;
	@Autowired
	private MailSenderService mailSenderService;
	@Autowired
	private EmailTemplateRepository emailTemplateRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;
	
	private String verifyCodeFromTheUser;
	private String verifyCode;

	private boolean correctVerifyCode = false;
	private boolean resendUsed = false;
	private boolean verifyResend = true;
	private User tmpUser;

	private static final String ACTIVE_CLASS_NAME = "SignupVerifyCodeController";

	/**
	 * If the entered code match, return creteNewPassword page
	 * 
	 * @return page_path
	 */
	public String checkCode() {
		final String ACTIVE_METHOD_NAME = "checkCode";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

		try {
			if (verifyCode.equals(getVerifyCodeFromTheUser())) {
				// the entered code match

				server.setRegisterUser(getTmpUser());

				setCorrectVerifyCode(true);
				Role regularUserRole = roleRepository.findByNameNotDeleted("regularUser");
				getTmpUser().setRole(regularUserRole);
				regularUserRole.getUsers().add(getTmpUser());
				userRepository.save(getTmpUser());

				EmailTemplate emailTemplate = new EmailTemplate();
				emailTemplate.setTo("dodgehellcat3478@gmail.com");
				emailTemplate.setFrom("WebScraper");
				emailTemplate.setSubject("New User Added");
				emailTemplate.setContent(getTmpUser().toString());

				emailTemplateRepository.save(emailTemplate);

				server.writeLogWithUser(getTmpUser(), "signedUp", CommonConstants.LOG_INFO);
				loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
				return NavigationUtils.buildRedirectionString("/login");

			} else {
				// the entered code not match
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "invalidCheckCode", "whiteSpaceChar");

				setCorrectVerifyCode(false);
				loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, "the entered code not match",
						CommonConstants.END);
				return null;
			}
		} catch (Exception e) {
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
		}
		return null;
	}

	/**
	 * This function work when you click on the "resend" button. It sends the code
	 * again.
	 */
	public void resendClicked() {
		final String ACTIVE_METHOD_NAME = "resendClicked";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		setResendUsed(true);
		setVerifyCodeFromTheUser(null);
		refreshVerifyCode();
		try {
			sendHTMLMailForPasswordVerifyCode(getTmpUser(), verifyCode);
			CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "verificationCodeResent",
					CommonConstants.WHITE_SPACE_CHAR);
		} catch (Exception e) {
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
		}
		setVerifyResend(false);
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
	 * This function runs when signup.xhtml is first opened.
	 */
	public String startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, null, CommonConstants.START);
		if (null != activeUserService.fetchActiveUser()) {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
			return NavigationUtils.buildRedirectionString("/dashboard");
		}
		if ((null == getTmpUser()) || (null == getTmpUser().getEmail())) {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, "signup",
					CommonConstants.END);
			return NavigationUtils.buildRedirectionString("/signup");
		} else {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, null, CommonConstants.END);
			return null;
		}

	}

	public String getVerifyCodeFromTheUser() {
		return verifyCodeFromTheUser;
	}

	public void setVerifyCodeFromTheUser(String verifyCodeFromTheUser) {
		this.verifyCodeFromTheUser = verifyCodeFromTheUser;
	}

	public boolean getResendUsed() {
		return resendUsed;
	}

	public void setResendUsed(boolean resendUsed) {
		this.resendUsed = resendUsed;
	}

	public boolean getVerifyResend() {
		return verifyResend;
	}

	public void setVerifyResend(boolean verifyResend) {
		this.verifyResend = verifyResend;
	}

	public boolean getCorrectVerifyCode() {
		return correctVerifyCode;
	}

	public void setCorrectVerifyCode(boolean correctVerifyCode) {
		this.correctVerifyCode = correctVerifyCode;
	}

	public void refreshVerifyCode() {
		setVerifyCode(CommonFunctions.generateRandomStringBySize(6));
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

	public User getTmpUser() {
		return tmpUser;
	}

	public void setTmpUser(User tmpUser) {
		this.tmpUser = tmpUser;
	}

}
