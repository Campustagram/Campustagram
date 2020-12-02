package com.campustagram.core.security.handlers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.controller.NotificationController;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.service.LoggerService;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private BeanFactory beanFactory;
	
	private static final String ACTIVE_CLASS_NAME = "CustomAuthenticationSuccessHandler";
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	/*
     * When user successfully logs in
     */
    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {
    	final String ACTIVE_METHOD_NAME = "onAuthenticationSuccess";
    	loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

    	User loggedInUser = userRepository.findByEmailNotDeleted(authentication.getName());

    	loggedInUser.setOnline(true);
    	loggedInUser.setLastSeen(new Date(System.currentTimeMillis()));
    	userRepository.save(loggedInUser);

        removeCookies(request, response);

        String userLangCode = loggedInUser.getLanguage().getCode();
        try {
        	FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(userLangCode));
        } catch (Exception e) {
        	String exceptionInfo = String.format("Exception when setting locale: %s", CommonFunctions.getExceptionAsString(e));
        	loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, exceptionInfo, CommonConstants.ERROR);
        }

        NotificationController notificationController = beanFactory.getBean(NotificationController.class);

        notificationController.updateAllNotificationLists();

		getRedirectStrategy().sendRedirect(request , response , "/dashboard"); // redirect to dashboard

        loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
    }

    public void setRedirectStrategy(final RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    protected void removeCookies(final HttpServletRequest request , final HttpServletResponse response) {
    	final String ACTIVE_METHOD_NAME = "removeCookies";
    	loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
        String[] result = request.getParameterValues(CommonConstants.REMEMBER_ME_COOKIE_NAME);

        if (null != result) {
        	
            loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, "Remember me clicked", CommonConstants.INFO);

        } else {
        	loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, "Remember me not clicked", CommonConstants.INFO);
        	
        	Cookie[] allCookies = request.getCookies();
        	
        	if (allCookies != null) {
        		Cookie session = Arrays.stream(allCookies).filter(x -> x.getName().equals(CommonConstants.REMEMBER_ME_COOKIE_NAME)).findFirst().orElse(null);
 
	            if (session != null) {
	                session.setMaxAge(0);
	                response.addCookie(session);
	            }   
	        }
        }

        loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
    }
}