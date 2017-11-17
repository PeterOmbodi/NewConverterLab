package com.peterombodi.newconverterlab.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.peterombodi.newconverterlab.global.Constants.FORMAT_LOCAL_DATE;
import static com.peterombodi.newconverterlab.global.Constants.FORMAT_SERVER_DATE;

/**
 * Created by Peter on 08.11.2017.
 */

public final class Utils {

	public static Date getDateFromString(String date, String format) {
		if (format == null) format = FORMAT_SERVER_DATE;
		SimpleDateFormat f = new SimpleDateFormat(format, Locale.US);
		date = date.trim();
		try {
			return f.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getStringFromDate(Date date, String format) {
		if (format == null) format = FORMAT_LOCAL_DATE;
		if (date != null) {
			return new SimpleDateFormat(format, Locale.US).format(date);
		}
		return "";
	}

	public static String transformDate(String date, String formatFrom, String formatTo) {
		return getStringFromDate(getDateFromString(date, formatFrom), formatTo);
	}

	public static Date yesterdayDate() {
		return new Date(System.currentTimeMillis() - 1000L * 60L * 60L * 24L);
	}

}
