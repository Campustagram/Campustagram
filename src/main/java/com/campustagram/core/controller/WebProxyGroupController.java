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
import com.campustagram.core.model.WebAppProxy;
import com.campustagram.core.model.WebProxyGroup;
import com.campustagram.core.persistence.WebProxyGroupRepository;
import com.campustagram.core.persistence.WebProxyRepository;
import com.campustagram.core.service.LoggerService;

@ManagedBean(name = "webProxyGroupController")
@Scope(value = "session")
@Component(value = "webProxyGroupController")
@Join(path = "/proxygroup", to = "/pages/core/proxy/proxygroup.jsf")
public class WebProxyGroupController {

	@Autowired
	private WebProxyRepository webProxyRepository;
	@Autowired
	private WebProxyGroupRepository webProxyGroupRepository;
	@Autowired
	private LoggerService loggerService;
	
	private List<WebProxyGroup> webProxyGroupList = new ArrayList<>();
	private List<WebProxyGroup> filteredWebProxyGroupList = new ArrayList<>();
	private WebProxyGroup tmpWebProxyGroup = new WebProxyGroup();
	private String searchKeyword;

	private static final String ACTIVE_CLASS_NAME = "WebProxyGroupController";

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.START);
		setWebProxyGroupList(webProxyGroupRepository.getAllWebProxyGroup());
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.END);
	}

	public String startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);
		searchKeyword = "";
		tmpWebProxyGroup = new WebProxyGroup();
		if (!FacesContext.getCurrentInstance().isPostback()) {
			filteredWebProxyGroupList.clear();
			filteredWebProxyGroupList.addAll(webProxyGroupList);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
		return null;
	}

	public void prepareNewProxyGroup() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "prepareNewProxyGroup", null, CommonConstants.START);
		tmpWebProxyGroup = new WebProxyGroup();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "prepareNewProxyGroup", null, CommonConstants.END);
	}

	public String addWebProxyGroup() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "addWebProxyGroup", null, CommonConstants.START);
		webProxyGroupRepository.save(tmpWebProxyGroup);
		loadData();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "addWebProxyGroup", null, CommonConstants.END);
		return null;
	}

	public String deleteWebProxyFromGroup(WebAppProxy webProxy) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "deleteWebProxy", null, CommonConstants.START);
		webProxy.setWebProxyGroup(null);
		webProxyRepository.save(webProxy);
		loadData();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "deleteWebProxy", null, CommonConstants.END);
		return null;
	}

	public String deleteWebProxyGroup(WebProxyGroup webProxyGroup) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "deleteWebProxyGroup", null, CommonConstants.START);
		webProxyGroup.setDeleted(true);

		for (WebAppProxy webProxy : webProxyGroup.getWebProxyList()) {
			webProxy.setWebProxyGroup(null);
			webProxyRepository.save(webProxy);
		}

		webProxyGroupRepository.save(webProxyGroup);
		loadData();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "deleteWebProxyGroup", null, CommonConstants.END);
		return null;
	}

	public String setActiveWebProxyGroup(WebProxyGroup webProxyGroup) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "setActiveWebProxyGroup", null, CommonConstants.START);
		webProxyGroup.setActive(true);
		webProxyGroupRepository.save(webProxyGroup);
		loadData();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "setActiveWebProxyGroup", null, CommonConstants.END);
		return null;
	}

	public String setPassiveProxyGroup(WebProxyGroup webProxyGroup) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "setPassiveProxyGroup", null, CommonConstants.START);
		webProxyGroup.setActive(false);
		webProxyGroupRepository.save(webProxyGroup);
		loadData();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "setPassiveProxyGroup", null, CommonConstants.END);
		return null;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public List<WebProxyGroup> getFilteredWebProxyGroupList() {
		return filteredWebProxyGroupList;
	}

	public void setFilteredWebProxyGroupList(List<WebProxyGroup> filteredWebProxyGroupList) {
		this.filteredWebProxyGroupList = filteredWebProxyGroupList;
	}

	public List<WebProxyGroup> getWebProxyGroupList() {
		return webProxyGroupList;
	}

	public void setWebProxyGroupList(List<WebProxyGroup> webProxyGroupList) {
		this.webProxyGroupList = webProxyGroupList;
	}

	public WebProxyGroup getTmpWebProxyGroup() {
		return tmpWebProxyGroup;
	}

	public void setTmpWebProxyGroup(WebProxyGroup tmpWebProxyGroup) {
		this.tmpWebProxyGroup = tmpWebProxyGroup;
	}

}
