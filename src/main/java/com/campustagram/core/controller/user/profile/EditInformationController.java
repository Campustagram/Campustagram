package com.campustagram.core.controller.user.profile;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonDate;
import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.security.service.UpdateSecurityContextService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "editinformationController")
@Scope(value = "session")
@Component(value = "editinformationController")
@ELBeanName(value = "editinformationController")
@Join(path = "/profile/editinformation", to = "/pages/core/userManagement/profile/editinformation.jsf")
public class EditInformationController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private Server server;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;
	@Autowired
	private UpdateSecurityContextService updateSecurityContextService;

	private User tmpUser;
	private boolean isEditMode = false;

	private static final String ACTIVE_CLASS_NAME = "EditInformationController";

	public String editUser() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "editUser", null, CommonConstants.START);
		setEditMode(true);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "editUser", null, CommonConstants.END);
		return null;
	}

	public String refresh() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "refresh", null, CommonConstants.START);
		tmpUser = (User) CommonFunctions.deepObjectClone(activeUserService.fetchActiveUser());
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "refresh", null, CommonConstants.END);
		return null;
	}

	public void handleFileUpload(FileUploadEvent event) {
		final String ACTIVE_METHOD_NAME = "handleFileUpload";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		try {
			String imagePath = CommonFunctions.uploadImage(event);
			this.tmpUser.setProfileImageURL(imagePath);
		} catch (IOException e) {
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), "ERROR");
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public String saveUser() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "saveUser", null, CommonConstants.START);
		User tmpSaveUser;
		if (!CommonFunctions.isEmailUsableFormat(tmpUser.getEmail())) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "emailIsNotValid",
					CommonConstants.WHITE_SPACE_CHAR);
		}
		if (null == tmpUser.getName()) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "theNamefieldIsRequired",
					CommonConstants.WHITE_SPACE_CHAR);
		}
		if (null == tmpUser.getLastname()) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "theLastnamefieldIsRequired",
					CommonConstants.WHITE_SPACE_CHAR);
		}
		if (tmpUser.getBirthDate().after(CommonDate.currentDate())) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "birthDateCanNotBeAfterCurrentDate",
					CommonConstants.WHITE_SPACE_CHAR);
		}
		if (CommonFunctions.isEmailUsableFormat(tmpUser.getEmail()) && null != tmpUser.getName()
				&& null != tmpUser.getLastname() && null != tmpUser.getId()
				&& tmpUser.getBirthDate().before(CommonDate.currentDate())) {

			// update
			if (null != (tmpSaveUser = userRepository.findByEmailNotDeleted(this.tmpUser.getEmail()))) {
				// bulunan kullanıcı kendisi (id leri aynı )
				if (tmpSaveUser.getEmail().equals(tmpUser.getEmail())) {
					userRepository.save(tmpUser);
					updateSecurityContextService.updateSecurityContextForEmailChange(tmpUser.getEmail());
					setEditMode(false);
					CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "saveSuccessful",
							CommonConstants.WHITE_SPACE_CHAR);
					server.writeLog("saveSuccessful", CommonConstants.LOG_UPDATE);
				} else {
					// email başkasına ait
					CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "emailAddressAlreadyInUse",
							CommonConstants.WHITE_SPACE_CHAR);
				}
			} else {
				// email ile kullanıcı eşleşmediyse o e-mail kullanılabilir.
				userRepository.save(tmpUser);
				updateSecurityContextService.updateSecurityContextForEmailChange(tmpUser.getEmail());
				setEditMode(false);
				CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "saveSuccessful",
						CommonConstants.WHITE_SPACE_CHAR);
				server.writeLog("saveSuccessful", CommonConstants.LOG_UPDATE);
			}

		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "saveUser", null, CommonConstants.END);
		return null;
	}

	public void cancel() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "cancel", null, CommonConstants.START);
		setEditMode(false);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "cancel", null, CommonConstants.END);
	}

	public String startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, null, CommonConstants.START);
		if (null != activeUserService.fetchActiveUser()) {
			this.tmpUser = (User) CommonFunctions.deepObjectClone(activeUserService.fetchActiveUser());
			setEditMode(false);
			loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, null, CommonConstants.END);
			return null;
		} else {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, CommonConstants.START_UP_CHECKS, "redirect login",
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
}
