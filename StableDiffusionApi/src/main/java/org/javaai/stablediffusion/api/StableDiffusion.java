package org.javaai.stablediffusion.api;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.javaai.stablediffusion.api.enums.GGML_type;
import org.javaai.stablediffusion.api.enums.RNGType;
import org.javaai.stablediffusion.api.enums.SampleMethod;
import org.javaai.stablediffusion.api.enums.Schedule;
import org.javaai.stablediffusion.api.result.Img2ImgParams;
import org.javaai.stablediffusion.api.result.StableResult;
import org.javaai.stablediffusion.api.result.Txt2ImgParams;
import org.javaai.stablediffusion.api.utils.ImageUtils;

public class StableDiffusion implements AutoCloseable {

	private Long pointer;
	

	public static final Integer img_default_width = 512;

	public static final Integer img_default_height = 512;
	
	
	/**
	 * 
	 */
	public StableDiffusion() {
		this(null, null, null, null, null, null, null, null);
	}

	
	
	/**
	 * 
	 * @param n_threads Nullable, default is -1. 
	 * @param vae_decode_only Nullable, default is false. 
	 * @param taesd_path Nullable, default is empty string. 
	 * @param free_params_immediately Nullable, default is false. 
	 * @param lora_model_dir Nullable, default is empty string. 
	 * @param rng_type Nullable, default is {@link RNGType#STD_DEFAULT_RNG}
	 */
	public StableDiffusion(Integer n_threads, 
			Boolean vae_decode_only, 
			String taesd_path, 
			String esrgan_path, 
			Boolean free_params_immediately, 
			Boolean vae_tiling, 
			String lora_model_dir, 
			Integer rng_type) {

		if (n_threads == null) {
			n_threads = -1;
		}

		if (vae_decode_only == null) {
			vae_decode_only = false;
		}

		if (taesd_path == null) {
			taesd_path = "";
		}
		
		if (esrgan_path == null) {
			esrgan_path = "";
		}

		if (free_params_immediately == null) {
			free_params_immediately = false;
		}
		
		if (vae_tiling == null) {
			vae_tiling = false;
		}

		if (lora_model_dir == null) {
			lora_model_dir = "";
		}

		if (rng_type == null) {
			rng_type = RNGType.STD_DEFAULT_RNG;
		}
		
		
		pointer = newInstance(n_threads, vae_decode_only, 
				taesd_path, esrgan_path, free_params_immediately, vae_tiling,
				lora_model_dir, rng_type);
		

	}
	
	
	protected static native long newInstance(int n_threads, boolean vae_decode_only, String taesd_path,
			String esrgan_path, boolean free_params_immediately, boolean vae_tiling, String lora_model_dir, int rng_type);
	
	
	
	protected static native void delete(long pointer);
	
	
	

	@Override
	public void close() throws Exception {
		if (pointer == null) {
			return;
		}
		delete(pointer);
		pointer = null;
	}
	
	
	
	/**
	 * 
	 * 
	 * Download weights<br>
		download original weights(.ckpt or .safetensors). For example<br>
		
		Stable Diffusion v1.4 from https://huggingface.co/CompVis/stable-diffusion-v-1-4-original<br>
		Stable Diffusion v1.5 from https://huggingface.co/runwayml/stable-diffusion-v1-5<br>
		Stable Diffuison v2.1 from https://huggingface.co/stabilityai/stable-diffusion-2-1<br>
	 * <br>
	 * @param model_path NotNull, path to local model path, like /opt/models/sd-v1-4.ckpt or /opt/models/v1-5-pruned-emaonly.safetensors
	 * @param vae_path Nullable, default is empty string. 
	 * @param ggml_type Nullable, default is {@link GGML_type#GGML_TYPE_COUNT}
	 * @param schedule Nullable, default is {@link Schedule#DEFAULT}
	 * @return
	 */
	public boolean loadFromFile(String model_path, String vae_path, Integer ggml_type, Integer schedule) {
		
		if (StringUtils.isBlank(model_path)) {
			throw new IllegalArgumentException("Argument model_path can not be empty/blank. ");
		}
		
		if (vae_path == null) {
			vae_path = "";
		}
		
		if (ggml_type == null) {
			ggml_type = GGML_type.GGML_TYPE_COUNT;
		}
		
		if (schedule == null) {
			schedule = Schedule.DEFAULT;
		}
		
		boolean ret = loadFromFile0(pointer, model_path, vae_path, ggml_type, schedule);
		
		if (!ret) {
			throw new StableDiffusionException("Load model failed! "
					+ "model_path: [" + model_path + "]"
					+ "vae_path: [" + vae_path + "]"
					+ "ggml_type_value: [" + ggml_type + "]"
					+ "schedule: [" + schedule + "]");
		}
		
		return ret;
		
	}
	
