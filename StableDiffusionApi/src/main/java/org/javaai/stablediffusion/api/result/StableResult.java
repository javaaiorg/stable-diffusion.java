package org.javaai.stablediffusion.api.result;

import java.util.ArrayList;
import java.util.List;

public class StableResult<P, I> {
	
	
	private P params;
	
	private List<I> resultImages = new ArrayList<>();

	public P getParams() {
		return params;
	}

	public void setParams(P params) {
		this.params = params;
	}

	public List<I> getResultImages() {
		return resultImages;
	}

	public void setResultImages(List<I> resultImages) {
		this.resultImages = resultImages;
	}


	
	
	
	
	
	
	

}
