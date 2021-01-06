package com.campustagram.app.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "searchController")
@Scope(value = "session")
@Component(value = "searchController")
@Join(path = "/search", to = "/pages/app/search.jsf")
public class SearchController {
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private OtherProfilePageController otherProfilePageController;

	private static final String ACTIVE_CLASS_NAME = "SearchController";

	private User activeUser;
	private List<User> users = new ArrayList<>();
	private List<User> allUsers = new ArrayList<>();

	private String key;

	public void startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);

		activeUser = activeUserService.fetchActiveUser();
		allUsers = userRepository.findAllNotDeleted();
		users = allUsers;
		key = "";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
	}

	public void findUsers() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "findUsers", null, CommonConstants.START);

		users = allUsers.stream().filter(user -> user.getEmail().contains(key) || user.getFullName().contains(key))
				.collect(Collectors.toList());
		Set<User> set = new HashSet<User>(users);
		users = new ArrayList<User>(set);

		loggerService.writeInfo(ACTIVE_CLASS_NAME, "findUsers", null, CommonConstants.START);
	}

	public String goToUserProfilePage(User user) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "goToUserProfilePage", null, CommonConstants.START);

		otherProfilePageController.setUserToView(user);

		loggerService.writeInfo(ACTIVE_CLASS_NAME, "goToUserProfilePage", null, CommonConstants.END);
		return NavigationUtils.buildRedirectionString("/otherprofilepage");
	}

	public User getActiveUser() {
		return activeUser;
	}

	public void setActiveUser(User activeUser) {
		this.activeUser = activeUser;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<User> getAllUsers() {
		return allUsers;
	}

	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}

}
