package com.GameName.Main.Debugging;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.GameName.Util.StringEffect;
import com.GameName.Util.StringEffectFormater;

public class Logger {
	private static Logger instance;
	
	private static ArrayList<String> log;
	private static StringEffect effect;
	private static String working;
	
	private static boolean time;
	private static Calendar cal;
	private static SimpleDateFormat sdf;
	
	static {
		instance = new Logger();
		effect = new StringEffect();
	}
	
	private Logger() {
		log = new ArrayList<String>();
		
		cal = Calendar.getInstance();
    	sdf = new SimpleDateFormat("HH:mm:ss");
	}
	
	public static void println(String info) {		
		print(info).end();
	}
	
	public static Logger print(String info) {		
		working = "[INFO] " + info;	
		return instance;
	}
	
	public Logger setColor(Color c) {
		effect.setColor(c);		
		return instance;
	}
	
	public Logger setBold() {
		effect.setBold();
		return  instance;
	}
	
	public Logger setItalics() {
		effect.setItalics();		
		return  instance;
	}
	
	public Logger setType(String type) {
		working = "[" + type.toUpperCase() + "]" + working.substring(working.indexOf(']') + 1);
		
		if(!effect.getEffects().contains("<color=")) {
			
			switch(type.toUpperCase()) {
			
			case "WARNING": effect.setColor(new Color(255, 234, 0)); break;
			case "ERROR": effect.setColor(Color.RED); break;
			case "SETUP": effect.setColor(Color.BLUE); break;
			case "INPUT": effect.setColor(new Color(0, 150, 0)); break;
			case "VBO": effect.setColor(new Color(225, 105, 0)); break;
			
			default: break;
			
			}
		}
		
		return instance;
	}
	
	public void end() {
		log.add(StringEffectFormater.addEffect((time ? getTime() + " " : "") + working, effect, ""));// + "<br>");
		
		effect = new StringEffect();
		working = "";
	}
	
	protected static void toggleTime() {
		time = !time;
	}
	
	private static String getTime() {
		cal = Calendar.getInstance();
		return sdf.format(cal.getTime());
	}
	
	protected static ArrayList<String> getLog() {
		return log;
	}
}
