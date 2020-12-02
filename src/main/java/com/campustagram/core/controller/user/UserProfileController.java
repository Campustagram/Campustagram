package com.campustagram.core.controller.user;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonDate;
import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.controller.LocaleController;
import com.campustagram.core.controller.NotificationController;
import com.campustagram.core.model.RememberMe;
import com.campustagram.core.model.Role;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.RememberMeRepository;
import com.campustagram.core.persistence.RoleRepository;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.security.service.BCryptEncoderService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "userprofileController")
@Scope(value = "session")
@Component(value = "userprofileController")
@Join(path = "/adduserprofile", to = "/pages/core/userManagement/adduserprofile.jsf")
public class UserProfileController {
	@Autowired
	private Server server;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private NotificationController notificationController;
	@Autowired
	private RememberMeRepository rememberMeRepository;
	@Autowired
	private LocaleController localeController;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private BCryptEncoderService bCryptEncoderService;
	@Autowired
	private ActiveUserService activeUserService;

	private User deletedUser = null;
	private User tmpUser;

	private Set<Role> allRoles = new HashSet<>();
	private Set<String> allRoleStrings = new HashSet<>();

	private String selectedRoleName;

	private String password = null;
	private String tempPassword = null;

	private String selectedTmpLanguage;

	private UploadedFile file;

