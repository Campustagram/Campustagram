package com.campustagram.core.security.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.header.Header;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.service.LoggerService;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LoggerService loggerService;
	
	private static final String ACTIVE_CLASS_NAME = "CustomLogoutSuccessHandler";

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	/*
	 * This method executed when user logs out
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		final String ACTIVE_METHOD_NAME = "onLogoutSuccess";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		
		if(null != authentication) {
			String logInfo = String.format("%s has successfully logged out", authentication.getName());
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, logInfo, CommonConstants.INFO);
			
			User loggedOutUser = (User)authentication.getPrincipal();
			loggedOutUser.setOnline(false);
			userRepository.save(loggedOutUser);
		}

		response.setStatus(HttpServletResponse.SC_OK);
		redirectStrategy.sendRedirect(request , response , "/login?logout");
		
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public void setUserAsOffline(User user) {
		userRepository.updateUserOnlineStatus(user.getId(), false);
	}
}