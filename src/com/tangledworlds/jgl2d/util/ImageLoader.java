package com.tangledworlds.jgl2d.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.tangledworlds.jgl2d.graphics.Texture;

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
	
	public static Texture loadTexture(File file){
		try {
			BufferedImage image = ImageIO.read(file);
			int width = image.getWidth();
			int height = image.getHeight();
			int data[] = new int[width * height];
			
			image.getRGB(0, 0, width, height, data, 0, width);
			ImageLoader.convertARGBtoRGBA(data);
			Texture texture = new Texture(data, width, height);
			return texture;
		} catch (IOException e) {
			return null;
		}
	}
}
