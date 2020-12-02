package com.campustagram.core.security.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.service.LoggerService;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Autowired
	private LoggerService loggerService;

	private static final String ACTIVE_CLASS_NAME = "CustomAuthenticationFailureHandler";

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authenticationException) throws IOException, ServletException {
		final String ACTIVE_METHOD_NAME = "onAuthenticationFailure";
		String errorLog;
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		/*
		 * Redirect user to necessary pages according to the error that happened when
		 * trying to login
		 */
		if (authenticationException instanceof DisabledException
				|| authenticationException instanceof LockedException) {

			errorLog = String.format("User %s is blocked", request.getUserPrincipal());
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, errorLog, CommonConstants.ERROR);
			redirectStrategy.sendRedirect(request, response, "/login?blocked");

		} else if (authenticationException instanceof AccountExpiredException) {

			errorLog = String.format("User %s is deleted", request.getUserPrincipal());
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, errorLog, CommonConstants.ERROR);
			redirectStrategy.sendRedirect(request, response, "/login?badCredentials");

		} else if (authenticationException instanceof CredentialsExpiredException) {

			errorLog = String.format("User %s's password is expired", request.getUserPrincipal());
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, errorLog, CommonConstants.ERROR);
			redirectStrategy.sendRedirect(request, response, "/resetpassword?passwordExpired");

		} else if (authenticationException instanceof BadCredentialsException) {

			errorLog = String.format("User %s's credentials not valid", request.getUserPrincipal());
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, errorLog, CommonConstants.ERROR);
			redirectStrategy.sendRedirect(request, response, "/login?badCredentials");

		} else if (authenticationException instanceof RememberMeAuthenticationException) {

			errorLog = String.format("User %s's remember-me authentication is failed", request.getUserPrincipal());
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, errorLog, CommonConstants.ERROR);
			redirectStrategy.sendRedirect(request, response, "/login?rememberMeError");

		} else if (authenticationException instanceof UsernameNotFoundException) {

			errorLog = String.format("User %s's username not found in the system", request.getUserPrincipal());
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, errorLog, CommonConstants.ERROR);
			redirectStrategy.sendRedirect(request, response, "/login?invalidUsername");

		} else {

			errorLog = String.format("Authetication failed.Reason: %s", authenticationException.getMessage());
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, errorLog, CommonConstants.ERROR);
			redirectStrategy.sendRedirect(request, response, "/errorpage?authError");

		}
	}
}