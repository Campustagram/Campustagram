package com.campustagram.core.controller;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;

@ManagedBean(name = "localeController")
@Scope(value = "session")
@Component(value = "localeController")
public class LocaleController {
	@Autowired
	private Server server;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;

	private String selectedTmpLanguage;

	private Locale locale;

	private static final String ACTIVE_CLASS_NAME = "LocaleController";

	@PostConstruct
	public void init() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "init", null, CommonConstants.START);
		try {
			locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		} catch (Exception e) {
			locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		}
		if (null != activeUserService.fetchActiveUser()) {
			if (null == activeUserService.fetchActiveUser().getLanguage()) {
				if (null != server.getLanguageAsObject(String.valueOf(locale))) {
					activeUserService.fetchActiveUser().setLanguage(server.getLanguageAsObject(String.valueOf(locale)));
					selectedTmpLanguage = String.valueOf(locale);
				} else {
					selectedTmpLanguage = String.valueOf(CommonConstants.DEFAULT_LANGUAGE_CODE);
					activeUserService.fetchActiveUser()
							.setLanguage(server.getLanguageAsObject(CommonConstants.DEFAULT_LANGUAGE_CODE));
				}
			}
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "init", null, CommonConstants.END);
	}

	public Locale getLocale() {
		if (null == locale) {
			return new Locale(CommonConstants.DEFAULT_LANGUAGE_CODE);
		}
		return locale;
	}

	public void setLocale(String language) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "setLocale", null, CommonConstants.START);
		if (null != activeUserService.fetchActiveUser()) {
			activeUserService.fetchActiveUser().setLanguage(server.getLanguageAsObject(language));
			userRepository.updateUserLanguage(activeUserService.fetchActiveUser().getId(), server.getLanguageAsObject(language));
		}
		locale = new Locale(language);
		selectedTmpLanguage = language;
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "setLocale", null, CommonConstants.END);
	}

	public String getSelectedTmpLanguage() {
		return selectedTmpLanguage;
	}

	public void setSelectedTmpLanguage(String selectedTmpLanguage) {
		this.selectedTmpLanguage = selectedTmpLanguage;
	}
}
