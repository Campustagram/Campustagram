package com.campustagram.core.common;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.campustagram.core.controller.log.ILogger;

@ManagedBean(name = "commonFunctions")
@ViewScoped
public class CommonFunctions {
	private static ILogger logger = new ILogger();
	private static Random randomGenerator = new Random();
	private static final String ACTIVE_CLASS_NAME = "CommonFunctions";

	public static <T> List<T> safeList(List<T> other) {
		return other == null ? Collections.emptyList() : other;
	}

	/**
	 * Exception a karşılık gelen string i döndürür.
	 * 
	 * @author Salih Emre Kuru
	 * @param e : exception
	 * @return Exception a karşılık gelen string
	 */
	public static String getExceptionAsString(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	/**
	 * Belirtilen string in son karakteri silinmiş halini döndürür.
	 * 
	 * @author Salih Emre Kuru
	 * @param str : string
	 * @return parametre olarak gelen string null sa veya boşsa parametre olarak
	 *         gelen string return edilir. Aksi durumda, parametre olarak gelen
	 *         string'in son karakteri çıkartılmış olan string return edilir.
	 */
	public static String removeLastCharacterFromString(String str) {
		if (str != null && str.length() > 0) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	/**
	 * Objenin Long değerini return eder. Başarısız olursa <b>null</b> return eder.
	 * 
	 * @author Salih Emre Kuru
	 * @param o : object
	 * @return objenin <b>Long</b> değerini return eder. <br>
	 *         Eğer exception fırlatırsa <b>null</b> return eder.
	 */
	public static Long convertToLong(Object o) {
		try {
			String stringToConvert = String.valueOf(o);
			Long convertedLong = Long.parseLong(stringToConvert);
			return convertedLong;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Objenin Double değerini return eder. Başarısız olursa <b>null</b> return
	 * eder.
	 * 
	 * @author Salih Emre Kuru
	 * @param o : object
	 * @return objenin <b>Double</b> değerini return eder. <br>
	 *         Eğer exception fırlatırsa <b>null</b> return eder.
	 */
	public static Double convertToDouble(Object o) {
		try {
			String stringToConvert = String.valueOf(o);
			Double convertedDouble = new Double(stringToConvert);
			return convertedDouble;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Genereate unique ID from UUID in positive space Reference:
	 * http://www.gregbugaj.com/?p=587
	 * 
	 * @return long value representing UUID
	 */
	public static Long generateUniqueId() {
		long val = -1;
		do {
			final UUID uid = UUID.randomUUID();
			final ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
			buffer.putLong(uid.getLeastSignificantBits());
			buffer.putLong(uid.getMostSignificantBits());
			final BigInteger bi = new BigInteger(buffer.array());
			val = bi.longValue();
		}
		// We also make sure that the ID is in positive space, if its not we simply
		// repeat the process
		while (val < 0);
		return val;
	}

	// ====================================================================================================================
	// Common STYLE START:
	// ====================================================================================================================

	/**
	 * MessageLevel'e karşılık gelen String değerini return eder. Her messageLevel e
	 * karşılık gelen String değeri vardır. Eğer geçersiz messageLevel girilirse
	 * <b>null</b> return edilir.
	 * 
	 * @author Salih Emre Kuru
	 * @param messageLevel :
	 * @return MessageLevel'e karşılık gelen String değerini return eder.<br>
	 *         Eğer karşılık gelen String değeri yoksa <b>null</b> return eder.
	 */
	public static String getColorStyleClass(Short messageLevel) {
		if (messageLevel == 1) {
			return "dangerMessageRowStyle";
		} else if (messageLevel == 3) {
			return "warningMessageRowStyle";
		} else if (messageLevel == 5) {
			return "infoMessageRowStyle";
		} else if (messageLevel == 11) {
			return "updateMessageRowStyle";
		} else if (messageLevel == 12) {
			return "deleteMessageRowStyle";
		} else if (messageLevel == 13) {
			return "recordMessageRowStyle";
		} else if (messageLevel == 21) {
			return "blockMessageRowStyle";
		} else if (messageLevel == 22) {
			return "unblockMessageRowStyle";
		}
		return null;
	}
	// ====================================================================================================================
	// Common STYLE END:
	// ====================================================================================================================

	// ====================================================================================================================
	// Common START:
	// ====================================================================================================================

	/**
	 * Converts the String to a Float and returns that Float.
	 * 
	 * @author Salih Emre Kuru
	 * @param stringNumber : a string
	 * @return If the string given as a parameter is not null and its length is
	 *         greater than 0, it returns the String to Float.<br>
	 *         Otherwise <b>null</b> return.
	 */
	public static Float stringToFloat(String stringNumber) {
		if ((null != stringNumber) && (stringNumber.length() > 0)) {
			stringNumber = stringNumber.replace(",", ".");
			stringNumber = stringNumber.replaceAll(CommonConstants.ANY_CHARACTER, "");
			float floatNumber = Float.parseFloat(stringNumber);
			return formatDecimal(floatNumber);
		}
		return null;
	}

	/**
	 * Number ı %10.2f formatına çevirir. formatDecimal(10.1) : 10.1
	 * formatDecimal(10.01) : 10.01 formatDecimal(10.001) : 10.0
	 * formatDecimal(10.0250008) : 10.03
	 * 
	 * @author Salih Emre Kuru
	 * @param number : any float number
	 * @return %10.2f formatında Float return eder. <br>
	 *         gelen parametre null sa null return eder.
	 */
	public static Float formatDecimal(Float number) {
		if (null != number) {
			float epsilon = 0.004f; // 4 tenths of a cent
			if (Math.abs(Math.round(number) - number) < epsilon) {
				return new Float(String.format("%10.0f", number).replace(",", ".")); // sdb
			} else {
				return new Float(String.format("%10.2f", number).replace(",", ".")); // dj_segfault
			}
		}
		return null;
	}

	/**
	 * Causes the currently executing thread to sleep (temporarily ceaseexecution)
	 * for the specified number of seconds, subject tothe precision and accuracy of
	 * system timers and schedulers. The threaddoes not lose ownership of any
	 * monitors.
	 * 
	 * @author Salih Emre Kuru
	 * @param sleepInSecond
	 */
	public static void sleepInSecond(Integer sleepInSecond) {
		if ((null != sleepInSecond) && (sleepInSecond > 0)) {
			sleepInMiliSecond(sleepInSecond * 1000);
		}
	}

	/**
	 * Causes the currently executing thread to sleep (temporarily ceaseexecution)
	 * for the specified number of milliseconds, subject tothe precision and
	 * accuracy of system timers and schedulers. The threaddoes not lose ownership
	 * of any monitors.
	 * 
	 * @author Salih Emre Kuru
	 * @param sleepInSecond
	 */
	public static void sleepInMiliSecond(Integer sleepInMiliSecond) {
		if ((null != sleepInMiliSecond) && (sleepInMiliSecond > 0)) {
			try {
				Thread.sleep((long) sleepInMiliSecond);
			} catch (InterruptedException e) {
				try {
					Thread.currentThread().interrupt();
				} catch (Exception e2) {
					logger.writeError(ACTIVE_CLASS_NAME, "sleepInSecond",
							com.campustagram.core.common.CommonFunctions.getExceptionAsString(e2),
							CommonConstants.ERROR);

				}
			}
		}
	}

	/**
	 * Checks image exist. <br>
	 * if it finds the matching picture return that. Otherwise, it returns the
	 * default imagePath.
	 * 
	 * @param imagePath
	 * @return return image full path. <br>
	 *         if imagePath not found returns default image full path.
	 */
	public String checkImageExist(String imagePath) {
		File f = new File(CommonConstants.USER_IMAGE_PATH + imagePath);
		if (f.exists() && !f.isDirectory()) {
			return CommonConstants.USER_IMAGE_PATH + imagePath;
		}
		return CommonConstants.USER_IMAGE_PATH + "user.png";
	}

	/**
	 * Checks image exist. <br>
	 * if it finds the matching picture return that. Otherwise, it returns the
	 * default imagePath.
	 * 
	 * @param imagePath
	 * @return return image full path. <br>
	 *         if imagePath not found returns default image full path.
	 */
	public String checkImageExistPF(String imagePath) {
		File f = new File(CommonConstants.USER_IMAGE_PATH + imagePath);
		if (null != imagePath && f.exists() && !f.isDirectory()) {
			return CommonConstants.USER_IMAGE_PATH_PF + imagePath;
		}
		return CommonConstants.USER_IMAGE_PATH_PF + "user.png";
	}

	/**
	 * Primefaces messages larını tetiklemek için kullanılır.
	 * 
	 * @author Salih Emre Kuru
	 * @param severity : SEVERITY_INFO, SEVERITY_WARN, SEVERITY_ERROR,
	 *                 SEVERITY_FATAL
	 * @param summary  : related summary
	 * @param detail   : related detail
	 */
	public static void addMessage(Severity severity, String summary, String detail) {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("internationalization.messages",
					FacesContext.getCurrentInstance().getViewRoot().getLocale());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, bundle.getString(summary), bundle.getString(detail)));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, "???" + summary + "???", "???" + detail + "???"));
			logger.writeError(ACTIVE_CLASS_NAME, "addMessage",
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
		}
	}

	/**
	 * Clones the object. It is a deep copy. Not shallow copy.
	 * 
	 * @author Salih Emre Kuru
	 * @param copyObject
	 * @return other new clone object.<br>
	 *         Faulty cases return null.
	 * 
	 */
	public static Object deepObjectClone(Object copyObject) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(copyObject);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			logger.writeError(ACTIVE_CLASS_NAME, "deepObjectClone",
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
			return null;
		}
	}

	/**
	 * 
	 * @author Salih Emre Kuru
	 * @param event file upload event
	 * @return if successful, it returns the encrypted named file path.<br>
	 *         if unsuccessful, it returns null.
	 * @throws IOException
	 */

	public static String uploadImage(FileUploadEvent event) throws IOException {
		UploadedFile uploadedPhoto = event.getFile();
		byte[] bytes = null;
		if (null != uploadedPhoto) {
			bytes = uploadedPhoto.getContents();
			String filename = FilenameUtils.getName(uploadedPhoto.getFileName()) + CommonDate.currentDate();
			String filenameHash = CommonCryptographicHash.encryptStringMD5SHA1(filename.toCharArray()) + ".png";
			try (BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File(CommonConstants.USER_IMAGE_PATH + filenameHash)))) {
				stream.write(bytes);

				return filenameHash;
			} catch (Exception e) {
				logger.writeError(ACTIVE_CLASS_NAME, "uploadImage",
						com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
			}

		}
		return null;
	}

