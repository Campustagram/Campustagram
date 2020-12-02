package com.campustagram.core.service;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.controller.log.AppLogger;
import com.campustagram.core.model.User;
import com.campustagram.core.security.service.IActiveUserService;

@Service
public class LoggerService implements AppLogger{
	
	@Autowired
	private IActiveUserService activeUserService;

	private final Logger logger = LoggerFactory.getLogger(LoggerService.class);

	private static final String LOG_REGEX = "User: {} => Class({}) Method({}) => info({}) => status({}) ";
	
	@Override
	public void writeInfo(String className, String methodName, String info, String status) {
		logger.info(LOG_REGEX, getUserInfo(), className, methodName, info, status);
		
	}

	@Override
	public void writeWarn(String className, String methodName, String info, String status) {
		logger.warn(LOG_REGEX, getUserInfo(), className, methodName, info, status);
		
	}

	@Override
	public void writeError(String className, String methodName, String info, String status) {
		logger.error(LOG_REGEX, getUserInfo(), className, methodName, info, status);
		
	}

	@Override
	public void writeDebug(String className, String methodName, String info, String status) {
		logger.debug(LOG_REGEX, getUserInfo(), className, methodName, info, status);
		
	}

	public String getUserInfo() {
		User activeUser = activeUserService.fetchActiveUser();
		
		HttpServletRequest request = null;
		try {
			if (null != RequestContextHolder.getRequestAttributes()) {
				request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			}
			
			if(null != activeUser) {
				StringBuilder sb = new StringBuilder();
				sb.append(activeUser.getId());
				sb.append(" => ");
				sb.append(activeUser.getFullName());
				try {
					sb.append(" => ");
					sb.append(CommonFunctions.getClientIpAddress(request));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return sb.toString();
			}

			if (null != request) {
				return CommonFunctions.getClientIpAddress(request);
			}
			
			return "UNKNOWN";
		} catch (Exception e) {
			return "UNKNOWN";
		}
	}
}











