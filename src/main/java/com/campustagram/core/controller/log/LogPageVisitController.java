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
import com.campustagram.core.model.LogPageVisit;
import com.campustagram.core.persistence.LogPageVisitRepository;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;

@ManagedBean(name = "logPageVisitController")
@Scope(value = "session")
@Component(value = "logPageVisitController")
@Join(path = "/pagevisitloglist", to = "/pages/core/logMonitoring/pagevisitloglist.jsf")
public class LogPageVisitController {
	@Autowired
	private LogPageVisitRepository logPageVisitRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;

	private List<LogPageVisit> logPageVisitList = new ArrayList<>();
	private List<LogPageVisit> filteredLogPageVisit = new ArrayList<>();

	private String searchKeyword;

	private Integer refreshRate = 60 * 60;

	private static final String ACTIVE_CLASS_NAME = "LogPageVisitController";

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.START);
		logPageVisitList = logPageVisitRepository.findAllNotDeleted();
		userRepository.updateUserLastSeen(activeUserService.fetchActiveUser().getId(), CommonDate.currentDate());
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.END);
	}

	public void unloadPage() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "unloadPage", null, CommonConstants.START);
		refreshRate = 60 * 60;
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "unloadPage", null, CommonConstants.END);
	}

	public void refreshSelection(String refresh) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "refreshSelection", null, CommonConstants.START);
		loadData();
		if (refresh.equals("refresh")) {

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
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "refreshSelection", null, CommonConstants.END);
	}

	public void startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);
		searchKeyword = null;
		if (!FacesContext.getCurrentInstance().isPostback()) {
			filteredLogPageVisit.clear();
			filteredLogPageVisit.addAll(logPageVisitList);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
	}

	public List<LogPageVisit> getLogPageVisitList() {
		return logPageVisitList;
	}

	public void setLogPageVisitList(List<LogPageVisit> logPageVisitList) {
		this.logPageVisitList = logPageVisitList;
	}

	public List<LogPageVisit> getFilteredLogPageVisit() {
		return filteredLogPageVisit;
	}

	public void setFilteredLogPageVisit(List<LogPageVisit> filteredLogPageVisit) {
		this.filteredLogPageVisit = filteredLogPageVisit;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public Integer getRefreshRate() {
		return refreshRate;
	}

	public void setRefreshRate(Integer refreshRate) {
		this.refreshRate = refreshRate;
	}
}
