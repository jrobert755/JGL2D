package tech.kodiko.jgl2d.util;

import java.io.IOException;
import java.io.InputStream;

public class ResourceLoader {
	private static String addResourcePath(String path){
		if(path.startsWith("/resources/")) return path;
		else return "/resources/" + path;
	}
	
	public static InputStream getResourceInputStream(String path){
		path = ResourceLoader.addResourcePath(path);
		return ResourceLoader.class.getResourceAsStream(path);
	}
	
	public static String getResourceFileContents(String path) throws IOException{
		InputStream stream = ResourceLoader.getResourceInputStream(path);
		int value = stream.read();
		String returnString = "";
		while(value != -1){
			returnString += ((char)value);
			value = stream.read();
		}
		return returnString;
		
		//return new String(Files.readAllBytes(file.toPath()));
	}
}
