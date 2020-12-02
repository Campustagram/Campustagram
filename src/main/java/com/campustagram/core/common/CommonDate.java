package com.campustagram.core.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.campustagram.core.controller.log.ILogger;

@ManagedBean(name = "commonDate")
@ViewScoped
public class CommonDate {
	private static ILogger logger = new ILogger();
	private static final String ACTIVE_CLASS_NAME = "CommonDate";
// ====================================================================================================================
	// DATE FORMATTER START:
	// ====================================================================================================================

	/**
	 * Removes the hour from the date.
	 * 
	 * @author Salih Emre Kuru
	 * @param date
	 * @return date without clock
	 */
	public static Date removeHourFromDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * Makes minutes readable for printing.
	 * 
	 * @author Salih Emre Kuru
	 * @param minute
	 * @return readable minute
	 */
	public static String minuteToString(Integer minute) {
		if (null != minute) {
			long hours = TimeUnit.MINUTES.toHours(minute);
			long minutes = TimeUnit.MINUTES.toMinutes(minute) - (hours * 60);
			return String.format("%d hr(s) %d min(s)", hours, minutes);
		}
		return null;
	}

	/**
	 * Şimdi ki zamandan ne kadar önce işlem yapıldığını yazdırmak için kullanılan
	 * yardımcı methodtur. Sadece en büyük zaman dilimini yazdırmak için
	 * kullanılır<br>
	 * 5 gün 7 saat 23 dakika 3 saniye önce ise "5 gün" önce yazdırır. <br>
	 * 0 gün 7 saat 23 dakika 3 saniye önce ise "7 saat" önce yazdırır. <br>
	 * 0 gün 0 saat 23 dakika 3 saniye önce ise "23 dakika" önce yazdırır. <br>
	 * 0 gün 0 saat 0 dakika 3 saniye önce ise "3 saniye" önce yazdırır. <br>
	 * saniyeden daha küçükse "" yazdırır.
	 * 
	 * @author Salih Emre Kuru
	 * @param processDate
	 * @param dateName
	 * @return
	 */
	public static String getUserFriendlyDate(Date processDate, List<String> dateName) {
		if (processDate == null) {
			return "";
		}
		long timeDiff = (CommonDate.currentDate().getTime() - processDate.getTime());
		return preparePastDateValues(timeDiff, dateName);
	}

	/**
	 * processDate ve endDate arasındaki farkı yazdırmak için kullanılan yardımcı
	 * methodtur. Sadece en büyük zaman dilimini yazdırmak için kullanılır.
	 * 
	 * @author Salih Emre Kuru
	 * @param processDate
	 * @param endDate
	 * @param dateName
	 * @return
	 */
	public static String getUserFriendlyDateWithEndDate(Date processDate, Date endDate, List<String> dateName) {
		if (processDate == null) {
			return "";
		}
		long timeDiff = (endDate.getTime() - processDate.getTime());
		return preparePastDateValues(timeDiff, dateName);
	}
	

	public static String getUserFriendlyDateFull(Date processDate, List<String> dateName) {
		if (processDate == null) {
			return "";
		}
		long timeDiff = (CommonDate.currentDate().getTime() - processDate.getTime());
		return preparePastDateValuesFull(timeDiff, dateName);
	}
	
	/**
	 * processDate ve endDate arasındaki farkı yazdırmak için kullanılan yardımcı
	 * methodtur. Bütün zaman dilimlerini yazdırmak için kullanılır.
	 * 
	 * @author Salih Emre Kuru
	 * @param processDate
	 * @param endDate
	 * @param dateName
	 * @return
	 */
	public static String getUserFriendlyDateWithEndDateFull(Date processDate, Date endDate, List<String> dateName) {
		if (processDate == null) {
			return "";
		}
		long timeDiff = (endDate.getTime() - processDate.getTime());
		return preparePastDateValuesFull(timeDiff, dateName);
	}

