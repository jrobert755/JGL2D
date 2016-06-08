package tech.kodiko.jgl2d.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import tech.kodiko.jgl2d.graphics.Texture;

public class ImageLoader {
	public static void convertARGBtoRGBA(int data[]){
		for(int i = 0; i < data.length; i++){
			int value = data[i];
			int alpha = (value & 0xff000000);
			alpha >>= 24;
			alpha &= 0xff;
			value <<= 8;
			value &= 0xffffff00;
			value |= alpha;
			data[i] = value;
		}
	}
	
	public static int[] flipData(int data[], int width, int height){
		int flipped[] = new int[width * height];
		for(int i = 0; i < height; i++){
			int dataOffset = i * width;
			int flippedOffset = (height - 1 - i) * width;
			for(int j = 0; j < width; j++){
				flipped[flippedOffset + j] = data[dataOffset + j];
			}
		}
		
		return flipped;
	}
	
	public static Texture loadTexture(File file){
		try {
			BufferedImage image = ImageIO.read(file);
			int width = image.getWidth();
			int height = image.getHeight();
			int data[] = new int[width * height];
			
			image.getRGB(0, 0, width, height, data, 0, width);
			ImageLoader.convertARGBtoRGBA(data);
			data = ImageLoader.flipData(data, width, height);
			Texture texture = new Texture(data, width, height);
			return texture;
		} catch (IOException e) {
			return null;
		}
	}
}
