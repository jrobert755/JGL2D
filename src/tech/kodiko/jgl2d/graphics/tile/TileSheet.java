package tech.kodiko.jgl2d.graphics.tile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.yaml.snakeyaml.Yaml;

import tech.kodiko.jgl2d.graphics.Sprite;
import tech.kodiko.jgl2d.graphics.Texture;
import tech.kodiko.jgl2d.math.Vector4;
import tech.kodiko.jgl2d.util.ImageLoader;
import tech.kodiko.jgl2d.util.ResourceLoader;

public class TileSheet {
	private HashMap<Integer, Tile> tiles;
	private Texture texture;
	private static HashMap<String, TileSheet> tilesheets = new HashMap<String, TileSheet>();
	private int tileWidth, tileHeight;
	
	public TileSheet(HashMap<Integer, Tile> tiles, Texture texture, int tileWidth, int tileHeight) {
		this.tiles = tiles;
		this.texture = texture;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}
	
	public Tile getTile(int tileNumber){
		try{
			return tiles.get(tileNumber);
		} catch(IndexOutOfBoundsException e){
			return null;
		}
	}
	
	public Sprite generateSpriteFromTile(int tileNumber, int x, int y, int width, int height){
		Tile tile = this.getTile(tileNumber);
		if(tile == null) return null;
		return tile.generateSprite(x, y, width, height);
	}
	
	public void destroy(){
		this.texture.destroy();
	}
	
	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}
	
	public static void destroyAll(){
		for(Entry<String, TileSheet> entry : TileSheet.tilesheets.entrySet()){
			TileSheet ts = entry.getValue();
			ts.destroy();
		}
	}
	
	public static void reloadTileSheet(String tilesheetName){
		TileSheet.tilesheets.remove(tilesheetName);
		TileSheet.loadTileSheet(tilesheetName);
	}

	@SuppressWarnings("unchecked")
	public static TileSheet loadTileSheet(String tilesheetName){
		try{
			TileSheet tilesheet = TileSheet.tilesheets.get(tilesheetName);
			if(tilesheet != null) return tilesheet;
			String filename = "tilesheets/" + tilesheetName + ".yml";
			InputStream stream = ResourceLoader.getResourceInputStream(filename);
			if(stream == null) return null;
			Yaml yaml = new Yaml();
			LinkedHashMap<String, ?> attributes = (LinkedHashMap<String, ?>) yaml.load(stream);

			int tileWidth = (Integer) attributes.get("tileWidth");
			int tileHeight = (Integer) attributes.get("tileHeight");
			String textureSheet = (String) attributes.get("textureSheet");
			
			Texture texture = ImageLoader.loadTexture(ResourceLoader.getResourceInputStream(textureSheet));
			if(texture == null) return null;
			
			int numberOfTiles = (Integer) attributes.get("numberOfTiles");
			
			int curUvX = 0;
			int curUvY = 0;
			int textureWidth = texture.getWidth();
			
			HashMap<Integer, Tile> tiles = new HashMap<Integer, Tile>();
			
			for(int i = 0; i < numberOfTiles; i++){
				Tile tile = new Tile(texture, curUvX, curUvY, tileWidth, tileHeight);
				
				curUvX += tileWidth;
				if(curUvX >= textureWidth){
					curUvX = 0;
					curUvY += tileHeight;
				}
				tiles.put(i, tile);
			}
			
			//Object additionalTiles = attributes.get("additionalTiles");
			ArrayList<Object> additionalTiles = (ArrayList<Object>) attributes.get("additionalTiles");
			if(additionalTiles != null){
				for(int i = 0; i < additionalTiles.size(); i++){
					LinkedHashMap<String, ?> additional = (LinkedHashMap<String, ?>) additionalTiles.get(i);
					
					Integer originalStart = (Integer) additional.get("originalStart");
					if(originalStart == null) originalStart = 0;
					Integer originalEnd = (Integer) additional.get("originalEnd");
					if(originalEnd == null || originalEnd == 0) originalEnd = numberOfTiles;
					if(originalStart >= originalEnd && originalStart != 0) continue;
					
					ArrayList<Integer> originalTiles = (ArrayList<Integer>) additional.get("originalTiles");
					if(originalTiles == null){
						originalTiles = new ArrayList<Integer>();
						for(int j = originalStart; j < originalEnd; j++){
							originalTiles.add(j);
						}
					}
					
					if(originalTiles.size() == 0) continue;
					
					LinkedHashMap<String, Double> oc = (LinkedHashMap<String, Double>) additional.get("overrideColor");
					Vector4 overrideColor = new Vector4();
					if(oc != null && oc.containsKey("red") && oc.containsKey("green") && oc.containsKey("blue") && oc.containsKey("alpha")){
						double red = oc.get("red");
						double green = oc.get("green");
						double blue = oc.get("blue");
						double alpha = oc.get("alpha");
						overrideColor.setXYZW((float)red, (float)green, (float)blue, (float)alpha);
					}
					int start = tiles.size();
					
					for(int j = 0; j < originalTiles.size(); j++){
						int tilenum = j + start;
						Tile tile = tiles.get(j + originalTiles.get(j));
						if(tile == null) break;
						Tile newTile = new Tile(tile, overrideColor);
						tiles.put(tilenum, newTile);
					}
				}
			}
			
			tilesheet = new TileSheet(tiles, texture, tileWidth, tileHeight);
			TileSheet.tilesheets.put(tilesheetName, tilesheet);
			return tilesheet;
		} catch(ClassCastException e){
			return null;
		}
	}
}
