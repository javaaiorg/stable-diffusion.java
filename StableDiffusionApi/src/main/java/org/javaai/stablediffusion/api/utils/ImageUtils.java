package org.javaai.stablediffusion.api.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.Arrays;

public class ImageUtils {
	
	
	public static BufferedImage pixelsBGRToImage(byte[] imgBytes, int width, int height) {

		BufferedImage img=new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		
		int countPixels = imgBytes.length / 3;
		
		int[] raw = new int[countPixels];
		for (int index = 0; index < countPixels; index++) {
		    raw[index] = 0xFF000000 | // alpha
		        ((imgBytes[3 * index + 2] & 0xFF) << 16) | // red
		        ((imgBytes[3 * index + 1] & 0xFF) << 8) | // green 
		        ((imgBytes[3 * index + 0] & 0xFF)); // blue
		}
		img.setRGB(0, 0, width, height, raw, 0, width);
		return img;
	}
	

	
	public static byte[] imageToPixelsBGR(BufferedImage image) {
		
		DataBufferByte dataBufferByte = ((DataBufferByte) image.getRaster().getDataBuffer());
	    byte[] pixelData = dataBufferByte.getData();
		
		WritableRaster alphaRaster = image.getAlphaRaster();
		int countChannels = 3;
		if (alphaRaster != null) {
			countChannels = 4;
		}
		
		int rgbStartIndex = 0;
		if (alphaRaster != null) {
			rgbStartIndex = 1;
		}
		
		byte[] results = new byte[image.getWidth() * image.getHeight() * countChannels];
		
		for (int row = 0; row < image.getHeight(); row ++) {
			for (int col = 0; col < image.getWidth(); col ++) {
				int resultPixelPos = row * image.getWidth() * 3 + col * 3;
				
				int rawPixelPos = row * image.getWidth() * countChannels + col * countChannels;
				
				byte b = pixelData[rawPixelPos + rgbStartIndex + 0];
				byte g = pixelData[rawPixelPos + rgbStartIndex + 1];
				byte r = pixelData[rawPixelPos + rgbStartIndex + 2];
				
				results[resultPixelPos + 0] = b;
				results[resultPixelPos + 1] = g;
				results[resultPixelPos + 2] = r;
				
				
			}
		}
		
	    
	    return results;
	}
	
	
	
	public static byte[] reverseRGB(byte[] data) {
		
		byte[] results = Arrays.copyOf(data, data.length);
		int pixels = data.length / 3;
		for (int i = 0; i < pixels; i ++) {
			results[i * 3 + 0] = data[i * 3 + 2];
			results[i * 3 + 2] = data[i * 3 + 0];
		}
		
		return results;
	}
	
	
	
	

}
