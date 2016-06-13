package tech.kodiko.jgl2d.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ResourceLoader {
	private static String addResourcePath(String path){
		if(path.startsWith("/resources/")) return path;
		else return "/resources/" + path;
	}
	
	public static InputStream getResourceInputStream(String path){
		path = ResourceLoader.addResourcePath(path);
		return ResourceLoader.class.getResourceAsStream(path);
	}
	
	public static byte[] getResourceFileContents(String path) throws IOException{
		InputStream stream = ResourceLoader.getResourceInputStream(path);
		ArrayList<Byte> alBytes = new ArrayList<Byte>();
		int value = stream.read();
		while(value != -1){
			alBytes.add(new Integer(value).byteValue());
			value = stream.read();
		}
		
		byte bytes[] = new byte[alBytes.size()];
		for(int i = 0; i < alBytes.size(); i++){
			bytes[i] = alBytes.get(i);
		}
		
		return bytes;
	}
	
	public static String getResourceFileContentsAsString(String path) throws IOException{
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
