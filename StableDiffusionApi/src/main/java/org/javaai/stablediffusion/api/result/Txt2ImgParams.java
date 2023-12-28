package org.javaai.stablediffusion.api.result;

public class Txt2ImgParams {

	private String prompt;
	private String negative_prompt;

	private Float cfg_scale;
	private Integer width;
	private Integer height;
	private Integer sample_method;

	private Integer sample_steps;
	private Long seed;
	private Integer batch_count;
	
	
	
	
	
	
	public Txt2ImgParams() {
		super();
	}
	
	
	public String getPrompt() {
		return prompt;
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	public String getNegative_prompt() {
		return negative_prompt;
	}
	public void setNegative_prompt(String negative_prompt) {
		this.negative_prompt = negative_prompt;
	}
	public Float getCfg_scale() {
		return cfg_scale;
	}
	public void setCfg_scale(Float cfg_scale) {
		this.cfg_scale = cfg_scale;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getSample_method() {
		return sample_method;
	}
	public void setSample_method(Integer sample_method) {
		this.sample_method = sample_method;
	}
	public Integer getSample_steps() {
		return sample_steps;
	}
	public void setSample_steps(Integer sample_steps) {
		this.sample_steps = sample_steps;
	}
	public Long getSeed() {
		return seed;
	}
	public void setSeed(Long seed) {
		this.seed = seed;
	}
	public Integer getBatch_count() {
		return batch_count;
	}
	public void setBatch_count(Integer batch_count) {
		this.batch_count = batch_count;
	}
	
	
	

}
