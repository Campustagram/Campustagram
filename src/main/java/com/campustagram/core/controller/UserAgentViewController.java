package com.campustagram.core.controller;

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

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.model.UserAgent;
import com.campustagram.core.persistence.UserAgentRepository;
import com.campustagram.core.service.LoggerService;

@ManagedBean(name = "userAgentViewController")
@Scope(value = "session")
@Component(value = "userAgentViewController")
@Join(path = "/useragentlist", to = "/pages/core/proxy/useragentlist.jsf")
public class UserAgentViewController {

	@Autowired
	private UserAgentRepository userAgentRepository;
	@Autowired
	private LoggerService loggerService;
	
	private List<UserAgent> userAgentList = new ArrayList<>();
	private List<UserAgent> filteredUserAgentList = new ArrayList<>();
	private UserAgent tmpUserAgent = new UserAgent();
	private String searchKeyword;

	private static final String ACTIVE_CLASS_NAME = "UserAgentViewController";

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.START);
		userAgentList = userAgentRepository.getAllUserAgent();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.END);
	}

	public String startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);
		setSearchKeyword("");
		setTmpUserAgent(new UserAgent());
		if (!FacesContext.getCurrentInstance().isPostback()) {
			filteredUserAgentList.clear();
			filteredUserAgentList.addAll(userAgentList);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
		return null;
	}

	public void prepareNewUserAgent() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "prepareNewUserAgent", null, CommonConstants.START);
		tmpUserAgent = new UserAgent();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "prepareNewUserAgent", null, CommonConstants.END);
	}

	public String addUserAgent() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "addUserAgent", null, CommonConstants.START);
		userAgentRepository.save(tmpUserAgent);
		loadData();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "addUserAgent", null, CommonConstants.END);
		return null;
	}

	public String deleteUserAgent(UserAgent userAgent) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "deleteUserAgent", null, CommonConstants.START);
		userAgent.setDeleted(true);
		userAgentRepository.save(userAgent);
		loadData();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "deleteUserAgent", null, CommonConstants.END);
		return null;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public List<UserAgent> getUserAgentList() {
		return userAgentList;
	}

	public void setUserAgentList(List<UserAgent> userAgentList) {
		this.userAgentList = userAgentList;
	}

	public List<UserAgent> getFilteredUserAgentList() {
		return filteredUserAgentList;
	}

	public void setFilteredUserAgentList(List<UserAgent> filteredUserAgentList) {
		this.filteredUserAgentList = filteredUserAgentList;
	}

	public UserAgent getTmpUserAgent() {
		return tmpUserAgent;
	}

	public void setTmpUserAgent(UserAgent tmpUserAgent) {
		this.tmpUserAgent = tmpUserAgent;
	}

}
