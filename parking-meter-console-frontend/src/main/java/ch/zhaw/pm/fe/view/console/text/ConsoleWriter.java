package ch.zhaw.pm.fe.view.console.text;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import ch.zhaw.pm.domain.exception.ErrCode;

/**
 * <code>ConsoleWriter</code> writes messages and errors 'language dependent' to
 * the <code>out e.g.
 * err stream</code>.
 * 
 *
 */

public class ConsoleWriter {

	Locale locale;
	PrintStream out;
	PrintStream err;
	ResourceBundle labels;

	/**
	 * Is using localized translation information for the console writer
	 * 
	 * @param out
	 *            PrintStream for outputting message to System.out
	 * @param err
	 *            PrintStream for outputting errors to System.err
	 * @param locale
	 *            Locale information for loading correct resource bundle
	 */
	public ConsoleWriter(PrintStream out, PrintStream err, Locale locale) {
		this.out = out;
		this.err = err;
		this.locale = locale;
		this.labels = ResourceBundle.getBundle("language", locale);
	}

	/**
	 * Constructs Console Writer by using default language English
	 * 
	 * @param out
	 * @param err
	 */
	public ConsoleWriter(PrintStream out, PrintStream err) {
		this.out = out;
		this.err = err;
		this.labels = ResourceBundle.getBundle("language");
	}

	/**
	 * Writes a message to the outstream with new line
	 * 
	 * @param message
	 *            to write
	 */
	public void println(String message) {
		out.println(message);

	}

	/**
	 * Writes a message to the outstream without new line
	 * 
	 * @param message
	 *            to write
	 */
	public void print(String message) {
		out.print(message);

	}

	/**
	 * Writes a message to the outstream without new line. It will try to find a
	 * translated message in the language_xx.properties file.
	 * 
	 * @param msg
	 *            from MsgCode Enum
	 */

	public void print(MsgCode msg) {
		String message = msg.getMsg();
		String label = readLabel(msg.toString(), message);
		out.print(label);

	}

	/**
	 * Writes a message to the outstream with new line. It will try to find a
	 * translated message in the language_xx.properties file.
	 * 
	 * @param msg
	 *            from MsgCode Enum
	 */
	public void println(MsgCode msg) {
		String message = msg.getMsg();
		String label = readLabel(msg.toString(), message);
		out.println(label);

	}

	/**
	 * Print error string to errstream
	 * 
	 * @param message
	 */
	public void printErrln(String message) {
		err.println(message);
	}

	/**
	 * Print formated error string to errstream
	 * 
	 * @param errNo
	 */
	public void printErrln(int errNo) {

		ErrCode errCode = ErrCode.getErrCode(errNo);
		String prefix = formatErrorPrefix(errCode);
		String label = readLabel(errCode.toString(), errCode.getMsg());
		err.println(prefix + label);
	}

	/**
	 * Print formated error string to errstream
	 * 
	 * @param errCode
	 */
	public void println(ErrCode errCode) {
		String message = errCode.getMsg();
		String prefix = formatErrorPrefix(errCode);
		String label = readLabel(errCode.toString(), message);
		err.println(prefix + label);
	}

	/**
	 * Tries to read from resource file. If key is found the string from
	 * resource file is returned, if not then the default label is returned.
	 * 
	 * @param key
	 * @param defaultLabel
	 * @return
	 */
	private String readLabel(String key, String defaultLabel) {
		String label = defaultLabel;
		if (labels != null) {
			try {
				label = labels.getString(key);

			} catch (MissingResourceException e) {
			}

		}

		return label;

	}

	private String formatErrorNumber(int errNo) {
		DecimalFormat df = new DecimalFormat("#000");
		return df.format(errNo);

	}

	private String formatErrorPrefix(ErrCode errCode) {
		String errorLabel = readLabel(ErrCode.ERROR.toString(),
				ErrCode.ERROR.getMsg());
		return errorLabel + formatErrorNumber(errCode.getNo()) + ": ";

	}

}
