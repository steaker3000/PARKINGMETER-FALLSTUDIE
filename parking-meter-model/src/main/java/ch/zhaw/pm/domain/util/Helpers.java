package ch.zhaw.pm.domain.util;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

/**
 * Contains helper functions to format or convert
 * 
 *
 */
public class Helpers {

	/**
	 * 
	 * @return Home directory of the user
	 */
	public static String getUserHomeDirectory() {
		return System.getProperty("user.home");
	}

	/**
	 * 
	 * @return current working directory from where java was started
	 */
	public static String getWorkingDirectory() {
		return System.getProperty("user.dir");
	}

	/**
	 * Verifies if a file path exists, if not it will create the directories
	 * 
	 * @param fileName
	 *            Path to create (without file name)
	 */
	public static void createDirectory(String fileName) {

		String pathString = new File(fileName).getAbsolutePath();
		Path path = FileSystems.getDefault().getPath(pathString);
		if (!Files.exists(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
			File dir = new File(pathString);
			dir.mkdirs();
		}
	}

	/**
	 * check if a directory is writable
	 * 
	 * @param pathString
	 *            containing the directory
	 * @return true if writable
	 */
	public static boolean isDirectoryWritable(String pathString) {

		File dir = new File(pathString);
		return dir.canWrite();

	}

	/**
	 * Checks if a string contains a number or letters.
	 * 
	 * @param str
	 *            contains the string to be checked.
	 * @return true if the string str is a number.
	 */
	public static boolean isNumeric(String str) {
		return str.trim().matches("-?\\d+(\\.\\d+)?"); // match a number with
		// optional '-' and
		// decimal.
	}

	/**
	 * Formatting a date and time to <code>"yyyy-MM-dd HH:mm"</code>.<br>
	 * Example: <code>2013-10-11 12:30</code>
	 * 
	 * @param date
	 *            to be formated.
	 * @return a string with the formated date.
	 */
	public static String formatDate(Date date) {
		if (date == null)
			return null;
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return dt.format(date);
	}

	/**
	 * Formats a given time in millisecond to hh:mm:ss. <br>
	 * Example <code>"00:23:55"</code>
	 * 
	 * @param milliseconds
	 *            contains the time to be formated.
	 * @return formated time.
	 */
	public static String formatTime(long milliseconds) {
		DecimalFormat df = new DecimalFormat("00");
		return df.format(milliseconds / (60 * 60 * 1000)) + ":"
				+ df.format(milliseconds / (60 * 1000) % 60) + ":"
				+ df.format(milliseconds / 1000 % 60);
	}

	/**
	 * Formats minutes always to 2 numbers and adds the string "min". <br>
	 * Example: <code>"02 min"</code>
	 * 
	 * @param minutes
	 * @return minutes the string with minutes and " min".
	 */
	public static String formatMinute(double minutes) {
		DecimalFormat df = new DecimalFormat("#00");
		return df.format(minutes) + " min";
	}

	/**
	 * Formats ID^s always to 4 numbers . <br>
	 * Example: <code>"0004"</code>
	 * 
	 * @param minutes
	 * @return minutes the string with minutes and " min".
	 */
	public static String formatID(Integer ID) {
		if (ID == null)
			return "null";
		DecimalFormat df = new DecimalFormat("#0000");
		return df.format(ID);
	}

	/**
	 * Formats the money output depending on the default local region settings.
	 * The value is 6 characters long, an currency string is added at the end.
	 * Example: <code>"  3.50 €"</code>
	 * 
	 * @param value
	 *            the value to be formated.
	 * @param currency
	 *            the currency string.
	 * @param locale
	 *            the locale string
	 * @return money the string plus currency.
	 */

	public static String formatMoney(double value, String currency,
			String locale) {
		Locale loc = new Locale(locale.substring(0, 2), locale.substring(3));
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(loc);
		return (currencyFormatter.format(value));

	}

	/**
	 * Formats the money output depending on the default local region settings.
	 * The value is 6 characters long, an currency string is added at the end.
	 * Example: <code>"  3.50 €"</code>
	 * 
	 * @param value
	 *            the value to be formated.
	 * @param currency
	 *            the currency string.
	 * @param locale
	 *            the locale object
	 * @return money the string plus currency.
	 */
	public static String formatMoney(double value, String currency,
			Locale locale) {
		NumberFormat currencyFormatter = NumberFormat
				.getCurrencyInstance(locale);
		return (currencyFormatter.format(value));
	}

	/**
	 * Formats the money output depending on the default local region settings.
	 * The value is 6 characters long, an currency string is added at the end.
	 * Example: <code>"  3.50 €"</code>
	 * 
	 * @param value
	 *            the value to be formated.
	 * @param currency
	 *            the currency string.
	 * @return money the string plus currency.
	 */
	public static String formatMoney(double value, String currency) {
		Locale currentLocale = Locale.getDefault();

		NumberFormat currencyFormatter = NumberFormat
				.getCurrencyInstance(currentLocale);
		return (currencyFormatter.format(value));
	}

	/**
	 * Formats the decimal output depending on the default local region
	 * settings.
	 * 
	 * @param value
	 *            the value to be formated.
	 * @return money the string plus currency.
	 */
	public static String formatDecimal(double value) {
		Locale currentLocale = Locale.getDefault();

		DecimalFormatSymbols symbols = new DecimalFormatSymbols(currentLocale);
		DecimalFormat df = new DecimalFormat("#0.00", symbols);
		return df.format(value);
	}

	/**
	 * Parses a number string with respect to the locale formatting
	 * 
	 * @param numberString
	 * @return
	 * @throws ParseException
	 */
	public double parseLocale(String numberString) throws ParseException {
		Locale currentLocale = Locale.getDefault();
		NumberFormat nf = NumberFormat.getInstance(currentLocale);
		Number number = nf.parse(numberString);
		return number.doubleValue();
	}

	/**
	 * Returns the currency string
	 * 
	 * @return the currency string
	 */

	public static String ISOtoCurrencySymbol(String currencyISO) {
		Currency currency = Currency.getInstance(currencyISO);
		return currency.getSymbol();
	}

	/**
	 * Formats the Value Added Tax String
	 * 
	 * @param vatFactor
	 * @return
	 */
	public static String formatVAT(double vatFactor) {
		return "" + (vatFactor * 100) + " %";

	}

	/**
	 * Create a Locale object from a string de_CH
	 * 
	 * @param locale
	 * @return
	 */
	public static Locale localeFromString(String locale) {
		String parts[] = locale.split("_", -1);
		if (parts.length == 1)
			return new Locale(parts[0]);
		else if (parts.length == 2
				|| (parts.length == 3 && parts[2].startsWith("#")))
			return new Locale(parts[0], parts[1]);
		else
			return new Locale(parts[0], parts[1], parts[2]);
	}

	/**
	 * Gets the DecimalSeparator based on the current Locale
	 * 
	 * @return DecimalSeparator
	 */
	public static String getDecimalSeparator() {
		DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
		DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
		return String.valueOf(symbols.getDecimalSeparator());

	}

}