	// ====================================================================================================================
	// Common END:
	// ====================================================================================================================

	// ====================================================================================================================
	// KILL SESSION FORMATTERS START:
	// ====================================================================================================================

	/**
	 * kills the specified session.
	 * 
	 * @param beanName specified session name
	 * @return true if successful, false if unsuccessful.
	 */
	public static boolean killSession(String beanName) {
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			session.removeAttribute(beanName);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// ====================================================================================================================
	// KILL SESSION FORMATTERS END:
	// ====================================================================================================================
	// ====================================================================================================================
	// EMAIL FORMATTERS START:
	// ====================================================================================================================

	/**
	 * email format check
	 * 
	 * @param emailInput
	 * @return
	 */
	public static boolean isEmailUsableFormat(String emailInput) {
		String lastFourChars = "";
		if (emailInput.length() > 8) {
			lastFourChars = emailInput.substring(emailInput.length() - 7);
		} else {
			return false;
		}
		if (lastFourChars.contains(".edu.tr")) {
			return Pattern.compile(CommonConstants.EMAIL_PATTERN, Pattern.CASE_INSENSITIVE).matcher(emailInput).find();
		} else {
			return false;
		}
	}
	// ====================================================================================================================
	// EMAIL FORMATTERS END:
	// ====================================================================================================================
	// ====================================================================================================================
	// RANDOM STRING FOR RESET PASSWORD CODE FORMATTERS START:
	// ====================================================================================================================

	/**
	 * This method generates random string of the specified length.
	 * 
	 * @author Salih Emre Kuru
	 * @param randomStringLength length of expected string
	 * @return string of the specified length.
	 */
	public static String generateRandomStringBySize(int randomStringLength) {
		char ch;
		int i = 0;
		StringBuffer randStr = new StringBuffer();
		for (i = 0; i < randomStringLength; i++) {
			ch = CommonConstants.CHAR_LIST.charAt(getRandomNumber());
			randStr.append(ch);
		}
		return randStr.toString();
	}

	/**
	 * This method generates random numbers
	 * 
	 * @return integer random number
	 */
	public static int getRandomNumber() {
		int randomInt = 0;

		randomInt = randomGenerator.nextInt(CommonConstants.CHAR_LIST.length());
		if (randomInt - 1 == -1) {
			return randomInt;
		} else {
			return randomInt - 1;
		}
	}

	public static String generateRandomString() {
		return UUID.randomUUID().toString();
	}

	public static char[] generateRandomChar() {
		return UUID.randomUUID().toString().toCharArray();
	}
	// ====================================================================================================================
	// RANDOM STRING FOR RESET PASSWORD CODE FORMATTERS END:
	// ====================================================================================================================

	// ====================================================================================================================
	// BROWSER START:
	// ====================================================================================================================

	/**
	 * 
	 * @param request
	 * @return get browser full info
	 */
	public static String getBrowserInfo(HttpServletRequest request) {
		return request.getHeader("user-agent");
	}
	// ====================================================================================================================
	// BROWSER END:
	// ====================================================================================================================
	// ====================================================================================================================
	// CLIENT START:
	// ====================================================================================================================

	/**
	 * 
	 * @param request
	 * @return client ip address
	 */
	public static String getClientIpAddress(HttpServletRequest request) {
		if (null != request) {
			for (String header : CommonConstants.IP_HEADER_CANDIDATES) {
				String ip = request.getHeader(header);
				if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
					return ip;
				}
			}
			return request.getRemoteAddr();
		}

		return null;
	}

