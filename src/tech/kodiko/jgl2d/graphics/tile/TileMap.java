package tech.kodiko.jgl2d.graphics.tile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map.Entry;

import org.lwjgl.BufferUtils;

import tech.kodiko.jgl2d.graphics.Renderable;
import tech.kodiko.jgl2d.graphics.Sprite;
import tech.kodiko.jgl2d.util.ResourceLoader;

public class TileMap extends Renderable{
	private static HashMap<String, TileMap> tilemaps = new HashMap<String, TileMap>();
	
	private TileSheet tilesheet;
	private float data[];
	
	private Sprite[][] sprites;
	private int[][] tiles;
	private int xOffset, yOffset;
	
	public TileMap(TileSheet tilesheet2, int[][] tiles, int xOffset, int yOffset){
		this.tilesheet = tilesheet2;
		
		this.sprites = new Sprite[tiles.length][tiles[0].length];
		this.tiles = tiles;
		this.data = new float[this.vertexCount() * Sprite.floatsPerVertex()];
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		int tileWidth = tilesheet.getTileWidth();
		int tileHeight = tilesheet.getTileHeight();

		for(int j = 0; j < sprites.length; j++){
			for(int i = 0; i < sprites[j].length; i++){
				int x = i * tileWidth;
				int y = j * tileHeight;
				
				sprites[j][i] = tilesheet.generateSpriteFromTile(tiles[j][i], x, y, tileWidth, tileHeight);
				this.updateTile(i, j);
			}
		}
		this.setZ(-1);
	}

	public void updateTile(int x, int y){
		synchronized(this){
			int offset = (y * this.sprites[0].length) + x;
			offset *= (Sprite.floatsPerVertex() * 4);
			float temp[] = this.sprites[y][x].getData();
			for(int i = 0; i < temp.length; i++){
				if((i % 8) == 0) temp[i] += this.xOffset;
				else if((i % 8) == 1) temp[i] += this.yOffset;
				this.data[offset+i] = temp[i];
			}
		}
	}
	
	public void changeTileNumber(int x, int y, int newTileNumber){
		Sprite sprite = this.sprites[y][x];
		Tile tile = this.tilesheet.getTile(newTileNumber);
		tile.updateSprite(sprite);
		this.tiles[y][x] = newTileNumber;
		this.updateTile(x, y);
	}
	
	@Override
	public float[] getData() {
		synchronized(this){
			return this.data;
		}
	}

	@Override
	public void render(int offset) {
		synchronized(this){
			for(int j = 0; j < this.sprites.length; j++){
				Sprite[] curRow = this.sprites[j];
				for(int i = 0; i < curRow.length; i++){
					curRow[i].render(offset);
					offset += 4;
				}
			}
		}
	}

	@Override
	public int vertexCount() {
		return this.sprites.length * this.sprites[0].length * 4;
	}

	@Override
	public void destroy(boolean destroyTexture) {
		synchronized(this){
			for(int i = 0; i < sprites.length; i++){
				for(int j = 0; j < sprites[i].length; j++){
					sprites[i][j].destroy(false);
				}
			}
			this.sprites = null;
			this.data = null;
			this.tiles = null;
		}
	}
	
	public int getWidth(){
		return tilesheet.getTileWidth() * this.sprites[0].length;
	}
	
	public int getHeigth(){
		return tilesheet.getTileHeight() * this.sprites.length;
	}
	
	public static void destroyAll(){
		for(Entry<String, TileMap> entry : TileMap.tilemaps.entrySet()){
			TileMap tm = entry.getValue();
			tm.destroy(false);
		}
	}
	
	public static TileMap reloadTileMap(String tilemapName){
		TileMap.tilemaps.remove(tilemapName);
		return TileMap.loadTileMap(tilemapName);
	}
	
	public static TileMap loadTileMap(String tilemapName){
		TileMap tilemap = TileMap.tilemaps.get(tilemapName);
		if(tilemap != null) return tilemap;
		
		String filename = "tilemaps/" + tilemapName + ".dat";
		byte data[];
		try {
			data = ResourceLoader.getResourceFileContents(filename);
		} catch (IOException e) {
			return null;
		}
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		String tilesheetName = "";
		byte value = buffer.get();
		while(value != 0){
			tilesheetName += (char)value;
			value = buffer.get();
		}
		
		TileSheet tilesheet = TileSheet.loadTileSheet(tilesheetName);
		if(tilesheet == null) return null;
		
		int width = buffer.getInt();
		int height = buffer.getInt();
		
		int tiles[][] = new int[height][width];
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				tiles[i][j] = buffer.getInt();
			}
		}

		tilemap = new TileMap(tilesheet, tiles, 0, 0);
		TileMap.tilemaps.put(tilemapName, tilemap);
		return tilemap;
	}
}
