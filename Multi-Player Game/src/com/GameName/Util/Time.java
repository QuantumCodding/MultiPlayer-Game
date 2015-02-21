package com.GameName.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Time {
	public static final long SECONDS = 1000000000L;
	
	public static long getSystemTime() {
		return System.nanoTime();
	}

	public static String getDate() {
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return df.format(cal.getTime()).split(" ")[0];
	}
	
	public static String getTime() {
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return df.format(cal.getTime()).split(" ")[1];
	}
}
