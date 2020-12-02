package com.campustagram.core.controller;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.mail.MessagingException;

import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.model.EmailTemplate;
import com.campustagram.core.model.MailSenderProperties;
import com.campustagram.core.model.SystemProperties;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.SystemPropertiesRepository;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.service.MailSenderService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "systemEmailController")
@Scope(value = "session")
@Component(value = "systemEmailController")
@Join(path = "/system/settings/systememail", to = "/pages/core/systemManagement/emailConfiguration/systememail.jsf")
public class SystemEmailController {

	@Autowired
	private SystemPropertiesRepository systemPropertiesRepository;
	@Autowired
	private MailSenderService mailSenderService;
	@Autowired
	private NotificationController notificationController;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;
	
	private SystemProperties systemProperties;
	private String tmpVerificationCode;
	private String verificationCode;

	@Autowired
	private Server server;

	private boolean isEditMode = false;

	private static final String ACTIVE_CLASS_NAME = "SystemEmailController";

	public String editSystemEmail() {
		final String ACTIVE_METHOD_NAME = "editSystemEmail";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		setEditMode(true);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;
	}

	public void cancel() {
		final String ACTIVE_METHOD_NAME = "cancel";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		setEditMode(false);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public void refresh() {
		final String ACTIVE_METHOD_NAME = "refresh";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		systemProperties = (SystemProperties) CommonFunctions.deepObjectClone(server.getSystemProperties());
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	@PostConstruct
	public void init() {
		final String ACTIVE_METHOD_NAME = "init";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public String save() {
		final String ACTIVE_METHOD_NAME = "save";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		if (!CommonFunctions.isEmailUsableFormat(systemProperties.getMailSenderProperties().getUsername())) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "emailIsNotValid",
					CommonConstants.WHITE_SPACE_CHAR);
		}
		if (null == systemProperties.getMailSenderProperties().getPassword()) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "thePasswordfieldIsRequired",
					CommonConstants.WHITE_SPACE_CHAR);
		}
		if (null == systemProperties.getMailSenderProperties().getPort()) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "theEmailportfieldIsRequired",
					CommonConstants.WHITE_SPACE_CHAR);
		}
		if (null == systemProperties.getMailSenderProperties().getHost()) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "theEmailhostfieldIsRequired",
					CommonConstants.WHITE_SPACE_CHAR);
		}

		if (CommonFunctions.isEmailUsableFormat(systemProperties.getMailSenderProperties().getUsername())
				&& null != systemProperties.getMailSenderProperties().getHost()
				&& null != systemProperties.getMailSenderProperties().getPassword()
				&& null != systemProperties.getMailSenderProperties().getPort()) {
			if (verificationCode.equals(tmpVerificationCode)) {
				try {
					MailSenderProperties mailSenderProperties = new MailSenderProperties();
					mailSenderProperties.setUsername(systemProperties.getMailSenderProperties().getUsername());
					mailSenderProperties.setPassword(systemProperties.getMailSenderProperties().getPassword());
					mailSenderProperties.setHost(systemProperties.getMailSenderProperties().getHost());
					mailSenderProperties.setPort(systemProperties.getMailSenderProperties().getPort());
					mailSenderProperties.setTransportProtocol("smtp");

					sendHTMLMailForSystemEmailChangeNotification(mailSenderProperties, activeUserService.fetchActiveUser());
					setEditMode(false);
					CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "updateSuccessful",
							CommonConstants.WHITE_SPACE_CHAR);
					server.writeLogWithUser(activeUserService.fetchActiveUser(), "systemEmailUpdated", CommonConstants.LOG_INFO);
					systemPropertiesRepository.save(systemProperties);
					notificationController.sendNotification(activeUserService.fetchActiveUser(), "systemEmailUpdated",
							CommonConstants.LOG_UPDATE, null);
					server.init();
				} catch (MessagingException e) {
					setEditMode(false);
					startUpChecks();
					CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "updateUnsuccessful",
							CommonConstants.WHITE_SPACE_CHAR);
					server.writeLogWithUser(activeUserService.fetchActiveUser(), "systemEmailUpdateUnsuccessful",
							CommonConstants.LOG_WARNING);
					loggerService.writeError(ACTIVE_CLASS_NAME, "verificationCodeIsIncorrect",
							com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), "ERROR");
				}
			} else {
				setEditMode(false);
				startUpChecks();
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "verificationCodeIsIncorrect",
						CommonConstants.WHITE_SPACE_CHAR);
				server.writeLogWithUser(activeUserService.fetchActiveUser(),
						"invalidVerificationCodeWasEnteredDuringSystemEmailChange", CommonConstants.LOG_WARNING);
			}

		} else {
			setEditMode(false);
			startUpChecks();
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "updateUnsuccessful",
					CommonConstants.WHITE_SPACE_CHAR);
			server.writeLogWithUser(activeUserService.fetchActiveUser(), "systemEmailUpdateUnsuccessful",
					CommonConstants.LOG_WARNING);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;
	}

	public void testAndSave() {
		final String ACTIVE_METHOD_NAME = "testAndSave";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		setTmpVerificationCode(null);
		MailSenderProperties mailSenderProperties = new MailSenderProperties();
		mailSenderProperties.setUsername(systemProperties.getMailSenderProperties().getUsername());
		mailSenderProperties.setPassword(systemProperties.getMailSenderProperties().getPassword());
		mailSenderProperties.setHost(systemProperties.getMailSenderProperties().getHost());
		mailSenderProperties.setPort(systemProperties.getMailSenderProperties().getPort());
		mailSenderProperties.setTransportProtocol("smtp");

		EmailTemplate emailTemplate = new EmailTemplate();
		emailTemplate.setFrom(systemProperties.getMailSenderProperties().getUsername());
		emailTemplate.setTo(systemProperties.getMailSenderProperties().getUsername());

		try {
			setVerificationCode(CommonFunctions.generateRandomStringBySize(6));
			sendVerifyCodeWithHTMLMailWhenSystemEmailIsChanged(mailSenderProperties, emailTemplate,
					getVerificationCode());
		} catch (Exception e) {
			setEditMode(false);
			startUpChecks();
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "failedToSendEmail",
					CommonConstants.WHITE_SPACE_CHAR);
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), "ERROR");
		}

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	// Use it to send HTML emails for verification code
	public void sendVerifyCodeWithHTMLMailWhenSystemEmailIsChanged(MailSenderProperties mailSenderProperties,
			EmailTemplate emailTemplate, String code) throws MessagingException {
		final String ACTIVE_METHOD_NAME = "sendVerifyCodeWithHTMLMailWhenSystemEmailIsChanged";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null,
				CommonConstants.START);
		try {

			emailTemplate.setSubject(server.getMultiLanguageStringWithKey("verifyCode"));
			emailTemplate.setContent("<p>" + server.getMultiLanguageStringWithKey("hello") + " "
					+ activeUserService.fetchActiveUser().getName() + "</p>" + "<p>"
					+ server.getMultiLanguageStringWithKey("verificationCodeToUpdateSystemEmailAddress")
					+ " :  <strong>" + code + "</strong></p>");
			loggerService.writeInfo(ACTIVE_CLASS_NAME, "verificationCodeIsIncorrect", "Reset Code: " + code,
					"INFO");
			mailSenderService.sendTestMail(mailSenderService.createTestMailSender(mailSenderProperties), emailTemplate);
		} catch (Exception e) {
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null,
				CommonConstants.END);
	}

	// Use to send HTML emails in system email change notification
	public void sendHTMLMailForSystemEmailChangeNotification(MailSenderProperties mailSenderProperties, User user)
			throws MessagingException {
		final String ACTIVE_METHOD_NAME = "sendHTMLMailForSystemEmailChangeNotification";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null,
				CommonConstants.START);
		try {
			user.setEmail(systemProperties.getMailSenderProperties().getUsername());
			EmailTemplate emailTemplate = mailSenderService.mailGenerator(user);
			emailTemplate.setSubject(server.getMultiLanguageStringWithKey("systemEmailChangedNotification"));
			emailTemplate.setContent("<strong>" + server.getMultiLanguageStringWithKey("criticalUpdate") + "</strong>"
					+ "<p>" + server.getMultiLanguageStringWithKey("systemEmailChanged") + "</p>" + "<p>"
					+ server.getMultiLanguageStringWithKey("thanksForVisiting") + "</p>");

			mailSenderService.sendTestMail(mailSenderService.createTestMailSender(mailSenderProperties), emailTemplate);
		} catch (Exception e) {
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
		}

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null,
				CommonConstants.END);
	}

	public String startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, null, CommonConstants.START);
		if (null != activeUserService.fetchActiveUser()) {
			systemProperties = (SystemProperties) CommonFunctions.deepObjectClone(server.getSystemProperties());
			setEditMode(false);
			loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, null, CommonConstants.END);
			return null;
		} else {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, "Redirect Login",
					CommonConstants.END);
			return NavigationUtils.buildRedirectionString("/login");
		}
	}

	public SystemPropertiesRepository getSystemPropertiesRepository() {
		return systemPropertiesRepository;
	}

	public void setSystemPropertiesRepository(SystemPropertiesRepository systemPropertiesRepository) {
		this.systemPropertiesRepository = systemPropertiesRepository;
	}

	public SystemProperties getSystemProperties() {
		return systemProperties;
	}

	public void setSystemProperties(SystemProperties systemProperties) {
		this.systemProperties = systemProperties;
	}

	public boolean isEditMode() {
		return isEditMode;
	}

	public void setEditMode(boolean isEditMode) {
		this.isEditMode = isEditMode;
	}

	public String getTmpVerificationCode() {
		return tmpVerificationCode;
	}

	public void setTmpVerificationCode(String tmpVerificationCode) {
		this.tmpVerificationCode = tmpVerificationCode;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
		setTmpVerificationCode(null);
	}
}