	/**
	 * Şimdi ki zamandan ne kadar önce işlem yapıldığını yazdırmak için kullanılan
	 * yardımcı methodtur. Sadece en büyük zaman dilimini yazdırmak için
	 * kullanılır<br>
	 * 5 gün 7 saat 23 dakika 3 saniye önce ise "5 gün" önce yazdırır. <br>
	 * 0 gün 7 saat 23 dakika 3 saniye önce ise "7 saat" önce yazdırır. <br>
	 * 0 gün 0 saat 23 dakika 3 saniye önce ise "23 dakika" önce yazdırır. <br>
	 * 0 gün 0 saat 0 dakika 3 saniye önce ise "3 saniye" önce yazdırır. <br>
	 * saniyeden daha küçükse "" yazdırır.
	 * 
	 * @author Salih Emre Kuru
	 * @param timeDiff
	 * @param dateName
	 * @return string in readable format
	 */
	private static String preparePastDateValues(long timeDiff, List<String> dateName) {
		if (timeDiff > CommonConstants.MILLIS_PER_YEAR)
			return Integer.toString((int) (timeDiff / CommonConstants.MILLIS_PER_YEAR)) + dateName.get(0);
		if (timeDiff > CommonConstants.MILLIS_PER_MONTH)
			return Integer.toString((int) (timeDiff / CommonConstants.MILLIS_PER_MONTH)) + dateName.get(1);
		if (timeDiff > CommonConstants.MILLIS_PER_DAY)
			return Integer.toString((int) (timeDiff / CommonConstants.MILLIS_PER_DAY)) + dateName.get(2);
		if (timeDiff > CommonConstants.MILLIS_PER_HOUR)
			return Integer.toString((int) (timeDiff / CommonConstants.MILLIS_PER_HOUR)) + dateName.get(3);
		if (timeDiff > CommonConstants.MILLIS_PER_MINUTE)
			return Integer.toString((int) (timeDiff / CommonConstants.MILLIS_PER_MINUTE)) + dateName.get(4);
		if (timeDiff > CommonConstants.MILLIS_PER_SECOND)
			return Integer.toString((int) (timeDiff / CommonConstants.MILLIS_PER_SECOND)) + dateName.get(5);
		return "";
	}

	/**
	 * Şimdi ki zamandan ne kadar önce işlem yapıldığını yazdırmak için kullanılan
	 * yardımcı methodtur. <br>
	 * 5 gün 7 saat 23 dakika 3 saniye önce ise "5 gün 7 saat 23 dakika 3 saniye"
	 * önce yazdırır. <br>
	 * 0 gün 7 saat 23 dakika 3 saniye önce ise "7 saat 23 dakika 3 saniye" önce
	 * yazdırır. <br>
	 * 0 gün 0 saat 23 dakika 3 saniye önce ise "23 dakika 3 saniye" önce yazdırır.
	 * <br>
	 * 0 gün 0 saat 0 dakika 3 saniye önce ise "3 saniye" önce yazdırır. <br>
	 * saniyeden daha küçükse "" yazdırır.
	 * 
	 * @author Salih Emre Kuru
	 * @param timeDiff
	 * @param dateName
	 * @return String in readable format
	 */
	private static String preparePastDateValuesFull(long timeDiff, List<String> dateName) {
		String value = "";
		if (timeDiff > CommonConstants.MILLIS_PER_YEAR) {
			value += Integer.toString((int) (timeDiff / CommonConstants.MILLIS_PER_YEAR)) + dateName.get(0) + " ";
			timeDiff %= CommonConstants.MILLIS_PER_YEAR;
		}
		if (timeDiff > CommonConstants.MILLIS_PER_MONTH) {
			value += Integer.toString((int) (timeDiff / CommonConstants.MILLIS_PER_MONTH)) + dateName.get(1) + " ";
			timeDiff %= CommonConstants.MILLIS_PER_MONTH;
		}
		if (timeDiff > CommonConstants.MILLIS_PER_DAY) {
			value += Integer.toString((int) (timeDiff / CommonConstants.MILLIS_PER_DAY)) + dateName.get(2) + " ";
			timeDiff %= CommonConstants.MILLIS_PER_DAY;
		}
		if (timeDiff > CommonConstants.MILLIS_PER_HOUR) {
			value += Integer.toString((int) (timeDiff / CommonConstants.MILLIS_PER_HOUR)) + dateName.get(3) + " ";
			timeDiff %= CommonConstants.MILLIS_PER_HOUR;
		}
		if (timeDiff > CommonConstants.MILLIS_PER_MINUTE) {
			value += Integer.toString((int) (timeDiff / CommonConstants.MILLIS_PER_MINUTE)) + dateName.get(4) + " ";
			timeDiff %= CommonConstants.MILLIS_PER_MINUTE;
		}
		if (timeDiff > CommonConstants.MILLIS_PER_SECOND) {
			value += Integer.toString((int) (timeDiff / CommonConstants.MILLIS_PER_SECOND)) + dateName.get(5) + " ";
		}
		return value;
	}

	/**
	 * iki date arasındaki zaman farkını dakika cinsinden bulmak için kullanılır.
	 * 
	 * @author Salih Emre Kuru
	 * @param date1
	 * @param date2
	 * @return date1-date2
	 */
	public static Long computeDiffBetween2Minutes(Date date1, Date date2) {
		if ((null != date1) && (null != date2)) {
			return Math.round((date1.getTime() - date2.getTime()) / ((double) CommonConstants.MILLIS_PER_MINUTE));
		}
		return null;
	}

