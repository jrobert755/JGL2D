package tech.kodiko.jgl2d.graphics.tile;

import tech.kodiko.jgl2d.graphics.Sprite;
import tech.kodiko.jgl2d.graphics.Texture;
import tech.kodiko.jgl2d.math.Vector4;

public class Tile {
	private int uvX, uvY;
	private int uvWidth, uvHeight;
	private Vector4 overrideColor;
	private Texture texture;
	
	public Tile(Texture texture, int uvX, int uvY, int uvWidth, int uvHeight) {
		this.texture = texture;
		this.uvX = uvX;
		this.uvY = uvY;
		this.uvWidth = uvWidth;
		this.uvHeight = uvHeight;
	}
	
	public Tile(Texture texture, int uvX, int uvY, int uvWidth, int uvHeight, Vector4 overrideColor) {
		this(texture, uvX, uvY, uvWidth, uvHeight);
		this.overrideColor = overrideColor;
	}
	
	public Tile(Tile tile, Vector4 overrideColor){
		this.texture = tile.texture;
		this.uvX = tile.uvX;
		this.uvY = tile.uvY;
		this.uvWidth = tile.uvWidth;
		this.uvHeight = tile.uvHeight;
		this.overrideColor = overrideColor;
	}
	
	public Sprite generateSprite(int x, int y, int width, int height){
		Sprite sprite = new Sprite(this.texture, x, y, width, height, this.uvX, this.uvY, this.uvWidth, this.uvHeight);
		if(this.overrideColor != null) sprite.setOverrideColor(this.overrideColor);
		return sprite;
	}
}
