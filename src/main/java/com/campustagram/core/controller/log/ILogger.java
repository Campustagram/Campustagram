package com.campustagram.core.controller.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.security.service.ActiveUserService;

public class ILogger implements AppLogger {
	private Server server;
	
	@Autowired
	private ActiveUserService activeUserService;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private String logRegex = "User: {} => Class({}) Method({}) => info({}) => status({}) ";

	public ILogger(Server server) {
		this.server = server;
	}

	public ILogger() {

	}

	@Override
	public void writeInfo(String className, String methodName, String info, String status) {
		this.log.info(logRegex, getUserInfo(), className, methodName, info, status);
	}

	@Override
	public void writeWarn(String className, String methodName, String info, String status) {
		this.log.warn(logRegex, getUserInfo(), className, methodName, info, status);
	}

	@Override
	public void writeError(String className, String methodName, String info, String status) {
		this.log.error(logRegex, getUserInfo(), className, methodName, info, status);
	}

	@Override
	public void writeDebug(String className, String methodName, String info, String status) {
		this.log.debug(logRegex, getUserInfo(), className, methodName, info, status);
	}

	public String getUserInfo() {
		if (null != server && null != activeUserService.fetchActiveUser()) {
			StringBuilder sb = new StringBuilder();
			sb.append(activeUserService.fetchActiveUser().getId().toString());
			sb.append(" => ");
			sb.append(activeUserService.fetchActiveUser().getFullName());
			try {
				sb.append(" => ");
				sb.append(CommonFunctions.getClientIpAddress(server.getHttpServletRequest()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return sb.toString();
		}

		if (null != server && null != server.getHttpServletRequest()) {
			return CommonFunctions.getClientIpAddress(server.getHttpServletRequest());
		}

		return "UNKNOWN";

	}

}
