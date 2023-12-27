package org.javaai.stablediffusion.api;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.javaai.stablediffusion.api.utils.ImageUtils;

public class StableDiffusion implements AutoCloseable {

	private Long pointer;
	

	public static final Integer img_default_width = 512;

	public static final Integer img_default_height = 512;
	
	

	
	
	/**
	 * 
	 * @param n_threads Nullable, default is -1. 
	 * @param vae_decode_only Nullable, default is false. 
	 * @param taesd_path Nullable, default is empty string. 
	 * @param free_params_immediately Nullable, default is false. 
	 * @param lora_model_dir Nullable, default is empty string. 
	 * @param rng_type Nullable, default is {@link RNGType#STD_DEFAULT_RNG}
	 */
	public StableDiffusion(Integer n_threads, Boolean vae_decode_only, String taesd_path,
			Boolean free_params_immediately, String lora_model_dir, Integer rng_type) {

		if (n_threads == null) {
			n_threads = -1;
		}

		if (vae_decode_only == null) {
			vae_decode_only = false;
		}

		if (taesd_path == null) {
			taesd_path = "";
		}

		if (free_params_immediately == null) {
			free_params_immediately = false;
		}

		if (lora_model_dir == null) {
			lora_model_dir = "";
		}

		if (rng_type == null) {
			rng_type = RNGType.STD_DEFAULT_RNG;
		}
		
		
		pointer = newInstance(n_threads, vae_decode_only, taesd_path, free_params_immediately, lora_model_dir, rng_type);
		

	}
	
	
	protected static native long newInstance(int n_threads, boolean vae_decode_only, String taesd_path,
			boolean free_params_immediately, String lora_model_dir, int rng_type);
	
	
	
	protected static native void delete(long pointer);
	
	
	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}


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
	 * @param negative_prompt Nullable, default is empty string. 
	 * @param cfg_scale Nullable, default is 7.0f. 
	 * @param width Nullable, default is 512. 
	 * @param height Nullable, default is 512, 
	 * @param sample_method Nullable, default is {@link SampleMethod#EULER_A}
	 * @param sample_steps Nullable, default is 20.  
	 * @param seed Nullable, default is 42L. 
	 * @param batch_count Nullable, default is 1. 
	 * @return
	 */
	public List<BufferedImage> txt2img(String prompt, String negative_prompt, 
			Float cfg_scale, Integer width, Integer height, Integer sample_method, 
			Integer sample_steps, Long seed, Integer batch_count) {
		
		width = makeWidthSafe(width);
		height = makeHeightSafe(height);
		
		List<byte[]> pixelImages = txt2PixelsImg(prompt, negative_prompt, cfg_scale, width, height, sample_method, sample_steps, seed, batch_count);
		
		List<BufferedImage> results = new ArrayList<>(pixelImages.size());
		for (byte[] pixels : pixelImages) {
			BufferedImage image = ImageUtils.pixelsBGRToImage(pixels, width, height);
			results.add(image);
		}
		
		return results;
		
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
	 * @param prompt NotNull 
	 * @param negative_prompt Nullable, default is empty string. 
	 * @param cfg_scale Nullable, default is 7.0f. 
	 * @param width Nullable, default is 512. 
	 * @param height Nullable, default is 512, 
	 * @param sample_method Nullable, default is {@link SampleMethod#EULER_A}
	 * @param sample_steps Nullable, default is 20.  
	 * @param seed Nullable, default is 42L. 
	 * @param batch_count Nullable, default is 1. 
	 * @return
	 */
	public List<byte[]> txt2PixelsImg(String prompt, String negative_prompt, 
			Float cfg_scale, Integer width, Integer height, Integer sample_method, 
			Integer sample_steps, Long seed, Integer batch_count) {
		
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
		
		
		return results;
	}
	

	protected static native List<byte[]> txt2img0(
			long pointer, 
			String prompt, String negative_prompt, 
			float cfg_scale, int width, int height, int sample_method, 
			int sample_steps, long seed, int batch_count);
	
	
	
	
	

}
