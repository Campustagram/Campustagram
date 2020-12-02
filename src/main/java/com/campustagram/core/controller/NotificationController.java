package com.campustagram.core.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.model.Notification;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.NotificationRepository;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.enums.PermissionType;
import com.campustagram.core.security.enums.RoleType;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "notificationController")
@Scope(value = "session")
@Component(value = "notificationController")
@Join(path = "/notification", to = "/pages/core/notification/notification.jsf")
public class NotificationController {

	@Autowired
	private Server server;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;
	@Autowired
	private RoleRightController roleRightController;

	private List<Notification> notificationList;
	private List<Notification> unSeenNotificationList;
	private List<User> userList;

	private String searchKeyword;

	private Notification selectedNotification;
	private Notification preSelectedNotification;

	private static final String ACTIVE_CLASS_NAME = "NotificationController";

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		final String ACTIVE_METHOD_NAME = "loadData";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		User activeUser = activeUserService.fetchActiveUser();

		if (null != activeUser) {
			setNotificationList(notificationRepository.findAllNotificationOfUserByID(activeUser.getId()));
			sortListBySeen();
		}

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public String getNotificationTags() {
		final String ACTIVE_METHOD_NAME = "getNotificationTags";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return "<span class=\"topbar-badge animated rubberBand\">" + unSeenNotificationList.size() + "</span>";
	}

	// TODO POSSIBLE_BUG: two loops in this method do the same thing
	public void sortListBySeen() {
		final String ACTIVE_METHOD_NAME = "sortListBySeen";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		List<Notification> notifications = new ArrayList<>();
		for (Notification notification : notificationList) {
			if (!notification.isSeen()) {
				notifications.add(notification);
			}
		}
		for (Notification notification : notificationList) {
			if (notification.isSeen()) {
				notifications.add(notification);
			}
		}
		setNotificationList(notifications);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public void updateAllNotificationLists() {
		final String ACTIVE_METHOD_NAME = "updateAllNotificationLists";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		User activeUser = activeUserService.fetchActiveUser();

		if (null != activeUser) {
			notificationList = notificationRepository.findAllNotificationOfUserByID(activeUser.getId());
			userList = userRepository.findAllNotDeleted();
			unSeenNotificationList = notificationRepository.findUserUnSeenNotificationById(activeUser.getId());
		}
		sortListBySeen();

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public void sendNotification(User processUser, String info, Short status, String content) {
		final String ACTIVE_METHOD_NAME = "sendNotification";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		server.writeLogWithProcessUser(processUser, info, status);

		// 12 ne anlama geliyor?
		if (status != 12) {
			Notification notification = new Notification();
			notification.setUser(processUser);
			notification.setProcessUser(activeUserService.fetchActiveUser());
			notification.setProcessedUser(processUser);
			notification.setInfo(info);
			notification.setStatus(status);
			notification.setContent(content);
			notificationRepository.save(notification);

		}

		for (User informUser : CommonFunctions.safeList(userList)) {
			if (!informUser.getId().equals(processUser.getId()) && informUser.isActive()) {
				if (roleRightController.checkIfUserHasPrivilege(informUser, RoleType.ROLE_NOTIFICATION_MANAGEMENT,
						new HashSet<>(Arrays.asList(PermissionType.PERMISSION_SYSTEM_NOTIFICATION)))) {
					Notification n = new Notification();
					n.setUser(informUser);
					n.setProcessUser(activeUserService.fetchActiveUser());
					n.setProcessedUser(processUser);
					n.setInfo(info);
					n.setStatus(status);
					n.setContent(content);
					notificationRepository.save(n);
				}
			}
		}
		updateAllNotificationLists();

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public void startUpChecks() {
		final String ACTIVE_METHOD_NAME = "startUpChecks";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		searchKeyword = "";
		if (null != preSelectedNotification) {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('detailedNotification').show();");
			setPreSelectedNotification(null);
		}

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public String showSelectedNotification(Notification notification) {
		final String ACTIVE_METHOD_NAME = "showSelectedNotification";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

		setPreSelectedNotification(notification);
		setSelectedNotification(getPreSelectedNotification());
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return NavigationUtils.buildRedirectionString("/notification");
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public List<Notification> getNotificationList() {
		return notificationList;
	}

	public void setNotificationList(List<Notification> notificationList) {
		this.notificationList = notificationList;
	}

	public Notification getSelectedNotification() {
		return selectedNotification;
	}

	public void setSelectedNotification(Notification clickedNotification) {
		clickedNotification.setSeen(true);
		notificationRepository.save(clickedNotification);
		this.selectedNotification = clickedNotification;
	}

	public List<Notification> getUnSeenNotificationList() {
		return unSeenNotificationList;
	}

	public void setUnSeenNotificationList(List<Notification> unSeenNotificationList) {
		this.unSeenNotificationList = unSeenNotificationList;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public Notification getPreSelectedNotification() {
		return preSelectedNotification;
	}

	public void setPreSelectedNotification(Notification preSelectedNotification) {
		this.preSelectedNotification = preSelectedNotification;
	}
}
