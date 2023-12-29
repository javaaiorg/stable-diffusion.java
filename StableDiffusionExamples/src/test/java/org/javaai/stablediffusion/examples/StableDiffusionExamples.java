package org.javaai.stablediffusion.examples;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.javaai.stablediffusion.api.StableDiffusion;
import org.javaai.stablediffusion.api.StableDiffusionLoader;
import org.javaai.stablediffusion.api.Util;
import org.javaai.stablediffusion.api.enums.SDLogLevel;
import org.javaai.stablediffusion.api.result.Img2ImgParams;
import org.javaai.stablediffusion.api.result.StableResult;
import org.javaai.stablediffusion.api.result.Txt2ImgParams;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class StableDiffusionExamples {
	

	/**
	 * modelPath example: 
	 * changeme/pathToStableModel/v2-1_768-ema-pruned.ckpt
	 * 
	 * You can download models by the following links: 
	 * 		
		Stable Diffusion v1.4 from https://huggingface.co/CompVis/stable-diffusion-v-1-4-original<br>
		Stable Diffusion v1.5 from https://huggingface.co/runwayml/stable-diffusion-v1-5<br>
		Stable Diffuison v2.1 from https://huggingface.co/stabilityai/stable-diffusion-2-1<br>
	 * 
	 * 
	 */
	public static final String modelPath = 
			"changeme/pathToStableModel/v2-1_768-ema-pruned.ckpt";
	

	
	/**
	 * loraModelPath example: 
	 * changeme/pathToLora/pytorch_lora_weights.safetensors
	 * 
	 * You can download lora models by the following links: 
	 * 
	 * https://huggingface.co/latent-consistency/lcm-lora-sdv1-5
	 * 
	 * ERROR: 
	 * Don't use lora 
	 * original stable-diffusion.cpp project not support lora for now, this example will trigger assertion error: 
	 * Assertion failed: n_dims >= 1 && n_dims <= GGML_MAX_DIMS, file stable-diffusion.cpp\ggml\src\ggml.c, line 2450
	 * 
	 * 
	 */
	public static final String loraModelPath = 
			"changeme/pathToLora/pytorch_lora_weights.safetensors";

	/**
	 * outputDir path example: 
	 * changeme/pathToOutputDirectory
	 * 
	 * 
	 * 
	 */
	public static final String outputDir = 
			"changeme/pathToOutputDirectory";
	
	
	
	
	public static void loadLibrary() {
		StableDiffusionLoader.loadShared();
	}
	
	
	
	//@Test
	void testLoadLibrary() throws Exception {
		
		loadLibrary();
		
		
	}
	
	
	@BeforeAll
	public static void beforeAll() {
		loadLibrary();
		
		File file = new File(outputDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		
	}
	
	
	public static StableDiffusion createStableDifussionInstance() {
		StableDiffusion stableDiffusion = new StableDiffusion();
		
		return stableDiffusion;
	}

	
	@Test
	void testCreateStableDifussionInstance() throws Exception {
		
		StableDiffusion sd = createStableDifussionInstance();
		
		sd.close();
		
	}
	
	
	
	public static StableDiffusion createStableDifussionInstanceWithLora() {
		StableDiffusion stableDiffusion = new StableDiffusion(null, null, null, null, null, null, loraModelPath, null);
		
		return stableDiffusion;
	}

	
	@Test
	void testCreateStableDifussionInstanceWithLora() throws Exception {
		
		StableDiffusion sd = createStableDifussionInstanceWithLora();
		
		sd.close();
		
	}
	
	
	
	
	
	public static StableDiffusion loadModel(StableDiffusion sd) {
		
		
		boolean successful = sd.loadFromFile(modelPath, null, null, null);
		
		
		return sd;
	}


	@Test
	void testLoadModel() throws Exception {
		
		StableDiffusion sd = createStableDifussionInstance();
		
		try {
			loadModel(sd);
		} finally {
			sd.close();
		}
		
		
	}
	

	@Test
	void testLoadModelWithLora() throws Exception {
		
		StableDiffusion sd = createStableDifussionInstanceWithLora();
		
		try {
			loadModel(sd);
		} finally {
			sd.close();
		}
		
		
	}
	
	

	@Test
	void testTxt2Img() throws Exception {
		
		StableDiffusion sd = createStableDifussionInstance();
		
		try {
			loadModel(sd);
			
			StableResult<Txt2ImgParams, BufferedImage> imgs = sd.txt2img("a lovely cat", 
					null, null, null, null, null, 20, 
					(long)(Math.random() * Long.MAX_VALUE), null);
			

			for (int i = 0; i < imgs.getResultImages().size(); i ++) {
				BufferedImage img = imgs.getResultImages().get(i);
				
				ImageIO.write(img, "jpg", new File(outputDir, "sd-java-txt2img-" + i + ".jpg"));
			}
		} finally {
			sd.close();
		}
		
		
	}
	

	
	/**
	 * ERROR: 
	 * Don't use lora 
	 * original stable-diffusion.cpp project not support lora for now, this example will trigger assertion error: 
	 * Assertion failed: n_dims >= 1 && n_dims <= GGML_MAX_DIMS, file stable-diffusion.cpp\ggml\src\ggml.c, line 2450
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	void testTxt2ImgWithLora() throws Exception {
		
		StableDiffusion sd = createStableDifussionInstanceWithLora();
		
		try {
			loadModel(sd);
			
			StableResult<Txt2ImgParams, BufferedImage> imgs = sd.txt2img(
					"a lovely cat<lora:lcm-lora-sdv1-5:1>", 
					null, 1F, null, null, null, 4, 
					(long)(Math.random() * Long.MAX_VALUE), null);
			

			for (int i = 0; i < imgs.getResultImages().size(); i ++) {
				BufferedImage img = imgs.getResultImages().get(i);
				
				ImageIO.write(img, "jpg", new File(outputDir, "sd-java-txt2img-lora-" + i + ".jpg"));
			}
		} finally {
			sd.close();
		}
		
		
	}
	
	


	@Test
	void testImg2Img() throws Exception {
		
		StableDiffusion sd = createStableDifussionInstance();
		try {

			loadModel(sd);

			BufferedImage inputImg = ImageIO.read(new File(outputDir, "sd-java-txt2img-0.jpg"));
			
			StableResult<Img2ImgParams, BufferedImage> imgs = sd.img2img(inputImg, 
					"a lovely cat with blue eyes", 
					null, null, null, null, null, 20, null, 
					(long)(Math.random() * Long.MAX_VALUE));
			

			for (int i = 0; i < imgs.getResultImages().size(); i ++) {
				BufferedImage img = imgs.getResultImages().get(i);
				
				ImageIO.write(img, "jpg", new File(outputDir, "sd-java-img2img-" + i + ".jpg"));
			}
		} finally {
			sd.close();
		}
		
		
	}
	
	
	@Test
	void testChangeLogLevel() throws Exception {
		Util.setSDLogLevel(SDLogLevel.WARN);
		StableDiffusion sd = createStableDifussionInstance();
		try {
			loadModel(sd);
		} finally {
			sd.close();
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
