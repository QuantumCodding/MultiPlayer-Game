package com.GameName.Util.Crash;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CrashReport {
	private static DateFormat dateFormat;
	private static final String END, TAB;
	private static final String DEFAULT_LOCATION;
	
	static {
		DEFAULT_LOCATION = "res/crash/GameName_CrashReport_";
		dateFormat = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
		END = "\n"; TAB = "\t";
	}
	
	private String description;
	private Throwable cause;
	
	private String date, time;
	
	public CrashReport(String description, Throwable cause) {
		this.description = description;
		this.cause = cause;
		
		String rawDate = dateFormat.format(Calendar.getInstance().getTime());
		date = rawDate.split(" ")[0];
		time = rawDate.split(" ")[1];
	}
	
	public boolean writeReport(File location) {
		if(location.exists()) {
			String path = location.getAbsolutePath();
			location = getFilePath(Integer.parseInt(path.substring(path.length() - 5, path.length() - 4)) + 1);
		}
		
		try {
			PrintStream write = new PrintStream(location.getAbsolutePath(), "UTF-8");
			
			write.println("----------GameName has Crashed!----------" + END);

			write.println("TimeZone: " + System.getProperty("user.timezone"));
			write.println("Date: " + date.replace("/", " / ").trim());
			write.println("Time: " + time + END);
			
			write.println("Description: ");
			write.println(TAB + description + END);
			
			write.println(cause.toString());			
			for(StackTraceElement element : cause.getStackTrace()) {
				write.println(TAB + element + END);
			}			
			write.println(END);
			
			write.println("-------------Computer Specs.-------------" + END);
			
			write.println("Operation System: " + System.getProperty("os.name"));
			write.println("Java Version: " + System.getProperty("java.version") + END);
			
			write.println("Available processors: [" +
			        "Cores: " + Runtime.getRuntime().availableProcessors() + "]");			
		    write.println("Free memory: [" + 
		    		"Bytes: " + Runtime.getRuntime().freeMemory() + "]");	    
		    long maxMemory = Runtime.getRuntime().maxMemory();
		    write.println("Maximum memory: [" + 
		    		"Bytes: " +  (maxMemory == Long.MAX_VALUE ? "No Limit" : maxMemory) + "]");
		    write.println("Total memory available to JVM: [" + 
		    		"Bytes: " + Runtime.getRuntime().totalMemory() + "]");
				    
			write.close();			
		} catch (IOException e) {
			System.out.println(location);
			return false;
		}
		
		return true;
	}
	
	public File getFilePath(int id) {
		return new File(DEFAULT_LOCATION + 
//				cause.toString().substring(
//				cause.toString().lastIndexOf('.') + 1, 
//				cause.toString().indexOf(' ') - 1)+ "_" 
				+ id + ".txt");
	}
	
	public static void main(String[] args) {
		CrashReport report = new CrashReport("Testing", new Exception("Yup"));
		System.out.println(report.writeReport(report.getFilePath(0)));
//		System.getProperties().list(System.out);
	}
}
