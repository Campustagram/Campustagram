package com.campustagram.core.controller.user;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "userController")
@Scope(value = "session")
@Component(value = "userController")
@Join(path = "/userlist", to = "/pages/core/userManagement/userlist.jsf")
public class UserListController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserProfileController userProfileController;
	@Autowired
	private LoggerService loggerService;
	
	private List<User> userList = new ArrayList<>();
	private List<User> filteredUsers = new ArrayList<>();

	private String searchKeyword;
	private User tmpUser;

	private static final String ACTIVE_CLASS_NAME = "UserListController";

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.START);
		userList = userRepository.findAllNotDeleted();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.END);
	}

	@PreAuthorize("hasAuthority('ROLE_USER_MANAGEMENT-PERMISSION_EDIT')")
	public String editUser(User user) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "editUser", null, CommonConstants.START);
		userProfileController.setTmpUser(user);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "editUser", null, CommonConstants.END);
		return NavigationUtils.buildRedirectionString("/userprofileedit");
	}

	public String addUser() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "addUser", null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "addUser", null, CommonConstants.END);
		return userProfileController.addUser();
	}

	@PreAuthorize("hasAuthority('ROLE_USER_MANAGEMENT-PERMISSION_REMOVE')")
	public void deleteUser(User user) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "deleteUser", null, CommonConstants.START);
		userProfileController.deleteUser(user);
		loadData();
		filteredUsers.clear();
		filteredUsers.addAll(userList);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "deleteUser", null, CommonConstants.END);
	}

	public String refresh() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "refresh", null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "refresh", null, CommonConstants.END);
		return null;
	}

	public void blockUser(User user) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "blockUser", null, CommonConstants.START);
		userProfileController.blockUser(user);
		loadData();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "blockUser", null, CommonConstants.END);
	}

	public void unblockUser(User user) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "unblockUser", null, CommonConstants.START);
		userProfileController.unblockUser(user);
		loadData();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "unblockUser", null, CommonConstants.END);
	}

	public void startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);
		searchKeyword = "";
		userProfileController.setTmpUser(null);
		if (!FacesContext.getCurrentInstance().isPostback()) {
			filteredUsers.clear();
			filteredUsers.addAll(userList);
		}
		if (null != userProfileController.getDeletedUser()) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "deleteUserSuccess", CommonConstants.WHITE_SPACE_CHAR);
			userProfileController.setDeletedUser(null);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public List<User> getFilteredUsers() {
		return filteredUsers;
	}

	public void setFilteredUsers(List<User> filteredUsers) {
		this.filteredUsers = filteredUsers;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public User getTmpUser() {
		return tmpUser;
	}

	public void setTmpUser(User tmpUser) {
		this.tmpUser = tmpUser;
	}
}
