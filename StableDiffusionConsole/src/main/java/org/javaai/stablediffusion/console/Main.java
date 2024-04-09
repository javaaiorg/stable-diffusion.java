package org.javaai.stablediffusion.console;

import org.javaai.stablediffusion.api.SDLogCallback;
import org.javaai.stablediffusion.api.StableDiffusion;
import org.javaai.stablediffusion.api.StableDiffusionLoader;
import org.javaai.stablediffusion.api.Util;

public class Main {
	
	
	public static void main(String[] args) throws Exception {
		
		StableDiffusionLoader.loadShared();
		
		StableDiffusion sd = new StableDiffusion();
		Util.setSDLogCallback(new SDLogCallback() {
			
			@Override
			public void onSDLogCallback(int SDLogLevel, String file, int line, String log) {
				System.out.println(log);
			}
		});
		
		boolean loadFromFile = sd.loadFromFile("/Users/u/Desktop/models/sd-v1-4.ckpt", null, null, null);
		
		System.out.println("load:" + loadFromFile);
		sd.txt2img("test");
		
		
		sd.close();
		
		System.out.println("success");
		
	}

}
