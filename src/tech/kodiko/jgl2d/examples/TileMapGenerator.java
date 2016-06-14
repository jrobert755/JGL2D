package tech.kodiko.jgl2d.examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TileMapGenerator {
	public static byte[] intToByteArray(int value){
		byte ret[] = new byte[4];
		ret[0] = new Integer((value >> 0) & 0xff).byteValue();
		ret[1] = new Integer((value >> 8) & 0xff).byteValue();
		ret[2] = new Integer((value >> 16) & 0xff).byteValue();
		ret[3] = new Integer((value >> 24) & 0xff).byteValue();
		return ret;
	}
	
	public static void main(String[] args) throws IOException {
		String tilesheetName = "mono";
		int width = 6, height = 4;
		int tiles[] =
			{
					190, 168, 168, 168, 168, 163,
					151,   0,   0,   0,   0, 151,
					151,   0,   0,   0,   0, 151,
					164, 168, 168, 168, 168, 189
			};
		
		
		/*int tiles[] =
			{
				1, 1, 1, 1, 1, 1,
				2, 2, 2, 2, 2, 2,
				3, 3, 3, 3, 3, 3,
				4, 4, 4, 4, 4, 4
			};
		*/
		
		File outFile = new File("./src/resources/tilemaps/mono.dat");
		FileOutputStream out = new FileOutputStream(outFile);
		out.write(tilesheetName.getBytes());
		out.write(new byte[]{0});
		out.write(intToByteArray(width));
		out.write(intToByteArray(height));
		for(int i = 0; i < tiles.length; i++){
			out.write(intToByteArray(tiles[i]));
		}
		out.close();
	}

}
