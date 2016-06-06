package tech.kodiko.jgl2d.graphics.tiles;

import java.util.ArrayList;

import tech.kodiko.jgl2d.graphics.Texture;
import tech.kodiko.jgl2d.math.Vector2;

public class TileSheet {
	private Texture tilesheetTexture;
	private int tileWidth, tileHeight;
	private float uvWidth, uvHeight;
	private ArrayList<Vector2> uvMappings;
	
	public TileSheet(Texture tilesheetTexture, int tileWidth, int tileHeight){
		this.tilesheetTexture = tilesheetTexture;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		
		this.uvMappings = new ArrayList<Vector2>();
		
		int tilesheetWidth = tilesheetTexture.getWidth();
		int tilesheetHeight = tilesheetTexture.getHeight();
		int numTilesWidth = tilesheetWidth / tileWidth;
		int numTilesHeight = tilesheetHeight / tileHeight;
		this.uvWidth = (new Float(tileWidth)) / (new Float(tilesheetWidth));
		this.uvHeight = (new Float(tileHeight)) / (new Float(tilesheetHeight));
		for(int j = 0; j < numTilesHeight; j++){
			//float uvY = (new Float(j)) / (new Float(tilesheetHeight));
			int uvY = j * tileHeight;
			
			for(int i = 0; i < numTilesWidth; i++){
				//float uvX = (new Float(i)) / (new Float(tilesheetWidth));
				int uvX = i * tileWidth;
				
				this.uvMappings.add(new Vector2(uvX, uvY));
			}
		}
	}
	
	public Vector2 getTileUV(int tileNumber){
		if(tileNumber >= this.uvMappings.size()) return null;
		else return this.uvMappings.get(tileNumber);
	}

	public float getUvWidth() {
		return this.uvWidth;
	}

	public float getUvHeight() {
		return this.uvHeight;
	}
	
	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public void bindTexture(){
		if(this.tilesheetTexture != null) this.tilesheetTexture.bind();
	}
	
	public void unbindTexture(){
		if(this.tilesheetTexture != null) this.tilesheetTexture.unbind();
	}
	
	public void destroy(){
		if(this.tilesheetTexture != null) this.tilesheetTexture.destroy();
	}

	public Texture getTexture() {
		return this.tilesheetTexture;
	}
}
