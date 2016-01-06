package ch.zhaw.pm.fe.view.console.text;

/**
 * Provides message codes and message strings. The codes can be used to identify
 * text resources in a file for translation purposes.
 *
 */
public enum MsgCode {

	EXIT(1, "shutdown parking meter - bye "),
	READCONFIG(2, "boot: reading config file"),
	STARTPARKINGMETER(3, "boot: parking meter started at: "),
	INITPARKINGMETER(4, "boot: init parking meter service"),
	BOOTINGPARKINGMETER(5, "boot: parking-meter is booting"),
	ENTERSPOTNO(6, "enter spot no: "),
	PROMPT(7, ">"),
	COINPROMPT(8, "coin > "),
	SPOTPROMPT(9, "spot > "),
	SELECTEDSPOTNO(10, "selected spot no: "),
	INSERTED(11, "inserted "),
	FORSPOT(12, " for spot "),
	REMAININGTIME(13, "| remaining time "),
	MINUTE(14, "min "),
	INSERTCOINS(15, "insert coins: "),
	ALLINFO(16, "All Parking Meter Information"),
	OR(17, " or ");

	int no;
	String msg;

	MsgCode(int no, String msg) {
		this.no = no;
		this.msg = msg;

	}

	/**
	 * @return the message no
	 */
	public final int getNo() {
		return no;
	}

	/**
	 * @return the msg itself
	 */
	public final String getMsg() {
		return msg;
	}

	/**
	 * Return the message String from message number
	 * 
	 * @param msgNo
	 * @return the message String
	 */
	public static String getMessageFromCode(int msgNo) {
		for (MsgCode e : MsgCode.values()) {
			if (msgNo == e.no)
				return e.getMsg();
		}
		return null;
	}

}
