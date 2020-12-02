package com.campustagram.core.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonDate;
import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.controller.log.ILogger;
import com.campustagram.core.model.AppSystem;
import com.campustagram.core.model.Language;
import com.campustagram.core.model.Log;
import com.campustagram.core.model.LogPageVisit;
import com.campustagram.core.model.SystemProperties;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.LanguageRepository;
import com.campustagram.core.persistence.LogPageVisitRepository;
import com.campustagram.core.persistence.LogRepository;
import com.campustagram.core.persistence.SystemPropertiesRepository;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "server")
@Scope(value = "session")
@Component(value = "server")
@Service
public class Server {
	@Autowired
	private LogRepository logRepository;
	@Autowired
	private LogPageVisitRepository logPageVisitRepository;
	@Autowired
	private SystemPropertiesRepository systemPropertiesRepository;
	@Autowired
	private LanguageRepository languageRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ActiveUserService activeUserService;
	@Autowired
	private LoggerService loggerService;

	private AppSystem appSystem = new AppSystem();

	private User registerUser = new User();
	private User passwordChangedUser = new User();
	private User blockedUser = new User();
	private boolean resetPassword = false;

	private static List<Language> languages = new ArrayList<>();
	private static SystemProperties systemProperties = new SystemProperties();

	private ILogger logger = new ILogger(this);
	private static final String ACTIVE_CLASS_NAME = "Server";

	private String previousPage = null;

	private String applicationVersion = "V.0.1.0";

	@PostConstruct
	public void init() {
		final String ACTIVE_METHOD_NAME = "init";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		setSystemProperties(systemPropertiesRepository.findAll().get(0));
		setLanguages(languageRepository.findAllNotDeleted());
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public boolean checkPageRefresh() {
		final String ACTIVE_METHOD_NAME = "checkPageRefresh";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
		String id = viewRoot.getViewId();
		boolean refresh = (getPreviousPage() != null) && (getPreviousPage().equals(id));

		if (!refresh) {
			PrimeFaces.current().executeScript("localStorage.removeItem('classOfLastSelected');");
		}

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return refresh;
	}

	public void logPageId() {
		final String ACTIVE_METHOD_NAME = "logPageId";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
		setPreviousPage(viewRoot.getViewId());
		PrimeFaces.current().executeScript("localStorage.setItem('lastPage' , '" + viewRoot.getViewId() + "');");
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public String getDashboardPage() {
		final String ACTIVE_METHOD_NAME = "getDashboardPage";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		return NavigationUtils.buildRedirectionString("/dashboard");
		/*
		 * try { if (roleRightController.checkIfActiveUserHasRoleType(RoleType.
		 * ROLE_SYSTEM_MANAGEMENT)) { loggerService.writeInfo(ACTIVE_CLASS_NAME,
		 * ACTIVE_METHOD_NAME, null, CommonConstants.END); return
		 * "/pages/core/dashboard.xhtml?faces-redirect=true"; // redirect to dashboard }
		 * } catch (Exception e) { // TODO: handle exception e.printStackTrace(); }
		 * loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null,
		 * CommonConstants.END); return
		 * "/pages/app/n11/search/search.xhtml?faces-redirect=true"; // redirect to
		 * search
		 */
	}

	public String getUserInfoWithId(Long id) {
		final String ACTIVE_METHOD_NAME = "getUserInfoWithId";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

		if (null != id) {
			try {
				User user = userRepository.getOne(id);

				loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
				return user.getFullName();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;
	}

	public Language getLanguageAsObject(String langCode) {
		final String ACTIVE_METHOD_NAME = "getLanguageAsObject";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		for (Language language : languages) {
			if (language.getCode().equals(langCode)) {
				loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, langCode + " found",
						CommonConstants.END);
				return language;
			}
		}

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, "not found", CommonConstants.END);
		return null;
	}

	public HttpServletRequest getHttpServletRequest() {
		final String ACTIVE_METHOD_NAME = "getHttpServletRequest";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

		try {

			return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		} catch (Exception e) {

			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);

		} finally {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		}

		return null;
	}

	public HttpSession getHttpSession() {
		final String ACTIVE_METHOD_NAME = "getHttpSession";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

		try {

			return getHttpServletRequest().getSession();

		} catch (Exception e) {

			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);

		} finally {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		}

		return null;
	}

