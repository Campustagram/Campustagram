package com.campustagram.core.controller;

import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonDate;
import com.campustagram.core.service.LoggerService;

@ManagedBean(name = "maintenanceController")
@Scope(value = "session")
@Component(value = "maintenanceController")
@Join(path = "/maintenance", to = "/pages/core/maintenance/maintenance.jsf")
public class MaintenanceController {

	@Autowired
	private Server server;
	@Autowired
	private LoggerService loggerService;
	
	private static final String ACTIVE_CLASS_NAME = "MaintenanceController";

	public Integer howMuchTimeLeftToBeOnline() {
		final String ACTIVE_METHOD_NAME = "howMuchTimeLeftToBeOnline";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		if (null != server.getSystemProperties().getMaintenanceEndDate()) {
			Long time = Math.round((server.getSystemProperties().getMaintenanceEndDate().getTime()
					- CommonDate.currentDate().getTime()) / ((double) CommonConstants.MILLIS_PER_SECOND));
			if (time <= 0) {
				loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, "maintenance date finished = 0",
						CommonConstants.END);
				return 0;
			}
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, time.toString(), CommonConstants.END);
			return time.intValue();
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, "maintenance false 0", CommonConstants.END);
		return 0;
	}

	public boolean isMaintenanceDateExpired() {
		final String ACTIVE_METHOD_NAME = "isMaintenanceDateExpired";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		if (null != server.getSystemProperties() && null != server.getSystemProperties().getMaintenanceEndDate()) {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
			return (server.getSystemProperties().isOnMaintenance()
					&& server.getSystemProperties().getMaintenanceEndDate().before(CommonDate.currentDate()));
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return false;
	}

	public boolean isOnMaintenanceDateAndNotExpired() {
		final String ACTIVE_METHOD_NAME = "isOnMaintenanceDateAndNotExpired";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		if (null != server.getSystemProperties() && null != server.getSystemProperties().getMaintenanceStartDate()
				&& null != server.getSystemProperties().getMaintenanceEndDate()) {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
			return (server.getSystemProperties().isOnMaintenance()
					&& server.getSystemProperties().getMaintenanceEndDate().after(CommonDate.currentDate()));
		} else {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
			return false;
		}
	}

	public void startupChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startupChecks", null, CommonConstants.START);
		server.createPageVisitLog(null, "pageEntry", CommonConstants.LOG_INFO);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startupChecks", null, CommonConstants.END);
	}

}
