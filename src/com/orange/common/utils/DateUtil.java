package com.orange.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

	public static String DATE_FORMAT = "yyyyMMddHHmmss";

	public static String currentDate() {
		Date date = new Date();
		return DateUtil.dateToStringByFormat(date, DATE_FORMAT);
	}

	public static String dateToString(Date date){
		return dateToStringByFormat(date, DATE_FORMAT);
	}
	
	// format example "dd/MM/yyyy-hh:mm:ss"
	public static String dateToStringByFormat(Date date, String format) {
		if (date == null || format == null)
			return null;

		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
	}

	public static Date dateFromString(String dateString){
		return dateFromStringByFormat(dateString, DATE_FORMAT);
	}
			
	public static Date dateFromStringByFormat(String dateString, String format) {
		if (dateString == null || format == null)
			return null;

		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			return dateFormat.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String dateDescription(Date date) {
		if (date == null)
			return "(null)";
		else
			return date.toString();
	}

	public static long getCurrentTime() {
		Date date = new Date();
		return date.getTime();
	}
}