	private static final String ACTIVE_CLASS_NAME = "UserProfileController";

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.START);
		setAllRoles(new HashSet<>(roleRepository.findAllNotDeleted()));
		setAllRoleStrings(getAllRoles().stream().map(Role::getName).collect(Collectors.toSet()));
		Role defaultRole = server.getSystemProperties().getDefaultNewUserRole();

		/*
		 * TODO (Ahmet Şen will decide this)
		 * 
		 */
		if (null == defaultRole) {
			setSelectedRoleName(CommonConstants.EMPTY_STRING);
		} else {
			setSelectedRoleName(defaultRole.getName());
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.END);
	}

	public String editUser() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "editUser", null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "userEdited", null, CommonConstants.END);
		return null;
	}

	public String addUser() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "addUser", null, CommonConstants.START);
		setTmpUser(null);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "addUser", null, CommonConstants.END);
		return NavigationUtils.buildRedirectionString("/adduserprofile");
	}

	public String deleteUser(User user) {
		final String ACTIVE_METHOD_NAME = "deleteUser";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		try {
			if (!activeUserService.fetchActiveUser().getId().equals((user.getId()))) {
				user.setDeleted(true);
				userRepository.save(user);
				for (RememberMe rememberMe : rememberMeRepository.findByUserId(user.getId())) {
					rememberMeRepository.delete(rememberMe);
				}
				notificationController.sendNotification(user, "accountIsDeleted", CommonConstants.LOG_DELETE, null);
				loadData();
				setTmpUser(null);
				CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "deleteUserSuccess",
						CommonConstants.WHITE_SPACE_CHAR);
				setDeletedUser(user);
				loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
				return NavigationUtils.buildRedirectionString("/userlist");
			} else {
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "notDeleteYourself",
						CommonConstants.WHITE_SPACE_CHAR);
				loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, "notDeleteYourself",
						CommonConstants.END);
				return null;
			}
		} catch (Exception e) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "deleteUserUnsuccess",
					CommonConstants.WHITE_SPACE_CHAR);
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, CommonFunctions.getExceptionAsString(e),
					CommonConstants.ERROR);
			return null;
		}

	}

	public String refresh() {
		final String ACTIVE_METHOD_NAME = "refresh";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		try {
			tmpUser = (User) userRepository.findById(tmpUser.getId()).get();
		} catch (NoSuchElementException e) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "refreshUnsuccess",
					CommonConstants.WHITE_SPACE_CHAR);
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, CommonFunctions.getExceptionAsString(e),
					CommonConstants.ERROR);
		}

		selectedTmpLanguage = tmpUser.getLanguage().getCode();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;
	}

	public String blockUser(User user) {
		final String ACTIVE_METHOD_NAME = "blockUser";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		try {
			if (!activeUserService.fetchActiveUser().getId().equals((user.getId()))) {
				user.setActive(false);
				userRepository.save(user);
				notificationController.sendNotification(user, "accountIsBlocked", CommonConstants.LOG_BLOCK, null);
				loadData();
				CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "blockingSuccessful",
						CommonConstants.WHITE_SPACE_CHAR);
			} else {
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "notBlockYourself",
						CommonConstants.WHITE_SPACE_CHAR);
			}
		} catch (Exception e) {
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "blockingUnsuccessful",
					CommonConstants.WHITE_SPACE_CHAR);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;
	}

	public String unblockUser(User user) {
		final String ACTIVE_METHOD_NAME = "unblockUser";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		try {
			user.setActive(true);
			userRepository.save(user);
			notificationController.sendNotification(user, "accountIsUnblocked", CommonConstants.LOG_UNBLOCK, null);
			loadData();
			CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "unblockingSuccessful",
					CommonConstants.WHITE_SPACE_CHAR);
		} catch (Exception e) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "unblockingUnsuccessful",
					CommonConstants.WHITE_SPACE_CHAR);
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;
	}

	public String saveUser() {
		final String ACTIVE_METHOD_NAME = "saveUser";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
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

		if (null == tmpUser.getId()) {
			if (!getPassword().equals(getTempPassword())) {
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "passwordsAreNotSame",
						CommonConstants.WHITE_SPACE_CHAR);
			}
			if (null != userRepository.findByEmailNotDeleted(this.tmpUser.getEmail())) {
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "emailAddressAlreadyInUse",
						CommonConstants.WHITE_SPACE_CHAR);
			}
		}
		if (null != tmpUser.getBirthDate()) {
			if (tmpUser.getBirthDate().after(CommonDate.currentDate())) {
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "birthDateCanNotBeAfterCurrentDate",
						CommonConstants.WHITE_SPACE_CHAR);
			}
		}
		if (CommonFunctions.isEmailUsableFormat(tmpUser.getEmail()) && null != tmpUser.getName()
				&& null != tmpUser.getLastname()
				&& (null == tmpUser.getBirthDate() || tmpUser.getBirthDate().before(CommonDate.currentDate()))) {
			tmpUser.setLanguage(server.getLanguageAsObject(selectedTmpLanguage));
			if (null != tmpUser.getId()) {
				// update
				if (null != (tmpSaveUser = userRepository.findByEmailNotDeleted(this.tmpUser.getEmail()))) {
					// bulunan kullanıcı kendisi (id leri aynı )
					if (tmpSaveUser.getId().equals(tmpUser.getId())) {
						assignUserRole();
						userRepository.save(tmpUser);

						notificationController.sendNotification(tmpUser, "accountIsUpdated", CommonConstants.LOG_UPDATE,
								null);
						CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "saveSuccessful",
								CommonConstants.WHITE_SPACE_CHAR);
					} else {
						// email başkasına ait
						CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "emailAddressAlreadyInUse",
								CommonConstants.WHITE_SPACE_CHAR);
					}
				} else {
					// email ile kullanıcı eşleşmediyse o e-mail kullanılabilir.
					assignUserRole();
					userRepository.save(tmpUser);
					notificationController.sendNotification(tmpUser, "accountIsUpdated", CommonConstants.LOG_UPDATE,
							null);
					CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "saveSuccessful",
							CommonConstants.WHITE_SPACE_CHAR);
				}
			} else {
				// insert
				if (getPassword().equals(getTempPassword())
						&& (null == userRepository.findByEmailNotDeleted(this.tmpUser.getEmail()))) {
					tmpUser.setPassword(bCryptEncoderService.encode(password));
					tmpUser.setRole(roleRepository.findByNameNotDeleted(getSelectedRoleName()));
					userRepository.save(tmpUser);
					if (null != (tmpSaveUser = userRepository.findByEmailNotDeleted(this.tmpUser.getEmail()))) {
						tmpUser = (User) CommonFunctions.deepObjectClone(tmpSaveUser);
					}
					notificationController.sendNotification(tmpUser, "accountIsCreated", CommonConstants.LOG_CREATE,
							null);

					CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "saveSuccessful",
							CommonConstants.WHITE_SPACE_CHAR);
					return NavigationUtils.buildRedirectionString("/userprofileedit");
				} else {
					CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "saveUnsuccessful",
							CommonConstants.WHITE_SPACE_CHAR);
				}
			}

		} else {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "saveUnsuccessful",
					CommonConstants.WHITE_SPACE_CHAR);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;
	}

	private void assignUserRole() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "assignUserRole", null, CommonConstants.START);
		getTmpUser().setRole(roleRepository.findByNameNotDeleted(getSelectedRoleName()));
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "assignUserRole", null, CommonConstants.END);
	}

	public String reset() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "reset", null, CommonConstants.START);
		setTmpUser(new User());
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "reset", null, CommonConstants.END);
		return null;
	}

	public void cancel() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "cancel", null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "cancel", null, CommonConstants.END);
	}

	public String successfullyDeleted() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "successfullyDeleted", null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "successfullyDeleted", null, CommonConstants.END);
		return NavigationUtils.buildRedirectionString("/userlist");
	}

	public String startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);
		if (null == this.tmpUser || null == this.tmpUser.getId()) {
			tmpUser = new User();
			setSelectedRoleName(CommonConstants.EMPTY_STRING);
		} else {
			Role defaultRole = server.getSystemProperties().getDefaultNewUserRole();

			if (null != getTmpUser().getRole()) {
				setSelectedRoleName(getTmpUser().getRole().getName());
			} else if (null == defaultRole) {
				setSelectedRoleName(CommonConstants.EMPTY_STRING);
			} else {
				setSelectedRoleName(defaultRole.getName());
			}

			System.err.println(" ---------------------  " + getSelectedRoleName());
		}

		if (null != tmpUser.getLanguage()) {
			selectedTmpLanguage = tmpUser.getLanguage().getCode();
		} else {
			selectedTmpLanguage = localeController.getSelectedTmpLanguage();
		}

		setAllRoles(new HashSet<>(roleRepository.findAllNotDeleted()));
		setAllRoleStrings(getAllRoles().stream().map(Role::getName).collect(Collectors.toSet()));

		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
		return null;
	}

	public User getDeletedUser() {
		return deletedUser;
	}

	public void setDeletedUser(User deletedUser) {
		this.deletedUser = deletedUser;
	}

	public User getTmpUser() {
		return tmpUser;
	}

	public void setTmpUser(User tmpUser) {
		this.tmpUser = tmpUser;
	}

	public String getTempPassword() {
		return tempPassword;
	}

	public void setTempPassword(String tempPassword) {
		this.tempPassword = tempPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public String getSelectedTmpLanguage() {
		return selectedTmpLanguage;
	}

	public void setSelectedTmpLanguage(String selectedTmpLanguage) {
		this.selectedTmpLanguage = selectedTmpLanguage;
	}

	public Set<Role> getAllRoles() {
		return allRoles;
	}

	public void setAllRoles(Set<Role> allRoles) {
		this.allRoles = allRoles;
	}

	public Set<String> getAllRoleStrings() {
		return allRoleStrings;
	}

	public void setAllRoleStrings(Set<String> allRoleStrings) {
		this.allRoleStrings = allRoleStrings;
	}

	public String getSelectedRoleName() {
		if (null == selectedRoleName || selectedRoleName.equalsIgnoreCase("")) {
			selectedRoleName = server.getSystemProperties().getDefaultNewUserRole().getName();
		}
		return selectedRoleName;
	}

	public void setSelectedRoleName(String selectedRoleName) {
		this.selectedRoleName = selectedRoleName;
	}
}
