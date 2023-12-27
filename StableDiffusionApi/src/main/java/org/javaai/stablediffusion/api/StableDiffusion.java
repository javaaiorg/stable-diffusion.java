package org.javaai.stablediffusion.api;

public class StableDiffusion implements AutoCloseable {

	private Long pointer;

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
	
	
	private native long newInstance(int n_threads, boolean vae_decode_only, String taesd_path,
			boolean free_params_immediately, String lora_model_dir, int rng_type);
	
	
	
	private native void delete(long pointer);
	
	
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
	
	
	

}
