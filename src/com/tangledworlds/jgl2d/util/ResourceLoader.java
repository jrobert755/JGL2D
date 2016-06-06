package com.tangledworlds.jgl2d.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

public class ResourceLoader {
	private static String addResourcePath(String path){
		if(path.startsWith("resources/")) return path;
		else return "resources/" + path;
	}
	
	public static File getResourceFile(String path){
		path = ResourceLoader.addResourcePath(path);
		ClassLoader loader = ResourceLoader.class.getClassLoader();
		URL url = loader.getResource(path);
		
		File file = new File(url.getFile());
		return file;
	}
	
	public static InputStream getResourceInputStream(String path){
		path = ResourceLoader.addResourcePath(path);
		File file = ResourceLoader.getResourceFile(path);
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	public static String getResourceFileContents(String path) throws IOException{
		File file = ResourceLoader.getResourceFile(path);
		return new String(Files.readAllBytes(file.toPath()));
	}
}
