package ch.zhaw.pm.domain.exception;

/**
 * Provides error codes and messages. The codes can be used to identify text
 * resources in a file for translation purposes.
 *
 */
public enum ErrCode {

	ERROR(1, "error-"),
	UNKNOWNCOMMAND(2, "unknown command"),
	READCONFIGFILE(3, "read config file"),
	NO_PARKING_SPOT_SELECTED(4, "no parking spot selected"),
	INITPARKINGMETER(5, "cannot init parking meter"),
	PARKINGSPOTNOTVALID(6, "parking spot is not valid"),
	INVALIDCOIN(7, "invalid coin inserted"),
	MAINTENANCENOTALLOWED(8, "maintenance is only allowd in 'enter spot mode'"),
	MORETHANMAXPARKINGTIME(9,
			"the maximum parking time is exceeded - coin is not taken"),
	DBOPENBYOTHERPROCESS(10,
			"Maybe the database file is already opened by another process!!");

	int no;
	String msg;

	ErrCode(int no, String msg) {
		this.no = no;
		this.msg = msg;

	}

	/**
	 * @return the message number
	 */
	public final int getNo() {
		return no;
	}

	/**
	 * @return the msg string
	 */
	public final String getMsg() {
		return msg;
	}

	/**
	 * Return the error Code from message number
	 * 
	 * @param msgNo
	 * @return the error code
	 */
	public static ErrCode getErrCode(int msgNo) {
		for (ErrCode e : ErrCode.values()) {
			if (msgNo == e.no)
				return e;
		}
		return null;
	}

	/**
	 * Return the message String from message number
	 * 
	 * @param msgNo
	 * @return the message String
	 */
	public static String getMessageFromCode(int msgNo) {
		for (ErrCode e : ErrCode.values()) {
			if (msgNo == e.no)
				return e.getMsg();
		}
		return null;
	}

}
