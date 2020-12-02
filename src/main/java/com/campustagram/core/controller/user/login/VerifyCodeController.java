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
import com.campustagram.core.controller.RoleRightController;
import com.campustagram.core.model.ResetPassword;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.ResetPasswordRepository;
import com.campustagram.core.security.enums.RoleType;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "verifycodeController")
@Scope(value = "session")
@Component(value = "verifycodeController")
@Join(path = "/verifycode", to = "/pages/core/login/verifycode.jsf")
public class VerifyCodeController {
	@Autowired
	private Server server;
	@Autowired
	private ResetPasswordController resetPasswordController;
	@Autowired
	private ResetPasswordRepository resetPasswordRepository;
	@Autowired
	private LoginController loginController;
	@Autowired
	private RoleRightController roleRightController;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;
	
	private String verifyCodeFromTheUser;

	private boolean correctVerifyCode = false;
	private boolean resendUsed = false;
	private boolean verifyResend = true;

	private static final String ACTIVE_CLASS_NAME = "VerifyCodeController";

	/**
	 * If the entered code match, return creteNewPassword page
	 * 
	 * @return page_path
	 */
	public String checkCode() {
		final String ACTIVE_METHOD_NAME = "checkCode";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

		ResetPassword rp;
		if (resetPasswordController.getResetPassword().getHash()
				.equals(CommonCryptographicHash.encryptChar(getVerifyCodeFromTheUser().toCharArray()))
				&& (null != (rp = resetPasswordRepository
						.findByHash(CommonCryptographicHash.encryptChar(getVerifyCodeFromTheUser().toCharArray()))))) {
			// the entered code match
			if (CommonDate.computeDiffBetween2Minutes(rp.getCreateDate(), CommonDate.currentDate()) > 5) {
				// Time past reset code.
				server.writeLogWithUser(rp.getUser(), "resetPasswordCodeTimeOver", CommonConstants.LOG_INFO);
				resetPasswordRepository.delete(rp);
				resetPasswordController.setTimeOver(true);
				resetPasswordController.setCounterOver(false);
				setCorrectVerifyCode(false);
				loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, "resetPasswordCodeTimeOver",
						CommonConstants.START);
				return NavigationUtils.buildRedirectionString("/resetpassword");
			} else {
				resetPasswordController.setTimeOver(false);
				resetPasswordController.setCounterOver(false);
				setCorrectVerifyCode(true);
				loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
				return NavigationUtils.buildRedirectionString("/createnewpassword");
			}

		} else {
			// the entered code not match
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "invalidCheckCode", "whiteSpaceChar");
			ResetPassword resetPassword;

			if (null != (resetPassword = resetPasswordRepository
					.findByUserId(resetPasswordController.getUser().getId()))) {
				resetPassword.setCounter(resetPassword.getCounter() + 1);
				server.writeLogWithUser(resetPassword.getUser(), "wrongResetPasswordCodeEntered",
						CommonConstants.LOG_WARNING);
				if (resetPassword.getCounter() >= 3) {
					server.writeLogWithUser(resetPassword.getUser(), "resetPasswordCodeDeleted",
							CommonConstants.LOG_DELETE);
					resetPasswordRepository.delete(resetPassword);
					resetPassword = null;
					resetPasswordController.setUser(new User());
					resetPasswordController.setCounterOver(true);
					resetPasswordController.setTimeOver(false);
					loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, "wrongResetPasswordCodeEntered",
							CommonConstants.END);
					return NavigationUtils.buildRedirectionString("/resetpassword");
				} else {
					resetPasswordRepository.save(resetPassword);
				}
			}
			setCorrectVerifyCode(false);
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, "the entered code not match",
					CommonConstants.END);
			return null;
		}
	}

	/**
	 * This function work when you click on the "resend" button. It sends the code
	 * again.
	 */
	public void resendClicked() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "resendClicked", null, CommonConstants.START);
		setResendUsed(true);
		setVerifyCodeFromTheUser(null);
		resetPasswordController.resendCode(resetPasswordController.getResetPassword());
		setVerifyResend(false);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "resendClicked", null, CommonConstants.END);
	}

	/*public String isOnMaintainceVerifyPage() {
		final String ACTIVE_METHOD_NAME = "isOnMaintainceVerifyPage";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		if ((server.getSystemProperties().isOnMaintenance()) && (server.getSystemProperties().isEndDateActive())
				&& !(roleRightController.checkIfUserHasRoleType(resetPasswordController.getTmpUser() , RoleType.ROLE_SYSTEM_MANAGEMENT))) {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, "redirect maintenance",
					CommonConstants.END);
			return "/pages/core/maintenance/maintenance.xhtml?faces-redirect=true";
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;
	}*/

	/**
	 * This function runs when resetpassword.xhtml is first opened.
	 */
	public String startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, null, CommonConstants.START);
		if (null != activeUserService.fetchActiveUser()) {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
			return NavigationUtils.buildRedirectionString("/dashboard");
		}
		if (null == resetPasswordController.getUser().getEmail()) {
			loginController.clearSession();
			loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, "redirectResetpassword",
					CommonConstants.END);
			return NavigationUtils.buildRedirectionString("/resetpassword");
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

}
