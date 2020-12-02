package com.campustagram.core.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.jsoup.Jsoup;
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

@ManagedBean(name = "webProxyCheckController")
@Scope(value = "session")
@Component(value = "webProxyCheckController")
@Join(path = "/proxycheck", to = "/pages/core/proxy/proxycheck.jsf")
public class WebProxyCheckController {

	@Autowired
	private Server server;
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

	private String checkUrl = "https://www.n11.com/";
	private Integer connectionTimeout = 20000;

	private static final String ACTIVE_CLASS_NAME = "WebProxyCheckController";

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

	public void checkUrl() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "checkUrl", null, CommonConstants.START);
		try {
			for (WebAppProxy webProxy : getWebProxyList()) {
				checkProxyWorks(webProxy);
			}
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "checkUrl", null, CommonConstants.END);
	}

	public Long checkProxyWorks(WebAppProxy webProxy) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "checkUrl", null, CommonConstants.START);
		Long processTime = -1L;
		try {
			Date startDate = new Date();
			Jsoup.connect(getCheckUrl()).proxy(webProxy.getHost(), webProxy.getPort())
					.header("Content-Language", "en-US").timeout(getConnectionTimeout()).get();
			processTime = new Date().getTime() - startDate.getTime();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		getWebProxyList().get(getWebProxyList().indexOf(webProxy)).setUrlAccessTime(processTime);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "checkUrl", null, CommonConstants.END);

		return processTime;
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

	public String getCheckUrl() {
		return checkUrl;
	}

	public void setCheckUrl(String checkUrl) {
		this.checkUrl = checkUrl;
	}

	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

}
