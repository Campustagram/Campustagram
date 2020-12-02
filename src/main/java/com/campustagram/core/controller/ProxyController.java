package com.campustagram.core.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.model.WebAppProxy;
import com.campustagram.core.persistence.WebProxyRepository;
import com.campustagram.core.service.LoggerService;

@Scope(value = "prototype")
@Component(value = "proxyController")
public class ProxyController {
	@Autowired
	private WebProxyRepository webProxyRepository;
	@Autowired
	private LoggerService loggerService;

	private static final String ACTIVE_CLASS_NAME = "ProxyController";

	public ProxyController() {
		super();
	}

	public WebAppProxy assignProxy() throws Exception {
		try {
			WebAppProxy webProxy = webProxyRepository.getLeastUsedProxy();
			webProxyRepository.increaseProxyUsage(webProxy.getId());
			// proxy usage +1
			return webProxy;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public WebAppProxy leaveProxy(WebAppProxy webProxy) throws Exception {
		try {
			if ((null != webProxy) && (null != webProxy.getId())) {
				if (webProxy.getActiveWorkerOnProxy() >= 0) {
					webProxyRepository.decreaseProxyUsage(webProxy.getId());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// proxy usage -1
		return null;
	}

	public WebAppProxy proxyError(WebAppProxy webProxy) throws Exception {
		final String ACTIVE_METHOD_NAME = "proxyError";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		webProxyRepository.updateProxyErrorCount(webProxy.getId(), new Date());
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, "webProxy error: " + webProxy, CommonConstants.END);

		return webProxy;
	}

	public WebAppProxy useWebProxy(WebAppProxy webProxy) throws Exception {
		final String ACTIVE_METHOD_NAME = "useWebProxy";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		if ((null != webProxy) && (null != webProxy.getId())) {
			if (webProxy.getUsageCountByApp() > 25) {

				webProxy = leaveProxy(webProxy);
				webProxy = assignProxy();

				webProxy.setUsageCountByApp(0);
			}
		} else {
			webProxy = assignProxy();
		}

		webProxy.setUsageCountByApp(webProxy.getUsageCountByApp() + 1);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return webProxy;
	}
}
