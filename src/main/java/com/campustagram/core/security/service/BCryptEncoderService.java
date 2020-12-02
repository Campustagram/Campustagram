package com.campustagram.core.security.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptEncoderService extends BCryptPasswordEncoder{
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public BCryptEncoderService() {
		bCryptPasswordEncoder = new BCryptPasswordEncoder();
	}

	@Override
	public String encode(CharSequence rawData) {
		return bCryptPasswordEncoder.encode(rawData);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
	}
}