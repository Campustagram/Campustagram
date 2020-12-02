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

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.model.WebAppProxy;
import com.campustagram.core.model.WebProxyGroup;
import com.campustagram.core.persistence.WebProxyGroupRepository;
import com.campustagram.core.persistence.WebProxyRepository;
import com.campustagram.core.service.LoggerService;

@ManagedBean(name = "webProxyController")
@Scope(value = "session")
@Component(value = "webProxyController")
@Join(path = "/proxylist", to = "/pages/core/proxy/proxylist.jsf")
public class WebProxyController {

	@Autowired
	private WebProxyRepository webProxyRepository;
	@Autowired
	private WebProxyGroupRepository webProxyGroupRepository;
	@Autowired
	private LoggerService loggerService;
	
	private List<WebAppProxy> webProxyList = new ArrayList<>();
	private List<WebAppProxy> filteredWebProxyList = new ArrayList<>();
	private WebAppProxy tmpWebProxy = new WebAppProxy();
	private String searchKeyword;

	private String selectedTmpProxyGroup;
	private List<WebProxyGroup> webProxyGroups = new ArrayList<>();

	private static final String ACTIVE_CLASS_NAME = "WebProxyController";

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.START);
		setWebProxyList(webProxyRepository.getAllWebProxy());
		setWebProxyGroups(webProxyGroupRepository.getAllWebProxyGroup());
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.END);
	}

	public String startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);
		setSearchKeyword("");
		tmpWebProxy = new WebAppProxy();
		if (!FacesContext.getCurrentInstance().isPostback()) {
			filteredWebProxyList.clear();
			filteredWebProxyList.addAll(webProxyList);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
		return null;
	}

	public void prepareNewProxy() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "prepareNewProxy", null, CommonConstants.START);
		tmpWebProxy = new WebAppProxy();
		selectedTmpProxyGroup = null;
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "prepareNewProxy", null, CommonConstants.END);
	}

	public String addWebProxy() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "addWebProxy", null, CommonConstants.START);
		WebProxyGroup selectedTmpProxyGroupObj = null;
		if (null != selectedTmpProxyGroup) {
			selectedTmpProxyGroupObj = webProxyGroupRepository.getWebProxyGroupByName(selectedTmpProxyGroup).get(0);
		} else {
			selectedTmpProxyGroupObj = webProxyGroupRepository.getWebProxyGroupByName(webProxyGroups.get(0).getName())
					.get(0);
		}
		tmpWebProxy.setWebProxyGroup(selectedTmpProxyGroupObj);
		webProxyRepository.save(tmpWebProxy);
		loadData();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "addWebProxy", null, CommonConstants.END);
		return null;
	}

	public String deleteWebProxy(WebAppProxy webProxy) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "deleteWebProxy", null, CommonConstants.START);
		webProxy.setDeleted(true);
		webProxyRepository.save(webProxy);
		loadData();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "deleteWebProxy", null, CommonConstants.END);
		return null;
	}

	public List<WebAppProxy> getWebProxyList() {
		return webProxyList;
	}

	public void setWebProxyList(List<WebAppProxy> webProxyList) {
		this.webProxyList = webProxyList;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public List<WebAppProxy> getFilteredWebProxyList() {
		return filteredWebProxyList;
	}

	public void setFilteredWebProxyList(List<WebAppProxy> filteredWebProxyList) {
		this.filteredWebProxyList = filteredWebProxyList;
	}

	public WebAppProxy getTmpWebProxy() {
		if (null == tmpWebProxy) {
			return new WebAppProxy();
		}
		return tmpWebProxy;
	}

	public void setTmpWebProxy(WebAppProxy tmpWebProxy) {
		if (null != tmpWebProxy.getWebProxyGroup()) {
			this.selectedTmpProxyGroup = tmpWebProxy.getWebProxyGroup().getName();
		} else {
			this.selectedTmpProxyGroup = null;
		}

		this.tmpWebProxy = tmpWebProxy;
	}

	public String getSelectedTmpProxyGroup() {
		return selectedTmpProxyGroup;
	}

	public void setSelectedTmpProxyGroup(String selectedTmpProxyGroup) {
		this.selectedTmpProxyGroup = selectedTmpProxyGroup;
	}

	public List<WebProxyGroup> getWebProxyGroups() {
		return webProxyGroups;
	}

	public void setWebProxyGroups(List<WebProxyGroup> webProxyGroups) {
		this.webProxyGroups = webProxyGroups;
	}

}
