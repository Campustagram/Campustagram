package com.campustagram.core.security.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.service.LoggerService;

public class CustomAccessDeniedHandler implements AccessDeniedHandler{
	
	@Autowired
	private LoggerService loggerService;
	
	private static final String ACTIVE_CLASS_NAME = "CustomAccessDeniedHandler";
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		final String ACTIVE_METHOD_NAME = "handle";
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      
		if (null != auth) {
			String logError = String.format("User: %s tried to access URL: %s" , auth.getName() , request.getRequestURL());
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME , logError , CommonConstants.ERROR);
		}

		//Redirect user to the access denied page
		redirectStrategy.sendRedirect(request , response , "/permissiondenied");
	}
}
