package com.GameName.Util;

public class Time {
	private static final long SECONDS = 1000000000L;
	
	public static long getTime() {
		return System.nanoTime();
	}

	public static long getSECONDS() {
		return SECONDS;
	}
}
