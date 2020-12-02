package com.campustagram.core.demo.controller;

import org.springframework.context.annotation.Bean;

import com.campustagram.core.common.CommonDate;
import com.campustagram.core.model.Language;
import com.campustagram.core.persistence.LanguageRepository;

public class CreateLanguageDemo {
	private LanguageRepository languageRepository;

	/**
	 * 
	 * @param roleRepository
	 */
	public CreateLanguageDemo(LanguageRepository languageRepository) {
		this.languageRepository = languageRepository;
	}

	@Bean
	public void createLanguage(String languageName, String code) {
		if (null == languageRepository.findByNameNotDeleted(languageName)) {
			Language language = new Language();
			language.setCode(code);
			language.setCreateDate(CommonDate.currentDate());
			languageRepository.save(language);
			System.out.println("Language oluşturuldu. (" + languageName + ")");
		} else {
			System.out.println("Language zaten mevcut oluşturulmadı. (" + languageName + ")");
		}
	}
}
