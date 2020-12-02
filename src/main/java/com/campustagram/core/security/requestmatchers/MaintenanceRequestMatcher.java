package com.campustagram.core.security.requestmatchers;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.model.SystemProperties;
import com.campustagram.core.persistence.SystemPropertiesRepository;
import com.campustagram.core.service.LoggerService;

public class MaintenanceRequestMatcher implements RequestMatcher {

	@Autowired
	private SystemPropertiesRepository systemPropertiesRepository;
	@Autowired
	private LoggerService loggerService;

	private static final String ACTIVE_CLASS_NAME = "MaintenanceRequestMatcher";

	@Override
	@Transactional
	public boolean matches(HttpServletRequest request) {
		final String ACTIVE_METHOD_NAME = "matches";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		SystemProperties systemProperties = systemPropertiesRepository.findAll().get(0);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return systemProperties.isOnMaintenance() && systemProperties.isEndDateActive();
	}
}