	protected static native boolean loadFromFile0(
			long pointer, 
			String model_path, String vae_path, int ggml_type_value, int schedule);
	
	
	

	/**
	 * 
	 * @param prompt NotNull 
	 * @return results already converted to BGR color image.
	 */
	public StableResult<Txt2ImgParams, BufferedImage> txt2img(String prompt) {
		StableResult<Txt2ImgParams, BufferedImage> results = txt2img(prompt, null, 
				null, null, null, null, null, null, null);
		
		return results;
	}
	
	/**
	 * 
	 * @param prompt NotNull 
	 * @param negative_prompt Nullable, default is empty string. 
	 * @param cfg_scale Nullable, default is 7.0f. 
	 * @param width Nullable, default is 512. 
	 * @param height Nullable, default is 512, 
	 * @param sample_method Nullable, default is {@link SampleMethod#EULER_A}
	 * @param sample_steps Nullable, default is 20.  
	 * @param seed Nullable, default is 42L. 
	 * @param batch_count Nullable, default is 1. 
	 * @return results already converted to BGR color image. 
	 */
	public StableResult<Txt2ImgParams, BufferedImage> txt2img(String prompt, String negative_prompt, 
			Float cfg_scale, Integer width, Integer height, Integer sample_method, 
			Integer sample_steps, Long seed, Integer batch_count) {
		
		
		StableResult<Txt2ImgParams, byte[]> pixelImagesResult = txt2PixelsImg(prompt, 
				negative_prompt, cfg_scale, width, 
				height, sample_method, sample_steps, seed, 
				batch_count);
		
		
		StableResult<Txt2ImgParams, BufferedImage> bufferedImageResult = new StableResult<>();
		bufferedImageResult.setParams(pixelImagesResult.getParams());
		
		List<BufferedImage> results = new ArrayList<>(pixelImagesResult.getResultImages().size());
		for (byte[] pixels : pixelImagesResult.getResultImages()) {
			pixels = ImageUtils.reverseRGB(pixels);
			
			BufferedImage image = ImageUtils.pixelsBGRToImage(pixels, 
					pixelImagesResult.getParams().getWidth(), 
					pixelImagesResult.getParams().getHeight());
			results.add(image);
		}
		
		bufferedImageResult.setResultImages(results);
		
		return bufferedImageResult;
		
	}
	

	private Integer makeWidthSafe(Integer width) {

		if (width == null) {
			width = img_default_width;
		}
		
		return width;
	}

	private Integer makeHeightSafe(Integer height) {

		if (height == null) {
			height = img_default_height;
		}
		
		return height;
	}
	
	
	/**
	 * 
	 * @param  Nullable, output param returns image size, left is width, right is height. 
	 * @param prompt NotNull 
	 * @return pixels images, RGB color mode, need convert to GBR color mode by {@link ImageUtils#reverseRGB(byte[])} manually. 
	 */
	public StableResult<Txt2ImgParams, byte[]> txt2PixelsImg(
			String prompt) {
		return txt2PixelsImg(prompt, null, null, null, null, null, null, null, null);
	}
	
