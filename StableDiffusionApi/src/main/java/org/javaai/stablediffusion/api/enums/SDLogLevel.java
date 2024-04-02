package org.javaai.stablediffusion.api.enums;

public class SDLogLevel {

	public static final int DEBUG = 0;
	public static final int INFO = 1;
	public static final int WARN = 2;
	public static final int ERROR = 3;
	
	
	
	public static String toReadableString(int level) {
		
		if (level == DEBUG) {
			return "DEBUG";
		}
		
		if (level == INFO) {
			return "INFO";
		}
		
		if (level == WARN) {
			return "WARN";
		}
		
		if (level == ERROR) {
			return "ERROR";
		}
		
		return null;
		
	}
	
	

}
