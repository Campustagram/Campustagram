package com.campustagram.core.security.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.Assert;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.service.LoggerService;

public class DatabaseSecurityContextRepository implements SecurityContextRepository{
	@Autowired
	private LoggerService loggerService;
	
	private static final String ACTIVE_CLASS_NAME = "DatabaseSecurityContextRepository";
	
	private final SecurityContextRepository delegate;
    private final UserDetailsService userDetailsService;

    //TODO updateSecurityContextService gerekli mi kontrol edilsin
    public DatabaseSecurityContextRepository(final SecurityContextRepository delegate, final UserDetailsService userDetailsService) {
        Assert.notNull(delegate , "Delegate can not be null");
        Assert.notNull(userDetailsService , "User Details can not be null");
        this.delegate = delegate;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public SecurityContext loadContext(final HttpRequestResponseHolder requestResponseHolder) {
    	final String ACTIVE_METHOD_NAME = "loadContext";
    	loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
        
    	SecurityContext securityContext = delegate.loadContext(requestResponseHolder);

        if(null == securityContext.getAuthentication()) {
            return securityContext;
        }

        Authentication authentication = securityContext.getAuthentication();
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());

        //this code has to be modified when using remember me service, jaas or a custom authentication token
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails , userDetails.getPassword() , userDetails.getAuthorities());

        securityContext.setAuthentication(token);
        saveContext(securityContext, requestResponseHolder.getRequest(), requestResponseHolder.getResponse());
        
        loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
        return securityContext;
    }

    @Override
    public void saveContext(final SecurityContext context, final HttpServletRequest request, final HttpServletResponse response) {
        delegate.saveContext(context, request, response);
    }

    @Override
    public boolean containsContext(final HttpServletRequest request) {
        return delegate.containsContext(request);
    }
}
