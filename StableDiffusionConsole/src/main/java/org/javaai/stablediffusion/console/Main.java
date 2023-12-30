package org.javaai.stablediffusion.console;

import org.javaai.stablediffusion.api.StableDiffusion;
import org.javaai.stablediffusion.api.StableDiffusionLoader;

public class Main {
	
	
	public static void main(String[] args) throws Exception {
		
		StableDiffusionLoader.loadShared();
		
		StableDiffusion sd = new StableDiffusion();
		
		
		sd.close();
		
		System.out.println("success");
		
	}

}
