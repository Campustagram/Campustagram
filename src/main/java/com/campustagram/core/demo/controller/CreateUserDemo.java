package com.campustagram.core.demo.controller;

import org.springframework.context.annotation.Bean;

import com.campustagram.core.common.CommonDate;
import com.campustagram.core.model.Role;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.LanguageRepository;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.service.BCryptEncoderService;

public class CreateUserDemo {

	private UserRepository userRepository;
	private LanguageRepository languageRepository;
	private BCryptEncoderService bCryptEncoderService;
	/**
	 * 
	 * @param roleRepository
	 */
	public CreateUserDemo(UserRepository userRepository , LanguageRepository languageRepository , BCryptEncoderService bCryptEncoderService) {
		this.userRepository = userRepository;
		this.languageRepository = languageRepository;
		this.bCryptEncoderService = bCryptEncoderService;
	}

	@Bean
	public void createUser(String email, String name, String lastname, Role userRole) {
		if (null == userRepository.findByEmailNotDeleted(email)) {
			User user = new User();
			user.setEmail(email);
			user.setName(name);
			user.setLastname(lastname);
			user.setPassword(bCryptEncoderService.encode("1"));
			user.setBirthDate(CommonDate.currentDate());
			user.setLastSeen(CommonDate.currentDate());
			user.setCreateDate(CommonDate.currentDate());
			user.setLanguage(languageRepository.findByNameNotDeleted("tr"));
			user.setRole(userRole);
			userRole.getUsers().add(user);
			userRepository.save(user);
			System.out.println("User oluşturuldu. (" + email + ")");
		} else {
			System.out.println("User zaten mevcut oluşturulmadı. (" + email + ")");
		}
	}
}
