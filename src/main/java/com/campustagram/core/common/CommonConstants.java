package com.campustagram.core.common;

import java.io.File;

import javax.faces.bean.ManagedBean;

import org.springframework.web.context.annotation.ApplicationScope;

@ManagedBean(name = "commonConstants")
@ApplicationScope
public class CommonConstants {
	public static final String START = "START";
	public static final String END = "END";
	public static final String INFO = "INFO";
	public static final String ERROR = "ERROR";
	public static final String SERVICE_ERROR = "Service Error =>";
	public static final String WHITE_SPACE_CHAR = "whiteSpaceChar";
	public static final String START_UP_CHECKS = "startUpChecks";
	public static final String EMPTY_STRING = "";

	public static final String ANY_CHARACTER = "[\\D]";
	public static final String STRING_SEPERATOR = "/-/";
	//Spring uses this string as default remember me id
	//If need change , change in spring security also 
	public static final String REMEMBER_ME_COOKIE_NAME = "remember-me";

	// ====================================================================================================================
	// COMMON START:
	// ====================================================================================================================

	public static final String USER_IMAGE_PATH = "src" + File.separator + "main" + File.separator + "webapp"
			+ File.separator + "resources" + File.separator + "core" + File.separator + "img" + File.separator + "user"
			+ File.separator;
	public static final String USER_IMAGE_PATH_PF = "/img/user/";

	public static final String DEFAULT_LANGUAGE_CODE = "tr";

	public static final Integer COOKIE_LIFE = 30 * 24 * 60 * 60; // one month

	public static final long MILLIS_PER_SECOND = 1000;
	public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
	public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
	public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;
	public static final long MILLIS_PER_MONTH = 30 * MILLIS_PER_DAY;
	public static final long MILLIS_PER_YEAR = 365 * MILLIS_PER_DAY;

