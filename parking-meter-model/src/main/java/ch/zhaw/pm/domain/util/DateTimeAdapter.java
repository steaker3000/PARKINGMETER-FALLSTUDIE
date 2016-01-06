package ch.zhaw.pm.domain.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateTimeAdapter extends XmlAdapter<String, Date> {

	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_PATTERN);

	@Override
	public Date unmarshal(String date) throws Exception {
		return formatter.parse(date);
	}

	@Override
	public String marshal(Date date) throws Exception {
		return formatter.format(date);
	}

}
