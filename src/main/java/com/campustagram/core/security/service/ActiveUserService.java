package com.campustagram.core.security.service;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.service.LoggerService;

@Service
@Transactional
public class ActiveUserService implements IActiveUserService {

	private static final String ACTIVE_CLASS_NAME = "ActiveUserService";

	private final Logger logger = LoggerFactory.getLogger(LoggerService.class);

	private static final String LOG_REGEX = "User: {} => Class({}) Method({}) => info({}) => status({}) ";

	@Autowired
	private UserRepository userRepository;

	/**
	 * returns the active user or null.
	 */
	@Override
	public User fetchActiveUser() {
		final String ACTIVE_METHOD_NAME = "fetchActiveUser";
		logger.info(LOG_REGEX, "UNKNOWN", ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

		Authentication loggedInUserAuthentication = SecurityContextHolder.getContext().getAuthentication();

		if (null == loggedInUserAuthentication || loggedInUserAuthentication instanceof AnonymousAuthenticationToken) {
			logger.info(LOG_REGEX, "UNKNOWN", ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
			return null;
		} else {
			String userEmail = loggedInUserAuthentication.getName();

			logger.info(LOG_REGEX, userEmail, ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
			return userRepository.findByEmailNotDeleted(userEmail);
		}
	}
}