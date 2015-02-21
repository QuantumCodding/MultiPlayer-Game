package com.GameName.Console.Base;

import java.awt.Color;


public class Logger {
	private static Logger instance;

	public final static String DEFAULT = "default";
	public final static String INPUT = "input";
	public final static String INFO = "info";
	public final static String SETUP = "setup";
	public final static String WARNING = "warning";
	public final static String ERROR = "error";
	
	private BasicLog log;
	
	static {
		instance = new Logger();
	}
	
	private Logger() {}
	
	public static Logger addLine(String line) {
		instance.log.addLine(line);
		return instance;
	}
	
	public static Logger addLine(String line, String style) {
		instance.log.addLine(line, style);
		return instance;
	}
	
	public static BasicLog getLog() {
		return instance.log;
	}
	
	public static void setLog(BasicLog log) {
		instance.log = log;
		
		log.addStyle(DEFAULT, BasicLog.DEFAULT_COLOR,   BasicLog.DEFAULT_FONT);
		log.addStyle(INPUT,   new Color(  0, 150,   0), BasicLog.DEFAULT_FONT);
		log.addStyle(INFO,    new Color( 70,   0,   0), BasicLog.DEFAULT_FONT);
		log.addStyle(SETUP,   new Color( 20, 200, 255), BasicLog.DEFAULT_FONT);
		log.addStyle(WARNING, new Color(225, 204,   0), BasicLog.DEFAULT_FONT);
		log.addStyle(ERROR,   new Color(255,   0,   0), BasicLog.DEFAULT_FONT_BOLD);
	}
}
