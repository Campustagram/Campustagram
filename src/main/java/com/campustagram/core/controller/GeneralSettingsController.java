package com.campustagram.core.controller;

import java.text.SimpleDateFormat;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonDate;
import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.model.SystemProperties;
import com.campustagram.core.persistence.SystemPropertiesRepository;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;

@ManagedBean(name = "generalsettingsController")
@Scope(value = "session")
@Component(value = "generalsettingsController")
@ELBeanName(value = "generalsettingsController")
@Join(path = "/system/settings/generalsettings", to = "/pages/core/systemManagement/systemConfiguration/generalsettings.jsf")
public class GeneralSettingsController {

	@Autowired
	private Server server;
	@Autowired
	private SystemPropertiesRepository systemPropertiesRepository;
	@Autowired
	private NotificationController notificationController;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;

	private SystemProperties systemProperties;

	private static final String ACTIVE_CLASS_NAME = "GeneralSettingsController";

	public String refresh() {
		final String ACTIVE_METHOD_NAME = "refresh";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;
	}

	public String save() {
		final String ACTIVE_METHOD_NAME = "save";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

		systemPropertiesRepository.save(systemProperties);
		server.init();

		try {
			systemPropertiesRepository.save(systemProperties);
			server.init();
			refresh();
			notificationController.sendNotification(activeUserService.fetchActiveUser(),
					"generalSystemSettingsIsUpdated", CommonConstants.LOG_UPDATE, null);
			CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "saveSuccessful", CommonConstants.WHITE_SPACE_CHAR);
			server.writeLogWithUser(activeUserService.fetchActiveUser(), "systemGeneralSettingsUpdateSuccessful",
					CommonConstants.LOG_INFO);
		} catch (Exception e) {
			server.writeLogWithUser(activeUserService.fetchActiveUser(), "systemGeneralSettingsUpdateUnsuccessful",
					CommonConstants.LOG_WARNING);
			startUpChecks();
		}

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;
	}

	public String saveMaintenanceDate() {
		final String ACTIVE_METHOD_NAME = "saveMaintenanceDate";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		if (!this.getSystemProperties().isOnMaintenance()) {
			server.getSystemProperties().setOnMaintenance(false);
			server.getSystemProperties().setMaintenanceStartDate(null);
			server.getSystemProperties().setMaintenanceEndDate(null);
			server.getSystemProperties().setUpdateUser(activeUserService.fetchActiveUser());
			server.getSystemProperties().setUpdateDate(CommonDate.currentDate());
			systemPropertiesRepository.save(server.getSystemProperties());
			notificationController.sendNotification(activeUserService.fetchActiveUser(), "finishMaintenance",
					CommonConstants.LOG_UPDATE, null);
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
			CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "saveSuccessful", CommonConstants.WHITE_SPACE_CHAR);
			this.systemProperties = (SystemProperties) CommonFunctions.deepObjectClone(server.getSystemProperties());
			return null;

		} else {
			if (this.getSystemProperties().getMaintenanceStartDate() == null) {
				server.getSystemProperties().setOnMaintenance(false);
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "startDateShouldBeSelected",
						CommonConstants.WHITE_SPACE_CHAR);
			}
			if (this.getSystemProperties().getMaintenanceEndDate() == null) {
				server.getSystemProperties().setOnMaintenance(false);
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, "endDateShouldBeSelected",
						CommonConstants.WHITE_SPACE_CHAR);
			}
			if (null != this.getSystemProperties().getMaintenanceStartDate()
					&& null != this.getSystemProperties().getMaintenanceEndDate()) {
				if (CommonDate.computeDiffBetween2Second(this.getSystemProperties().getMaintenanceStartDate(),
						this.getSystemProperties().getMaintenanceEndDate()) > 0) {
					server.getSystemProperties().setOnMaintenance(false);
					CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR,
							"maintenanceStartDateCanNotBeAfterMaintenanceEndDate", CommonConstants.WHITE_SPACE_CHAR);
				}
				if (CommonDate.computeDiffBetween2Second(this.getSystemProperties().getMaintenanceStartDate(),
						CommonDate.currentDate()) < 0) {
					server.getSystemProperties().setOnMaintenance(false);
					CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR,
							"maintenanceStartDateCanNotBeBeforeCurrentDate", CommonConstants.WHITE_SPACE_CHAR);
				}
				if (CommonDate.computeDiffBetween2Second(this.getSystemProperties().getMaintenanceEndDate(),
						CommonDate.currentDate()) < 0) {
					server.getSystemProperties().setOnMaintenance(false);
					CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR,
							"maintenanceEndDateCanNotBeBeforeCurrentDate", CommonConstants.WHITE_SPACE_CHAR);
				}

				if ((null != this.getSystemProperties().getMaintenanceStartDate())
						&& (null != this.getSystemProperties().getMaintenanceEndDate())
						&& (CommonDate.computeDiffBetween2Second(this.getSystemProperties().getMaintenanceStartDate(),
								this.getSystemProperties().getMaintenanceEndDate()) <= 0)
						&& (CommonDate.computeDiffBetween2Second(this.getSystemProperties().getMaintenanceStartDate(),
								CommonDate.currentDate()) >= 0)
						&& (CommonDate.computeDiffBetween2Second(this.getSystemProperties().getMaintenanceEndDate(),
								CommonDate.currentDate()) >= 0)) {
					server.getSystemProperties().setUpdateUser(activeUserService.fetchActiveUser());
					server.getSystemProperties().setOnMaintenance(true);
					server.getSystemProperties()
							.setMaintenanceStartDate(this.getSystemProperties().getMaintenanceStartDate());
					server.getSystemProperties()
							.setMaintenanceEndDate(this.getSystemProperties().getMaintenanceEndDate());
					server.getSystemProperties().setUpdateDate(CommonDate.currentDate());
					systemPropertiesRepository.save(server.getSystemProperties());

					notificationController.sendNotification(activeUserService.fetchActiveUser(), "startMaintenance",
							CommonConstants.LOG_UPDATE, new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
									.format(server.getSystemProperties().getMaintenanceEndDate()));
					CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "saveSuccessful",
							CommonConstants.WHITE_SPACE_CHAR);
				}
			}
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;

	}

	public void startUpChecks() {
		final String ACTIVE_METHOD_NAME = "startUpChecks";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		systemProperties = (SystemProperties) CommonFunctions.deepObjectClone(server.getSystemProperties());
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public SystemProperties getSystemProperties() {
		return systemProperties;
	}

	public void setSystemProperties(SystemProperties systemProperties) {
		this.systemProperties = systemProperties;
	}

}