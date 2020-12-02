package com.campustagram.core.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.campustagram.core.model.User;

@Service
public class UpdateSecurityContextService {
	public void updateSecurityContextForEmailChange(String newEmail) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User)authentication.getPrincipal();
		user.setEmail(newEmail);
	}
	
	public void updateSecurityContextForPasswordChange(String newPassword) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User)authentication.getPrincipal();
		user.setPassword(newPassword);
	}
}