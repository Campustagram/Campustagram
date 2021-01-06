package com.campustagram.core.util;

import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;

import org.springframework.web.context.annotation.ApplicationScope;

import com.campustagram.core.common.CommonFunctions;

@ManagedBean(name = "navigationUtils")
@ApplicationScope
public class NavigationUtils {
	private static Map<String, String> urlsAndPaths = new HashMap<>();

	private static final String FACES_REDIRECT_LITERAL = "?faces-redirect=true";

	static {
		urlsAndPaths.put("/loglist", "/pages/core/logMonitoring/loglist.xhtml");
		urlsAndPaths.put("/pagevisitloglist", "/pages/core/logMonitoring/pagevisitloglist.xhtml");
		urlsAndPaths.put("/createrole", "/pages/core/roleManagement/createrole.xhtml");
		urlsAndPaths.put("/editrole", "/pages/core/roleManagement/editrole.xhtml");
		urlsAndPaths.put("/dbsizechecker", "/pages/core/systemManagement/db/dbsizedbsizechecker.xhtml");
		urlsAndPaths.put("/dbdeadtupleschecker", "/pages/core/systemManagement/db/dbdeadtupleschecker.xhtml");
		urlsAndPaths.put("/performance/cpu", "/pages/core/systemManagement/performance/cpu.xhtml");
		urlsAndPaths.put("/performance/disc", "/pages/core/systemManagement/performance/disc.xhtml");
		urlsAndPaths.put("/performance/memory", "/pages/core/systemManagement/performance/memory.xhtml");
		urlsAndPaths.put("/createnewpassword", "/pages/core/login/createnewpassword.xhtml");
		urlsAndPaths.put("/login", "/pages/core/login/login.xhtml");
		urlsAndPaths.put("/signup", "/pages/core/login/signup.xhtml");
		urlsAndPaths.put("/signupverifycode", "/pages/core/login/signupverifycode.xhtml");
		urlsAndPaths.put("/verifycode", "/pages/core/login/verifycode.xhtml");
		urlsAndPaths.put("/profile/editinformation", "/pages/core/userManagement/profile/editinformation.xhtml");
		urlsAndPaths.put("/profile/security", "/pages/core/userManagement/profile/security.xhtml");
		urlsAndPaths.put("/profile/theme", "/pages/core/userManagement/profile/theme.xhtml");
		urlsAndPaths.put("/profile", "/pages/core/userManagement/profile.xhtml");
		urlsAndPaths.put("/userlist", "/pages/core/userManagement/userlist.xhtml");
		urlsAndPaths.put("/adduserprofile", "/pages/core/userManagement/adduserprofile.xhtml");
		urlsAndPaths.put("/userprofileedit", "/pages/core/userManagement/userprofileedit.xhtml");
		urlsAndPaths.put("/resetpassword", "/pages/core/login/resetpassword.xhtml");
		urlsAndPaths.put("/dashboard", "/pages/core/dashboard.xhtml");
		urlsAndPaths.put("/errorpage", "/pages/core/errorpage/errorpage.xhtml");
		urlsAndPaths.put("/system/settings/generalsettings",
				"/pages/core/systemManagement/systemConfiguration/generalsettings.xhtml");
		urlsAndPaths.put("/maintenance", "/pages/core/maintenance/maintenance.xhtml");
		urlsAndPaths.put("/notification", "/pages/core/notification/notification.xhtml");
		urlsAndPaths.put("/permissiondenied", "/pages/core/errorpage/403.xhtml");
		urlsAndPaths.put("/rolelist", "/pages/core/roleManagement/rolelist.xhtml");
		urlsAndPaths.put("/system/settings/systememail",
				"/pages/core/systemManagement/emailConfiguration/systememail.xhtml");
		urlsAndPaths.put("/system/settings", "/pages/core/systemManagement/systemConfiguration/settings.xhtml");
		urlsAndPaths.put("/ticket", "/pages/core/support/ticket.xhtml");
		urlsAndPaths.put("/useragentlist", "/pages/core/proxy/useragentlist.xhtml");
		urlsAndPaths.put("/viewticket", "/pages/core/support/viewticket.xhtml");
		urlsAndPaths.put("/proxycheck", "/pages/core/proxy/proxycheck.xhtml");
		urlsAndPaths.put("/proxylist", "/pages/core/proxy/proxylist.xhtml");
		urlsAndPaths.put("/proxygroup", "/pages/core/proxy/proxygroup.xhtml");
		urlsAndPaths.put("/demodatatable", "/pages/core/demo/demodatatable.xhtml");
		urlsAndPaths.put("/demoform", "/pages/core/demo/demoform.xhtml");
		urlsAndPaths.put("/index", "/pages/landing/index.xhtml");
		urlsAndPaths.put("/403", "/pages/core/errorpage/403.xhtml");
		
		
		urlsAndPaths.put("/mainpage", "/pages/app/mainpage.xhtml");
		urlsAndPaths.put("/profilepage", "/pages/app/profilepage.xhtml");
		urlsAndPaths.put("/search", "/pages/app/search.xhtml");
		
	}

	public NavigationUtils() {
		// This constructor is for JSF
	}

	public static String buildRedirectionString(String url) {
		if (null != urlsAndPaths.get(url)) {
			return urlsAndPaths.get(url) + FACES_REDIRECT_LITERAL;
		} else {
			System.out.println("ERROR: /hatali/" + url + "/" + FACES_REDIRECT_LITERAL);
		}

		return null;
	}

	public static String buildOutcomeString(String url) {
		if (null != urlsAndPaths.get(url)) {
			return urlsAndPaths.get(url);
		} else {
			System.err.println("ERROR: /hatali/" + url);
		}

		return null;
	}

	public static String buildOutcomeRedirectionString(String url) {
		if (null != urlsAndPaths.get(url)) {
			return urlsAndPaths.get(url) + FACES_REDIRECT_LITERAL;
		} else {
			System.out.println("ERROR: /hatali/" + url + "/" + FACES_REDIRECT_LITERAL);
		}

		return null;
	}

	public static String buildHrefString(String url) {
		return CommonFunctions.getLocalhostUrl() + url;
	}
}
