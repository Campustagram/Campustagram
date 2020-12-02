package com.campustagram.core.security.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.service.LoggerService;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private LoggerService loggerService;
	
	private static final String ACTIVE_CLASS_NAME = "CustomUserDetailsService";
	
	/*
	 * This method is executed whenever a user try to login
	 * Checks the database for user's information
	 */
	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		final String ACTIVE_METHOD_NAME = "loadUserByUsername";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		User user = userRepository.findByEmailNotDeleted(userEmail);
		
		if(null == user) {
			throw new UsernameNotFoundException(String.format("User with email: %s does not exist in database" , userEmail));
		}
		
		if(null == user.getRole() || null == user.getRole().getPrivileges() || null == user.getRole().getPrivileges().keySet()) {
			throw new UsernameNotFoundException(String.format("User with email: %s does not have any authorities" , userEmail));
		}
		
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return user;
	}
}