	public HttpServletResponse getHttpServletResponse() {
		final String ACTIVE_METHOD_NAME = "getHttpServletResponse";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

		try {

			return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

		} catch (Exception e) {

			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);

		} finally {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		}

		return null;
	}

	public ResourceBundle getResourceBundle() {
		final String ACTIVE_METHOD_NAME = "getResourceBundle";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

		try {

			return ResourceBundle.getBundle("internationalization.messages",
					FacesContext.getCurrentInstance().getViewRoot().getLocale());

		} catch (Exception e) {

			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, CommonFunctions.getExceptionAsString(e),
					CommonConstants.ERROR);

		} finally {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		}

		return null;
	}

	public String getMultiLanguageStringWithKey(String key) {
		final String ACTIVE_METHOD_NAME = "getMultiLanguageStringWithKey";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

		try {
			return getResourceBundle().getString(key);
		} catch (Exception e) {
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, CommonFunctions.getExceptionAsString(e),
					CommonConstants.ERROR);
		} finally {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		}
		return "??? " + key + " ???";
	}

	// ====================================================================================================================
	// LOG FORMATTERS START:
	// ====================================================================================================================
	public String pageEntryJSWorks() {
		final String ACTIVE_METHOD_NAME = "pageEntryWorks";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		try {
			this.createPageVisitLog(activeUserService.fetchActiveUser(), "pageEntryCheckWithJS",
					CommonConstants.LOG_INFO);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;
	}

	public String pageEntryWorks() {
		final String ACTIVE_METHOD_NAME = "pageEntryWorks";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		try {
			this.createPageVisitLog(activeUserService.fetchActiveUser(), "pageEntry", CommonConstants.LOG_INFO);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;
	}

	public String pageExitJSWorks() {
		final String ACTIVE_METHOD_NAME = "pageExitJSWorks";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		try {
			this.createPageVisitLog(activeUserService.fetchActiveUser(), "pageExitCheckWithJS",
					CommonConstants.LOG_INFO);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;
	}

	public LogPageVisit createPageVisitLog(User user, String logInfo, Short logLevel) {
		final String ACTIVE_METHOD_NAME = "createPageVisitLog";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		LogPageVisit logPageVisit = new LogPageVisit();
		logPageVisit.setBrowserInfo(CommonFunctions.getBrowserInfo(getHttpServletRequest()));
		logPageVisit.setIp(CommonFunctions.getClientIpAddress(getHttpServletRequest()));
		logPageVisit.setEnteredURL(CommonFunctions.getClientUrl(getHttpServletRequest()));
		logPageVisit.setLogInfo(logInfo);
		logPageVisit.setLogLevel(logLevel);
		if (null != user) {
			logPageVisit.setUser(user);
		} else {
			if (null != activeUserService.fetchActiveUser()) {
				logPageVisit.setUser(activeUserService.fetchActiveUser());
			}
		}
		logPageVisitRepository.save(logPageVisit);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return logPageVisit;
	}

	public Log createLog(User user, Short logLevel) {
		final String ACTIVE_METHOD_NAME = "createLog";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		Log log = new Log();
		log.setBrowserInfo(CommonFunctions.getBrowserInfo(getHttpServletRequest()));
		log.setIp(CommonFunctions.getClientIpAddress(getHttpServletRequest()));
		log.setEnteredURL(CommonFunctions.getClientUrl(getHttpServletRequest()));
		log.setLogLevel(logLevel);
		if (null != user) {
			log.setUser(user);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return log;
	}

	public void writeLog(String logInfo, Short logLevel) {
		final String ACTIVE_METHOD_NAME = "writeLog";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		Log log = createLog(activeUserService.fetchActiveUser(), logLevel);
		log.setLogInfo(logInfo);
		logRepository.save(log);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public void writeLogWithUser(User user, String logInfo, Short logLevel) {
		final String ACTIVE_METHOD_NAME = "writeLogWithUser";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		Log log = createLog(user, logLevel);
		log.setLogInfo(logInfo);
		logRepository.save(log);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public void writeLogWithProcessUser(User user, String logInfo, Short logLevel) {
		final String ACTIVE_METHOD_NAME = "writeLogWithProcessUser";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		Log log = createLog(activeUserService.fetchActiveUser(), logLevel);
		log.setLogInfo(logInfo);
		if (null != user) {
			log.setProcessUser(user);
		}
		logRepository.save(log);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public String getPastDateToProcessDate(Date processDate) {
		final String ACTIVE_METHOD_NAME = "getPastDateToProcessDate";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return CommonDate.getUserFriendlyDate(processDate, getDateStrings());

	}

	public String getPastDateToProcessDateWithEndDate(Date processDate, Date endDate) {
		final String ACTIVE_METHOD_NAME = "getPastDateToProcessDateWithEndDate";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return CommonDate.getUserFriendlyDateWithEndDate(processDate, endDate, getDateStrings());
	}

	public String getPastDateToProcessDateFull(Date processDate) {
		final String ACTIVE_METHOD_NAME = "getPastDateToProcessDateFull";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return CommonDate.getUserFriendlyDateFull(processDate, getDateStrings());

	}

	public String getPastDateToProcessDateWithEndDateFull(Date processDate, Date endDate) {
		final String ACTIVE_METHOD_NAME = "getPastDateToProcessDateWithEndDateFull";
		if ((null != processDate) && (null != endDate)) {

			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
			return CommonDate.getUserFriendlyDateWithEndDateFull(processDate, endDate, getDateStrings());

		}
		return null;
	}

	public List<String> getDateStrings() {
		ArrayList<String> str = new ArrayList<>();
		str.add(getMultiLanguageStringWithKey("year"));
		str.add(getMultiLanguageStringWithKey("month"));
		str.add(getMultiLanguageStringWithKey("day"));
		str.add(getMultiLanguageStringWithKey("hour"));
		str.add(getMultiLanguageStringWithKey("min"));
		str.add(getMultiLanguageStringWithKey("sec"));
		return str;
	}

	// ====================================================================================================================
	// LOG FORMATTERS END:
	// ====================================================================================================================
	public User getRegisterUser() {
		return registerUser;
	}

	public void setRegisterUser(User registerUser) {
		this.registerUser = registerUser;
	}

	public User getPasswordChangedUser() {
		return passwordChangedUser;
	}

	public void setPasswordChangedUser(User passwordChangedUser) {
		this.passwordChangedUser = passwordChangedUser;
	}

	public boolean getResetPassword() {
		return resetPassword;
	}

	public void setResetPassword(boolean resetPassword) {
		this.resetPassword = resetPassword;
	}

	public User getBlockedUser() {
		return blockedUser;
	}

	public void setBlockedUser(User blockedUser) {
		this.blockedUser = blockedUser;
	}

	public SystemProperties getSystemProperties() {
		return systemProperties;
	}

	public static void setSystemProperties(SystemProperties systemProperties) {
		Server.systemProperties = systemProperties;
	}

	public List<Language> getLanguages() {
		return languages;
	}

	public static void setLanguages(List<Language> languages) {
		Server.languages = languages;
	}

	public ILogger getLogger() {
		return logger;
	}

	public void setLogger(ILogger logger) {
		this.logger = logger;
	}

	public AppSystem getAppSystem() {
		return appSystem;
	}

	public void setAppSystem(AppSystem appSystem) {
		this.appSystem = appSystem;
	}

	public String getPreviousPage() {
		return previousPage;
	}

	public void setPreviousPage(String previousPage) {
		this.previousPage = previousPage;
	}

	public String getApplicationVersion() {
		return applicationVersion;
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

}
