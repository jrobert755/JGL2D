package com.tangledworlds.jgl2d.graphics.tiles;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.yaml.snakeyaml.Yaml;

import com.tangledworlds.jgl2d.exception.TileMapSizeException;
import com.tangledworlds.jgl2d.graphics.Renderable;
import com.tangledworlds.jgl2d.graphics.Sprite;
import com.tangledworlds.jgl2d.graphics.Texture;
import com.tangledworlds.jgl2d.math.Vector2;
import com.tangledworlds.jgl2d.math.Vector4;
import com.tangledworlds.jgl2d.util.ImageLoader;
import com.tangledworlds.jgl2d.util.ResourceLoader;

public class TileMap extends Renderable {
	private TileSheet tilesheet;
	private float data[];
	
	private Sprite[][] tilesNew;
	
	public TileMap(TileSheet tilesheet, int[][] tiles, int xOffset, int yOffset, Vector4[][] overrideColors){
		this.tilesheet = tilesheet;
		int tileWidth = tilesheet.getTileWidth();
		int tileHeight = tilesheet.getTileHeight();
		
		this.tilesNew = new Sprite[tiles.length][];
		this.data = new float[tiles.length * tiles[0].length * Sprite.floatsPerVertex() * 4];

		for(int j = 0; j < tiles.length; j++){
			if(tiles[j].length != tiles[0].length) throw new TileMapSizeException("Tilemap rows must be the same size!");
			this.tilesNew[j] = new Sprite[tiles[j].length];
			for(int i = 0; i < tiles[j].length; i++){
				Vector2 uvCoords = tilesheet.getTileUV(tiles[j][i]);
				int vertX = i * tileWidth + xOffset;
				int vertY = j * tileHeight + yOffset;
				Sprite sprite = new Sprite(tilesheet.getTexture(), vertX, vertY, tileWidth, tileHeight, (int)uvCoords.getX(), (int)uvCoords.getY(), tileWidth, tileHeight);
				if(overrideColors != null && overrideColors.length > j && overrideColors[j] != null && overrideColors[j].length > i && overrideColors[j][i] != null){
					sprite.setOverrideColor(overrideColors[j][i]);
				}
				this.tilesNew[j][i] = sprite;
				this.updateTile(i, j);
			}
		}
		this.setZ(-1);
	}
	
	public TileMap(TileSheet tilesheet, int[][] tiles){
		this(tilesheet, tiles, 0, 0, null);
	}
	
	public void updateTile(int x, int y){
		int offset = (y * this.tilesNew[0].length) + x;
		offset *= (Sprite.floatsPerVertex() * 4);
		float temp[] = this.tilesNew[y][x].getData();
		for(int i = 0; i < temp.length; i++){
			this.data[offset+i] = temp[i];
		}
	}
	
	@Override
	public float[] getData() {
		return this.data;
	}

	@Override
	public void render(int offset) {
		for(int j = 0; j < this.tilesNew.length; j++){
			Sprite[] curRow = this.tilesNew[j];
			for(int i = 0; i < curRow.length; i++){
				curRow[i].render(offset);
				offset += 4;
			}
		}
	}

	@Override
	public int vertexCount() {
		//return this.tiles.size() * 4;
		return this.tilesNew.length * this.tilesNew[0].length * 4;
	}

	@Override
	public void destroy(){
		/*this.tilesheet.destroy();
		for(int i = 0; i < this.tiles.size(); i++){
			this.tiles.get(i).destroy();
		}*/
		
		for(int j = 0; j < this.tilesNew.length; j++){
			Sprite[] curRow = this.tilesNew[j];
			for(int i = 0; i < curRow.length; i++){
				curRow[i].destroy();
			}
		}
	}
	
	public TileSheet getTileSheet(){
		return this.tilesheet;
	}
	
	public Sprite getTile(int x, int y){
		if(y >= this.tilesNew.length || x >= this.tilesNew[y].length) return null;
		return this.tilesNew[y][x];
	}
	
	@SuppressWarnings("unchecked")
	public static TileMap loadTileMap(String filename){
		try{
			InputStream stream = ResourceLoader.getResourceInputStream(filename);
			Yaml yaml = new Yaml();
			LinkedHashMap<String, ?> attributes = (LinkedHashMap<String, ?>) yaml.load(stream);
			if(!attributes.containsKey("tilemap") || !attributes.containsKey("tileWidth") || !attributes.containsKey("tileHeight") || !attributes.containsKey("tileImage")){
				return null;
			}
			
			int xOffset = 0;
			if(attributes.containsKey("xOffset")){
				xOffset = (int) attributes.get("xOffset");
			}
			int yOffset = 0;
			if(attributes.containsKey("yOffset")){
				yOffset = (int) attributes.get("yOffset");
			}
			
			int tileWidth = (int) attributes.get("tileWidth");
			int tileHeight = (int) attributes.get("tileHeight");
			String tileImage = (String) attributes.get("tileImage");
			
			Texture texture = ImageLoader.loadTexture(ResourceLoader.getResourceFile(tileImage));
			if(texture == null) return null;
			
			Object tilemapTest = attributes.get("tilemap");
			
			ArrayList<ArrayList<LinkedHashMap<String, ?>>> tilemapAttributes = (ArrayList<ArrayList<LinkedHashMap<String, ?>>>)tilemapTest;
			int tiles[][] = new int[tilemapAttributes.size()][];
			Vector4 overrideColors[][] = new Vector4[tilemapAttributes.size()][];
			for(int i = 0; i < tilemapAttributes.size(); i++){
				ArrayList<LinkedHashMap<String, ?>> cur = tilemapAttributes.get(i);
				tiles[i] = new int[cur.size()];
				if(i != 0 && tiles[i].length != tiles[0].length) throw new TileMapSizeException("Tilemap rows must be the same size!");
				overrideColors[i] = new Vector4[cur.size()];
				for(int j = 0; j < cur.size(); j++){
					LinkedHashMap<String, ?> curHashmap = cur.get(j);
					if(!curHashmap.containsKey("tilenumber")) return null;
					tiles[i][j] = (Integer)curHashmap.get("tilenumber");
					if(curHashmap.containsKey("overrideColor")){
						LinkedHashMap<String, Double> colors = (LinkedHashMap<String, Double>) curHashmap.get("overrideColor");
						if(!colors.containsKey("red") || !colors.containsKey("green") || !colors.containsKey("blue") || !colors.containsKey("alpha")){
							overrideColors[i][j] = null;
							continue;
						}
						overrideColors[i][j] = new Vector4(colors.get("red"), colors.get("green"), colors.get("blue"), colors.get("alpha"));
					} else overrideColors[i][j] = null;
				}
			}
			TileSheet tilesheet = new TileSheet(texture, tileWidth, tileHeight);
			TileMap tilemap = new TileMap(tilesheet, tiles, xOffset, yOffset, overrideColors);
			
			return tilemap;
		} catch(ClassCastException e){
			return null;
		}
	}
}
