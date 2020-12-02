package com.campustagram.core.controller.log;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonDate;
import com.campustagram.core.model.Log;
import com.campustagram.core.persistence.LogRepository;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;

@ManagedBean(name = "logController")
@Scope(value = "session")
@Component(value = "logController")
@Join(path = "/loglist", to = "/pages/core/logMonitoring/loglist.jsf")
public class LogController {
	@Autowired
	private LogRepository logRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;

	private List<Log> logList = new ArrayList<>();
	private List<Log> filteredLogs = new ArrayList<>();

	private String searchKeyword;
	private Integer refreshRate = 60 * 60;

	private static final String ACTIVE_CLASS_NAME = "LogController";

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		final String ACTIVE_METHOD_NAME = "loadData";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		logList = logRepository.findAllNotDeleted();
		userRepository.updateUserLastSeen(activeUserService.fetchActiveUser().getId(), CommonDate.currentDate());
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public void unloadPage() {
		final String ACTIVE_METHOD_NAME = "unloadPage";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		refreshRate = 60 * 60;
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public void refreshSelection(String refresh) {
		final String ACTIVE_METHOD_NAME = "refreshSelection";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		loadData();
		if (refresh.equals("refresh")) {
			// to do
		} else if (refresh.equals("1s")) {
			refreshRate = 1;
		} else if (refresh.equals("5s")) {
			refreshRate = 5;
		} else if (refresh.equals("10s")) {
			refreshRate = 10;
		} else if (refresh.equals("15s")) {
			refreshRate = 15;
		} else if (refresh.equals("manuel")) {
			refreshRate = 60 * 60;
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public void startUpChecks() {
		final String ACTIVE_METHOD_NAME = "startUpChecks";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		if (!FacesContext.getCurrentInstance().isPostback()) {
			filteredLogs.clear();
			filteredLogs.addAll(logList);
		}
		searchKeyword = "";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public LogRepository getLogRepository() {
		return logRepository;
	}

	public void setLogRepository(LogRepository logRepository) {
		this.logRepository = logRepository;
	}

	public List<Log> getLogList() {
		return logList;
	}

	public void setLogList(List<Log> logList) {
		this.logList = logList;
	}

	public List<Log> getFilteredLogs() {
		return filteredLogs;
	}

	public void setFilteredLogs(List<Log> filteredLogs) {
		this.filteredLogs = filteredLogs;
	}

	public Integer getRefreshRate() {
		return refreshRate;
	}

	public void setRefreshRate(Integer refreshRate) {
		this.refreshRate = refreshRate;
	}
}