	/**
	 * 
	 * @param prompt NotNull 
	 * @param negative_prompt Nullable, default is empty string. 
	 * @param cfg_scale Nullable, default is 7.0f. 
	 * @param width Nullable, default is 512. 
	 * @param height Nullable, default is 512, 
	 * @param sample_method Nullable, default is {@link SampleMethod#EULER_A}
	 * @param sample_steps Nullable, default is 20.  
	 * @param seed Nullable, default is 42L. 
	 * @param batch_count Nullable, default is 1. 
	 * @return pixels images, RGB color mode, need convert to GBR color mode by {@link ImageUtils#reverseRGB(byte[])} manually. 
	 */
	public StableResult<Txt2ImgParams, byte[]> txt2PixelsImg(String prompt, 
			String negative_prompt, Float cfg_scale, Integer width, Integer height, 
			Integer sample_method, Integer sample_steps, Long seed, Integer batch_count) {
		
		if (StringUtils.isBlank(prompt)) {
			throw new IllegalArgumentException("Argument prompt can not be empty/blank. ");
		}
		
		if (negative_prompt == null) {
			negative_prompt = "";
		}
		
		if (cfg_scale == null) {
			cfg_scale = 7.0f;
		}
		
		width = makeWidthSafe(width);
		height = makeHeightSafe(height);
		
		if (sample_method == null) {
			sample_method = SampleMethod.EULER_A;
		}
		
		if (sample_steps == null) {
			sample_steps = 20;
		}
		
		if (seed == null) {
			seed = 42L;
		}
		
		if (batch_count == null) {
			batch_count = 1;
		}
		
		
		List<byte[]> results = txt2img0(pointer, 
				prompt, negative_prompt, cfg_scale, 
				width, height, sample_method, sample_steps, seed, batch_count);
		
		StableResult<Txt2ImgParams, byte[]> stableResult = new StableResult<>();
		Txt2ImgParams params = new Txt2ImgParams();
		params.setPrompt(prompt);
		params.setNegative_prompt(negative_prompt);
		params.setCfg_scale(cfg_scale);
		params.setWidth(width);
		params.setHeight(height);
		params.setSample_method(sample_method);
		params.setSample_steps(sample_steps);
		params.setSeed(seed);
		params.setBatch_count(batch_count);
		
		stableResult.setParams(params);
		stableResult.setResultImages(results);
		
		return stableResult;
	}
	

	protected static native List<byte[]> txt2img0(
			long pointer, 
			String prompt, String negative_prompt, 
			float cfg_scale, int width, int height, int sample_method, 
			int sample_steps, long seed, int batch_count);
	
	
	

	
	/**
	 * 
	 * @param img NotNull
	 * @param prompt NotNull 
	 * @param negative_prompt Nullable, default is empty string. 
	 * @return results already converted to BGR color image.
	 */
	public StableResult<Img2ImgParams, BufferedImage> img2img(BufferedImage img, String prompt, String negative_prompt) {
		
		StableResult<Img2ImgParams, BufferedImage> results = img2img(img, 
				prompt, negative_prompt, null, null, null, null, null, null, null);
		
		return results;
	}
	

	
	/**
	 * 
	 * @param img NotNull BGR color mode image. 
	 * @param prompt NotNull 
	 * @param negative_prompt Nullable, default is empty string. 
	 * @param cfg_scale Nullable, default is 7.0f. 
	 * @param width Nullable, default is 512. 
	 * @param height Nullable, default is 512, 
	 * @param sample_method Nullable, default is {@link SampleMethod#EULER_A}
	 * @param sample_steps Nullable, default is 20.  
	 * @param strength Nullable, default is 0.75f.
	 * @param seed Nullable, default is 42L. 
	 * @return results already converted to BGR color image.
	 */
	public StableResult<Img2ImgParams, BufferedImage> img2img(BufferedImage img, String prompt, String negative_prompt, 
			Float cfg_scale, Integer width, Integer height, Integer sample_method, 
			Integer sample_steps, Float strength, Long seed) {
		

		byte[] pixelsBGR = ImageUtils.imageToPixelsBGR(img);
		pixelsBGR = ImageUtils.reverseRGB(pixelsBGR);
		
		StableResult<Img2ImgParams, byte[]> result = img2img(
				pixelsBGR, 
				prompt, negative_prompt, 
				cfg_scale, width, height, 
				sample_method, sample_steps, strength, seed);
		
		StableResult<Img2ImgParams, BufferedImage> biResult = new StableResult<>();
		biResult.setParams(result.getParams());
		
		List<BufferedImage> bufferedImages = new ArrayList<>();
		for (byte[] byteImage : result.getResultImages()) {
			
			byteImage = ImageUtils.reverseRGB(byteImage);
			
			BufferedImage bufferedImage = ImageUtils.pixelsBGRToImage(byteImage, 
					result.getParams().getWidth(), result.getParams().getHeight());
			bufferedImages.add(bufferedImage);

		}
		
		biResult.setResultImages(bufferedImages);
		
		return biResult;
		
	}
	
	

