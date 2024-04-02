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
	

	public static void removeSDLogCallback() {
		setSDLogCallback(null);
	}
	
	
	
	
	
	
	
	
	
	

}
