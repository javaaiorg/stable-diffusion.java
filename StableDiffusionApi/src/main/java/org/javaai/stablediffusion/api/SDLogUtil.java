package org.javaai.stablediffusion.api;

import org.apache.commons.io.FilenameUtils;
import org.javaai.stablediffusion.api.enums.SDLogLevel;

public class SDLogUtil {
	
	
	public static String formatLog(int level, String file, int line, String log) {

		String levelStr = SDLogLevel.toReadableString(level);
		String format = "[" + levelStr + "] " + FilenameUtils.getName(file) + ":" + String.format("%-4d", line) + " - " + log;
		
		return format;
	}
	
	

}