	/**
	 * iki date arasındaki zaman farkını saniye cinsinden bulmak için kullanılır.
	 * 
	 * @author Salih Emre Kuru
	 * @param date1
	 * @param date2
	 * @return date1-date2
	 */
	public static long computeDiffBetween2Second(Date date1, Date date2) {
		return Math.round((date1.getTime() - date2.getTime()) / ((double) CommonConstants.MILLIS_PER_SECOND));
	}

	/**
	 * Date i daha okunaklı göstermek için kullanılır. <br>
	 * Tarih formatı "HH:mm:ss / dd-MM-yyy" şeklindedir.
	 * 
	 * @author Salih Emre Kuru
	 * @param date
	 * @return HH:mm:ss / dd-MM-yyy
	 */
	public static String dateBeautifier(Date date) {
		if (null != date) {
			return new SimpleDateFormat("HH:mm:ss / dd-MM-yyy").format(date);
		}
		return null;
	}

	/**
	 * Date i daha okunaklı göstermek için kullanılır. <br>
	 * Tarih formatı "dd-MM-yyy" şeklindedir.
	 * 
	 * @author Salih Emre Kuru
	 * @param date
	 * @return dd-MM-yyy
	 */
	public static String dateBeautifierJustDate(Date date) {
		if (null != date) {
			return new SimpleDateFormat("dd-MM-yyy").format(date);
		}
		return null;
	}

	/**
	 * Adds minutes to the current date.
	 * 
	 * @author Salih Emre Kuru
	 * @param addMinute
	 * @return
	 */
	public static Date addMinuteToDate(Integer addMinute) {
		Calendar date = Calendar.getInstance();
		long t = date.getTimeInMillis();
		return new Date(t + (addMinute * CommonConstants.MILLIS_PER_MINUTE));
	}

	/**
	 * Adds days to the current date.
	 * 
	 * @author Salih Emre Kuru
	 * @param addDate
	 * @return
	 */
	public static Date addDayToDate(Integer addDate) {
		return addDayToDate(addDate, new Date());
	}

	/**
	 * Adds days to the specified date.
	 * 
	 * @author Salih Emre Kuru
	 * @param minusDay    number of days to add
	 * @param processDate date on which the day will be added.
	 * @return minusDay day after from processDate
	 */
	public static Date addDayToDate(Integer addDate, Date processDate) {
		if (processDate != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(processDate);

			// manipulate date
			// c.add(Calendar.YEAR, 1);
			// c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, addDate); // same with c.add(Calendar.DAY_OF_MONTH, 1);
			// c.add(Calendar.HOUR, 1);
			// c.add(Calendar.MINUTE, 1);
			// c.add(Calendar.SECOND, 1);

			// convert calendar to date
			Date resultDate = c.getTime();

			return resultDate;
		}
		return null;
	}

	/**
	 * Subtract dates from current date.
	 * 
	 * @author Salih Emre Kuru
	 * @param minusDay number of days to subtract
	 * @return minusDay day ago from current date
	 */
	public static Date minusDayToDate(Integer minusDate) {
		return minusDayToDate(minusDate, new Date());
	}

	/**
	 * Subtract dates from specified date.
	 * 
	 * @author Salih Emre Kuru
	 * @param minusDay    number of days to subtract
	 * @param processDate date on which the day will be subtracted.
	 * @return minusDay day ago from processDate
	 */
	public static Date minusDayToDate(Integer minusDay, Date processDate) {
		if (processDate != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(processDate);

			// manipulate date
			// c.add(Calendar.YEAR, 1);
			// c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -minusDay); // same with c.add(Calendar.DAY_OF_MONTH, 1);
			// c.add(Calendar.HOUR, 1);
			// c.add(Calendar.MINUTE, 1);
			// c.add(Calendar.SECOND, 1);

			// convert calendar to date
			Date resultDate = c.getTime();

			return resultDate;
		}
		return null;
	}

	/**
	 * Subtract minutes from current date.
	 * 
	 * @author Salih Emre Kuru
	 * @param minusMinute number of minutes to subtract
	 * @return minusMinutes minute ago from current date
	 */
	public static Date minusMinuteToDate(Integer minusMinute) {
		Calendar cal = Calendar.getInstance();
		// remove next line if you're always using the current time.
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -minusMinute);
		return cal.getTime();
	}
	// ====================================================================================================================
	// DATE FORMATTER END:
	// ====================================================================================================================

	// ====================================================================================================================
	// CALENDAR PROPERTİES START:
	// ====================================================================================================================
	/**
	 * This method is written to make it easier to change the system time setting.
	 * 
	 * @return date
	 */
	public static Date currentDate() {
		return new Date();
	}

	// ====================================================================================================================
	// CALENDAR PROPERTİES END:
	// ====================================================================================================================

}