	protected static final String[] IP_HEADER_CANDIDATES = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };
	// ====================================================================================================================
	// COMMON END:
	// ====================================================================================================================

	// ====================================================================================================================
	// LOG START :
	// ====================================================================================================================
	public static final Short LOG_DANGER = 1;
	public static final Short LOG_WARNING = 3;
	public static final Short LOG_INFO = 5;
	public static final Short LOG_UPDATE = 11;
	public static final Short LOG_DELETE = 12;
	public static final Short LOG_CREATE = 13;
	public static final Short LOG_BLOCK = 21;
	public static final Short LOG_UNBLOCK = 22;
	// ====================================================================================================================
	// LOG END:
	// ====================================================================================================================
	// ====================================================================================================================
	// SYSTEM MIN MAX VALUES START :
	// ====================================================================================================================
	public static final int MIN_EMAIL_LENGTH = 5;
	public static final int MAX_EMAIL_LENGTH = 256;
	public static final int MIN_PASSWORD_LENGTH = 8;
	public static final int MAX_PASSWORD_LENGTH = 30;
	public static final int MIN_RE_ENTER_PASSWORD_LENGTH = 8;
	public static final int MAX_RE_ENTER_PASSWORD_LENGTH = 30;
	public static final int MIN_NEW_PASSWORD_LENGTH = 8;
	public static final int MAX_NEW_PASSWORD_LENGTH = 30;
	public static final int MIN_RE_ENTER_NEW_PASSWORD_LENGTH = 8;
	public static final int MAX_RE_ENTER_NEW_PASSWORD_LENGTH = 30;
	public static final int MIN_NAME_LENGTH = 2;
	public static final int MAX_NAME_LENGTH = 30;
	public static final int MIN_LASTNAME_LENGTH = 2;
	public static final int MAX_LASTNAME_LENGTH = 30;

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public int getMinNameLength() {
		return MIN_NAME_LENGTH;
	}

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public int getMaxNameLength() {
		return MAX_NAME_LENGTH;
	}

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public int getMinPasswordLength() {
		return MIN_PASSWORD_LENGTH;
	}

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public int getMaxPasswordLength() {
		return MAX_PASSWORD_LENGTH;
	}

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public int getMinCheckPasswordLength() {
		return MIN_RE_ENTER_PASSWORD_LENGTH;
	}

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public int getMaxCheckPasswordLength() {
		return MAX_RE_ENTER_PASSWORD_LENGTH;
	}

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public int getMinNewPasswordLength() {
		return MIN_NEW_PASSWORD_LENGTH;
	}

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public int getMaxNewPasswordLength() {
		return MAX_NEW_PASSWORD_LENGTH;
	}

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public int getMinReenterNewPasswordLength() {
		return MIN_RE_ENTER_NEW_PASSWORD_LENGTH;
	}

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public int getMaxReenterNewPasswordLength() {
		return MAX_RE_ENTER_NEW_PASSWORD_LENGTH;
	}

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public int getMaxEmailLength() {
		return MAX_EMAIL_LENGTH;
	}

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public int getMinEmailLength() {
		return MIN_EMAIL_LENGTH;
	}

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public int getMinLastnameLength() {
		return MIN_LASTNAME_LENGTH;
	}

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public int getMaxLastnameLength() {
		return MAX_LASTNAME_LENGTH;
	}
	// ====================================================================================================================
	// SYSTEM MIN MAX VALUES END :
	// ====================================================================================================================
	// ====================================================================================================================
	// ROLES START:
	// ====================================================================================================================

	public static final Short SUPER_ADMIN = 1;
	public static final Short ADMIN = 3;
	public static final Short PROJECT_MANAGER = 6;
	public static final Short STANDART_USER = 8;
	public static final Short NONE_USER = 10;

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public Short requiredMinRoleSuperAdmin() {
		return CommonConstants.SUPER_ADMIN;
	}

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public Short requiredMinRoleAdmin() {
		return CommonConstants.ADMIN;
	}

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public Short requiredMinRoleProjectManager() {
		return CommonConstants.PROJECT_MANAGER;
	}

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public Short requiredMinRoleStandartUser() {
		return CommonConstants.STANDART_USER;
	}

	/**
	 * Written for JSF access.
	 * 
	 * @return
	 */
	public Short requiredRoleNone() {
		return CommonConstants.NONE_USER;
	}
	// ====================================================================================================================
	// ROLES END:
	// ====================================================================================================================

	// ====================================================================================================================
	// PASSWORD FORMATTERS START:
	// ====================================================================================================================

	/**
	 * prefix used for encryption. Do not convert to string.
	 * 
	 * @return
	 */
	public static char[] getAppPrefix() {
		return "cqJR$Ak4aAvNpzTKCrF8JU@gg-Sr6G6tmt!@5QJekst!*F8jJUfSYF?VYVzEFdVXEZue?97F=#Y$zpJV3^Dg^hW=L_ZyZDZq_C!4xtw-fJJ^_RQxKEAm!+U=&PqH#ngjV*k+3Bp5r=!h_#ZFgYDeXz*+TAaMYA*Rbp4%=2T8d5W-u5Rv^?uADsep36nYj#QMjpZU4tGn7EU$pmve+2zs4d4_G8t6j!bxrVH5BSB3z#UxZWmkt#MJZC=J7AuYCt+B"
				.toCharArray();
	}

	/**
	 * suffix used for encryption. Do not convert to string.
	 * 
	 * @return
	 */
	public static char[] getAppSuffix() {
		return "3aqL2NbV3S=gDp?V4qk5jGFn#qLkSLE@N7$AWJwVV^JksTsgv&gX-rBmMEUzp-&@PsZBTHPCTDRSzBNcp6FFMVFVDS?_c?q5e^-M-UfbS?5zxVF3PfD%QKbs$XjJg!_*c#P!nsJGkmBNkpHXy5s%q_mg6R=4wh!LuNqTnNfgK?#L&bxK%yjh&MRc36fGnN_x+#NRtTBngM##n3hGynE_-Td@JJ_N-+sKJ3#?mrLA8UsVU?*Mc-2jy+akgdT7BJmc"
				.toCharArray();
	}

	protected static final char[] KEY = "=*5sR&DY^JpjAWbq".toCharArray(); // 128 bit key
	protected static final char[] INITVECTOR = "Ej6P*rkGv*xbH8^C".toCharArray(); // 16 bytes IV

	// ====================================================================================================================
	// PASSWORD FORMATTERS END:
	// ====================================================================================================================
	// ====================================================================================================================
	// EMAIL FORMATTERS START:
	// ====================================================================================================================
	public static final String EMAIL_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+.(com|org|net|edu|gov|mil|biz|info|mobi)(.[A-Z]{2})?$";
	// ====================================================================================================================
	// EMAIL FORMATTERS END:
	// ====================================================================================================================
	// ====================================================================================================================
	// RANDOM STRING FOR RESET PASSWORD CODE FORMATTERS START:
	// ====================================================================================================================
	public static final String CHAR_LIST = "1234567890";
	public static final int RANDOM_STRING_LENGTH = 6;
	// ====================================================================================================================
	// RANDOM STRING FOR RESET PASSWORD CODE FORMATTERS END:
	// ====================================================================================================================

}