	/**
	 * 
	 * @param img NotNull, need RGB color mode image, need convert by {@link ImageUtils#reverseRGB(byte[])} manually. 
	 * @param prompt NotNull 
	 * @param negative_prompt Nullable, default is empty string. 
	 * @param  Nullable, output param returns image size, left is width, right is height. 
	 * @return RGB color mode image, need convert by {@link ImageUtils#reverseRGB(byte[])} manually. 
	 */
	public StableResult<Img2ImgParams, byte[]> img2img(byte[] img, String prompt, String negative_prompt) {
		
		StableResult<Img2ImgParams, byte[]> results = img2img(img, prompt, negative_prompt, 
				null, null, null, null, null, null, null);
		
		
		return results;
		
	}
	
	
	/**
	 * 
	 * @param img NotNull, need RGB color mode image, need convert by {@link ImageUtils#reverseRGB(byte[])} manually. 
	 * @param prompt NotNull 
	 * @param negative_prompt Nullable, default is empty string. 
	 * @param cfg_scale Nullable, default is 7.0f. 
	 * @param width Nullable, default is 512. 
	 * @param height Nullable, default is 512, 
	 * @param sample_method Nullable, default is {@link SampleMethod#EULER_A}
	 * @param sample_steps Nullable, default is 20.  
	 * @param strength Nullable, default is 0.75f.
	 * @param seed Nullable, default is 42L. 
	 * @param  Nullable, output param returns image size, left is width, right is height. 
	 * @return RGB color mode image, need convert by {@link ImageUtils#reverseRGB(byte[])} manually. 
	 */
	public StableResult<Img2ImgParams, byte[]> img2img(byte[] img, String prompt, String negative_prompt, 
			Float cfg_scale, Integer width, Integer height, Integer sample_method, 
			Integer sample_steps, Float strength, Long seed) {
		
		
		img = ImageUtils.reverseRGB(img);
		
		if (StringUtils.isBlank(prompt)) {
			throw new IllegalArgumentException("Argument prompt can not be empty/blank. ");
		}
		
		if (negative_prompt == null) {
			negative_prompt = "";
		}
		
		if (cfg_scale == null) {
			cfg_scale = 7.0f;
		}
		
		width = makeWidthSafe(width);
		height = makeHeightSafe(height);
		
		if (sample_method == null) {
			sample_method = SampleMethod.EULER_A;
		}
		
		if (sample_steps == null) {
			sample_steps = 20;
		}
		
		if (seed == null) {
			seed = 42L;
		}
		
		if (strength == null) {
			strength = 0.75f;
		}
		
		
		List<byte[]> resultImages = img2img0(pointer, 
				img,
				prompt, negative_prompt, cfg_scale, 
				width, height, sample_method, sample_steps, strength, seed);
		

		StableResult<Img2ImgParams, byte[]> result = new StableResult<>();
		Img2ImgParams params = new Img2ImgParams();
		params.setPrompt(prompt);
		params.setNegative_prompt(negative_prompt);
		params.setCfg_scale(cfg_scale);
		params.setWidth(width);
		params.setHeight(height);
		params.setSample_method(sample_method);
		params.setSample_steps(sample_steps);
		params.setStrength(strength);
		params.setSeed(seed);
		
		result.setParams(params);
		result.setResultImages(resultImages);
		
		return result;
	}
	

	protected static native List<byte[]> img2img0(
			long pointer, 
			byte[] img,
			String prompt, String negative_prompt, 
			float cfg_scale, int width, int height, int sample_method, 
			int sample_steps, float strength, long seed);
	
	
	
	
	

}
