package org.javaai.stablediffusion.api;

import org.apache.commons.lang3.StringUtils;

public class StableDiffusion implements AutoCloseable {

	private Long pointer;

	
	
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
	
	
	
	
	
	
	

}
