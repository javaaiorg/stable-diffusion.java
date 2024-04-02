package org.javaai.stablediffusion.api;

import org.javaai.stablediffusion.api.enums.SDLogLevel;

public class Util {
	
	
	
	private static SDLogCallback logCallback = null;
	
	
	/**
	 * 
	 * @param logLevel {@link SDLogLevel}
	 */
	public static native void setSDLogLevel(int logLevel);
	
	
	protected static native void enableSDLogCallback();
	
	
	protected static native void disableSDLogCallback();
	

	public static void onSDLogCallback(int SDLogLevel, String file, int line, String log) {
		if (logCallback != null) {
			logCallback.onSDLogCallback(SDLogLevel, file, line, log);
		}
	}
	
	public static void fuckfuckfuckyou2(int i, String s, int i2, String s2) {
		System.out.println(s2);
	}

	/**
	 * 
	 * @param logCallback
	 */
	public static void setSDLogCallback(SDLogCallback logCallback) {
		Util.logCallback = logCallback;
		if (Util.logCallback != null) {
			enableSDLogCallback();
		} else {
			disableSDLogCallback();
		}
	}
	

	public static void removeSDLogCallback(SDLogCallback logCallback) {
		setSDLogCallback(null);
	}
	
	
	
	
	
	
	
	
	
	

}
