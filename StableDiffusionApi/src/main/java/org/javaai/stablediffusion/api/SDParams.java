package org.javaai.stablediffusion.api;

public class SDParams {
	
	
	Integer n_threads = -1;
	Integer mode   = SDMode.TXT2IMG;

    String model_path;
    String vae_path;
    String taesd_path;
    Integer wtype = GGML_type.GGML_TYPE_COUNT;
    String lora_model_dir;
    String output_path = "output.png";
    String input_path;

    String prompt;
    String negative_prompt;
    Float cfg_scale = 7.0f;
    Integer width       = 512;
    Integer height      = 512;
    Integer batch_count = 1;

    Integer sample_method = SampleMethod.EULER_A;
    Integer schedule          = Schedule.DEFAULT;
    Integer sample_steps           = 20;
    Float strength             = 0.75f;
    Integer rng_type           = RNGType.CUDA_RNG;
    Long seed               = 42L;
    Boolean verbose               = false;
    
    
    

}
