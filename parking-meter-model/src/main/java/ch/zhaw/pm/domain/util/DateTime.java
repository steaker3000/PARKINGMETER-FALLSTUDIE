package ch.zhaw.pm.domain.util;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Contains methods to to format time and date
 *
 */
public class DateTime {
	/**
	 * Computes from two dates the time difference in milliseconds. The time
	 * difference is always positiv, independent of date1 is younger or older to
	 * date2.
	 * 
	 * @param date1
	 *            date 1.
	 * @param date2
	 *            date 2.
	 * @return the time difference in milliseconds.
	 */
	public static long computeTimeDifference(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return 0;
		GregorianCalendar cal1 = new GregorianCalendar();
		cal1.setTime(date1);
		GregorianCalendar cal2 = new GregorianCalendar();
		cal2.setTime(date2);

		long diff = Math.abs(cal1.getTimeInMillis() - cal2.getTimeInMillis());
		return diff;
	}

	/**
	 * Compares date1 with date2. Returns true if date1 is older than date2. <br>
	 * Example date1=2013-10-23 12:10 date2=2013-10-23 11:10 => returns true
	 * 
	 * @param date1
	 *            date 1 to be compared.
	 * @param date2
	 *            date 2 to be compared.
	 * @return true if date1 is older the date2
	 */
	public static boolean isDate1OlderThenDate2(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return false;
		GregorianCalendar cal1 = new GregorianCalendar();
		cal1.setTime(date1);
		GregorianCalendar cal2 = new GregorianCalendar();
		cal2.setTime(date2);

		return ((cal1.getTimeInMillis() - cal2.getTimeInMillis()) > 0);

	}

	/**
	 * Adds minutes to a given date.
	 * 
	 * @param date
	 *            the given date.
	 * @param minutes
	 *            the minutes to add.
	 * @return the new date and time.
	 */
	public static Date addTime(Date date, int minutes) {
		if (date == null)
			return null;
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);

		Date addDate = new Date();
		addDate.setTime(cal.getTimeInMillis() + minutes * 60000);
		return addDate;
	}

	/**
	 * Adds minutes to a given date.
	 * 
	 * @param date
	 *            the given date.
	 * @param minutes
	 *            the miliseconds to add.
	 * @return the new date and time.
	 */
	public static Date addTime(Date date, long miliseconds) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);

		Date addDate = new Date();
		addDate.setTime(cal.getTimeInMillis() + miliseconds);
		return addDate;
	}

}
