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
import com.campustagram.core.model.ResetPassword;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.EmailTemplateRepository;
import com.campustagram.core.persistence.ResetPasswordRepository;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.security.service.BCryptEncoderService;
import com.campustagram.core.security.service.UpdateSecurityContextService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.service.MailSenderService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "createnewpasswordController")
@Scope(value = "session")
@Component(value = "createnewpasswordController")
@Join(path = "/createnewpassword", to = "/pages/core/login/createnewpassword.jsf")
public class CreateNewPasswordController {
	@Autowired
	private Server server;
	@Autowired
	private VerifyCodeController verifyCodeController;
	@Autowired
	private ResetPasswordController resetPasswordController;
	@Autowired
	private ResetPasswordRepository resetPasswordRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MailSenderService mailSenderService;
	@Autowired
	private LoginController loginController;
	@Autowired
	private EmailTemplateRepository emailTemplateRepository;
	@Autowired
	private BCryptEncoderService bCryptEncoderService;
	@Autowired
	private UpdateSecurityContextService updateSecurityContextService;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;

	private User user;

	private String newPassword;
	private String reEnterPassword;

	private static final String ACTIVE_CLASS_NAME = "CreateNewPasswordController";

	/**
	 * If the entered passwords match, the new password is added to the database as
	 * encrypted.
	 * 
	 * @return page_path
	 */
	public String createNewPassword() {
		final String ACTIVE_METHOD_NAME = "createNewPassword";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		if (getNewPassword().equals(getReEnterPassword())) {
			// If the entered passwords are matched.
			try {
				user = (User) CommonFunctions.deepObjectClone(resetPasswordController.getUser());
				ResetPassword resetPassword;
				if (null != (resetPassword = resetPasswordRepository
						.findByUserId(resetPasswordController.getUser().getId())) && (null != user)) {
					// The new password is saved in the database if the user requests a new password
					// and the user is not null.
					user.setPassword(bCryptEncoderService.encode(getNewPassword()));
					userRepository.save(user);
					//updateSecurityContextService.updateSecurityContextForPasswordChange(bCryptEncoderService.encode(user.getPassword()));
					server.writeLogWithUser(user, "theUserChangedThePassword", CommonConstants.LOG_UPDATE);
					server.setPasswordChangedUser(user);
					sendHTMLMailForPasswordChangedNotification(user);
					resetPasswordRepository.delete(resetPassword);
					server.setPasswordChangedUser(user);
					loginController.clearSession();
					loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
					return NavigationUtils.buildRedirectionString("/login");
				} else {
					// User is not valid.
					CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "notValidUser", "whiteSpaceChar");
				}
			} catch (Exception e) {
				// in case of any error.
				// to do
				loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
						com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
			}
		} else {
			// If the entered passwords are not matched.
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "passwordMustMatch", "whiteSpaceChar");
			setNewPassword(null);
			setReEnterPassword(null);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;
	}

	/*public String isOnMaintainceVerifyPage() {
		final String ACTIVE_METHOD_NAME = "isOnMaintainceVerifyPage";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		if ((server.getSystemProperties().isOnMaintenance()) && (server.getSystemProperties().isEndDateActive())
				&& !(roleRightController.checkUserRoleRight(user, "view", CommonConstants.SUPER_ADMIN.intValue()))) {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, "redirect maintenance",
					CommonConstants.END);
			return "/pages/core/maintenance/maintenance.xhtml?faces-redirect=true";
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;
	}*/

	// Use to send HTML e-mails in password change notification
	public void sendHTMLMailForPasswordChangedNotification(User user) throws Exception {
		final String ACTIVE_METHOD_NAME = "sendHTMLMailForPasswordChangedNotification";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME , null,
				CommonConstants.START);
		EmailTemplate emailTemplate = mailSenderService.mailGenerator(user);
		emailTemplate.setSubject(server.getMultiLanguageStringWithKey("passwordChangedNotification"));
		emailTemplate.setContent(server.getMultiLanguageStringWithKey("thanksForVisiting")
				+ server.getMultiLanguageStringWithKey("passwordChanged"));

		emailTemplateRepository.save(emailTemplate);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME , null,
				CommonConstants.END);
	}

	public String startUpChecks() {
		final String ACTIVE_METHOD_NAME = "startUpChecks";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME , null, CommonConstants.START);
		if (null != activeUserService.fetchActiveUser()) {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME , null, CommonConstants.END);
			return NavigationUtils.buildRedirectionString("/dashboard");
		}
		if ((!verifyCodeController.getCorrectVerifyCode()) || (null == resetPasswordController.getUser().getId())) {
			loginController.clearSession();
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME , null, CommonConstants.END);
			return NavigationUtils.buildRedirectionString("/resetpassword");
		} else {
			user = (User) CommonFunctions.deepObjectClone(resetPasswordController.getUser());
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME , null, CommonConstants.END);
		return null;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getReEnterPassword() {
		return reEnterPassword;
	}

	public void setReEnterPassword(String reEnterPassword) {
		this.reEnterPassword = reEnterPassword;
	}
}