	/**
	 * 
	 * @param request
	 * @return usable client url as JSF
	 */
	public static String getClientUrlasJSF(HttpServletRequest request) {
		return request.getRequestURL().toString() + "?" + request.getQueryString();
	}

	/**
	 * 
	 * @param request
	 * @return usable client url as String
	 */
	public static String getClientUrl(HttpServletRequest request) {
		URL url;
		try {
			url = new URL(request.getRequestURL().toString());
			URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(),
					(String) request.getAttribute("javax.servlet.forward.request_uri"),
					(String) request.getAttribute("javax.servlet.forward.query_string"), null);
			return uri.toString();
		} catch (MalformedURLException | URISyntaxException e) {
			logger.writeError(ACTIVE_CLASS_NAME, "getClientUrl",
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
		}
		return null;
	}

	/**
	 * @return url=http://127.0.0.1:8081
	 */
	public static String getLocalhostUrl() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		StringBuilder stb = new StringBuilder();
		stb.append("http://");
		if (null != request.getServerName()) {
			stb.append(request.getServerName());
		}
		if (80 != request.getServerPort()) {
			stb.append(":");
			stb.append(request.getServerPort());
		}
		stb.append("/Campustagram");
		return stb.toString();
	}

	// ====================================================================================================================
	// CLIENT END:"
	// ====================================================================================================================
	// ====================================================================================================================
	// DATE FORMATTER START:
	// ====================================================================================================================

	// ====================================================================================================================
	// COLOR FORMATTERS START:
	// ====================================================================================================================
	/**
	 * 
	 * @param value ffffff
	 * @return #ffffff
	 */
	public String convertColorValueToCSSValue(String value) {
		return "#" + value;
	}

	/**
	 * 
	 * @param value
	 * @param opacityValue
	 * @return
	 */
	public String convertColorValueToCSSValueWithOpacity(String value, Integer opacityValue) {
		return "#" + value + opacityValue;
	}

	/**
	 * 
	 * @param value #ffffff
	 * @return ffffff
	 */
	public String convertColorCSSValueToValue(String value) {
		return value.substring(1);
	}

	// ====================================================================================================================
	// COLOR FORMATTERS END:
	// ====================================================================================================================

	// ====================================================================================================================
	// CALENDAR PROPERTİES END:
	// ====================================================================================================================

	// ====================================================================================================================
	// URL PERMALINK START:
	// ====================================================================================================================

	/**
	 * Yalnızca Türkçe ve İngilizce dillerini desteklemektedir. Belirtilen dile göre
	 * özel karakter "&" yerine "and" veya "ve" gelmektedir.
	 * 
	 * @author Salih Emre Kuru
	 * @param url
	 * @param language : "tr" ya da "en" olmak zorundadır.
	 * @return desteklenen dillere göre url retrurn edilir. Hatalı durum olursa null
	 *         return eder.
	 */
	public static String seoMoreFriendlyURL(String url, String language) {
		if (null != url && ("tr".equalsIgnoreCase(language) || "en".equalsIgnoreCase(language))) {
			if ("tr".equalsIgnoreCase(language)) {
				url = url.replace("&", "ve");
			} else if ("en".equalsIgnoreCase(language)) {
				url = url.replace("&", "and");
			}
			return seoFriendlyURL(url);
		}
		return null;
	}

	/**
	 * Belirtilen url i "-" a göre parçalara ayırır.
	 * 
	 * @author Salih Emre Kuru
	 * @param url any url
	 * @return "-" a göre parçalanmış url i return eder. Hata oluşması durumunda
	 *         null return eder.
	 */
	public static String[] splitSeoFriendlyUrl(String url) {
		if (null != url) {
			try {
				return seoMoreFriendlyURL(url, "en").split("-");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	/**
	 * Verilen url içerisinde bulunan özel karakterleri kaldırır. Seo uyumlu hale
	 * getirir.
	 * 
	 * @author Salih Emre Kuru
	 * @param url
	 * @return özel karakter içermeyen String döndürür. Hata oluşması durumunda null
	 *         döndürür.
	 */
	public static String seoFriendlyURL(String url) {
		if (null != url) {
			url = url.replace("ş", "s");
			url = url.replace("Ş", "S");
			url = url.replace("ğ", "g");
			url = url.replace("Ğ", "G");
			url = url.replace("İ", "I");
			url = url.replace("ı", "i");
			url = url.replace("ç", "c");
			url = url.replace("Ç", "C");
			url = url.replace("ö", "o");
			url = url.replace("Ö", "O");
			url = url.replace("ü", "u");
			url = url.replace("Ü", "U");
			url = url.replace("'", "");
			url = url.replace("\"", "");

			try {
				return Normalizer.normalize(url.toLowerCase(), Form.NFD)
						.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").replaceAll("[^\\p{Alnum}]+", "-");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	// ====================================================================================================================
	// URL PERMALINK END:
	// ====================================================================================================================

}