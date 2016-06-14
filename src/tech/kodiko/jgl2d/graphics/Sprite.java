package tech.kodiko.jgl2d.graphics;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import tech.kodiko.jgl2d.exception.TextureNotGeneratedException;
import tech.kodiko.jgl2d.math.Matrix22;
import tech.kodiko.jgl2d.math.Vector2;
import tech.kodiko.jgl2d.math.Vector4;

public class Sprite extends Renderable{
	private Texture texture;
	private FloatBuffer buffer;
	
	private float vertX, vertY, vertWidth, vertHeight;
	private float uvX, uvY, uvMaxX, uvMaxY;
	private float rotation;
	private boolean clockwiseRotation;
	private Vector4 overrideColor;
	private boolean cameraIndependent;
	
	/*
	 * When using GL_TRIANGLE_STRIP, do the vertices in the following order:
	 * 
	 *  C ----------- D
	 *  |             |
	 *  |             |
	 *  |             |
	 *  |             |
	 *  |             |
	 *  A ----------- B
	 */
	
	public Sprite(Texture texture, int vertX, int vertY, int vertWidth, int vertHeight, int uvX, int uvY, int uvWidth, int uvHeight){
		this.texture = texture;
		//this.buffer = BufferUtils.createFloatBuffer(8*4);
		this.buffer = BufferUtils.createFloatBuffer(Sprite.floatsPerVertex()*4);
		this.vertX = vertX;
		this.vertY = vertY;
		this.vertWidth = vertWidth;
		this.vertHeight = vertHeight;
		this.setUV(uvX, uvY, uvWidth, uvHeight);
		this.rotation = 0;
		this.clockwiseRotation = false;
		this.overrideColor = new Vector4(1f, 1f, 1f, 0f);
		this.cameraIndependent = false;
		this.updateBuffer();
	}
	
	public Sprite(Texture texture, int vertX, int vertY, int vertWidth, int vertHeight, int uvX, int uvY){
		this(texture, vertX, vertY, vertWidth, vertHeight, uvX, uvY, texture.getWidth(), texture.getHeight());
	}
	
	public Sprite(Texture texture, int vertX, int vertY, int vertWidth, int vertHeight){
		this(texture, vertX, vertY, vertWidth, vertHeight, 0, 0, texture.getWidth(), texture.getHeight());
	}
	
	public Sprite(Texture texture, int vertX, int vertY){
		this(texture, vertX, vertY, texture.getWidth(), texture.getHeight(), 0, 0, texture.getWidth(), texture.getHeight());
	}
	
	public Sprite(Texture texture){
		this(texture, 0, 0, texture.getWidth(), texture.getHeight(), 0, 0, texture.getWidth(), texture.getHeight());
	}
	
	private void updateBuffer(){
		synchronized(this){
			int vertMaxX = Math.round(vertWidth + vertX);
			int vertMaxY = Math.round(vertHeight + vertY);
			
			Vector2 vertexOne, vertexTwo, vertexThree, vertexFour;
			if(this.rotation != 0f){
				float xOrigin = this.vertWidth / 2 + this.vertX;
				float yOrigin = this.vertHeight / 2 + this.vertY;
				float rot = this.clockwiseRotation ? -this.rotation : this.rotation;
				vertexOne = Matrix22.rotate(this.vertX, xOrigin, vertMaxY, yOrigin, rot);
				vertexTwo = Matrix22.rotate(vertMaxX, xOrigin, vertMaxY, yOrigin, rot);
				vertexThree = Matrix22.rotate(this.vertX, xOrigin, this.vertY, yOrigin, rot);
				vertexFour = Matrix22.rotate(vertMaxX, xOrigin, this.vertY, yOrigin, rot);
			} else{
				vertexOne = new Vector2(this.vertX, vertMaxY);
				vertexTwo = new Vector2(vertMaxX, vertMaxY);
				vertexThree = new Vector2(this.vertX, this.vertY);
				vertexFour = new Vector2(vertMaxX, this.vertY);
			}
			
			Vector2 uvOne, uvTwo, uvThree, uvFour;
			uvOne = new Vector2(texture.normalizeX(this.uvX), texture.normalizeYFlipped(this.uvMaxY));
			uvTwo = new Vector2(texture.normalizeX(this.uvMaxX), texture.normalizeYFlipped(this.uvMaxY));
			uvThree = new Vector2(texture.normalizeX(this.uvX), texture.normalizeYFlipped(this.uvY));
			uvFour = new Vector2(texture.normalizeX(this.uvMaxX), texture.normalizeYFlipped(this.uvY));
			
			buffer.put(Sprite.packVertex(vertexOne, uvOne, overrideColor, this.cameraIndependent));
			buffer.put(Sprite.packVertex(vertexTwo, uvTwo, overrideColor, this.cameraIndependent));
			buffer.put(Sprite.packVertex(vertexThree, uvThree, overrideColor, this.cameraIndependent));
			buffer.put(Sprite.packVertex(vertexFour, uvFour, overrideColor, this.cameraIndependent));
			
			buffer.flip();
		}
	}
	
	public void addPosition(Vector2 position){
		this.addPosition(position.getX(), position.getY());
	}
	
	public void addPosition(float x, float y){
		synchronized(this){
			vertX += x;
			vertY += y;
			this.updateBuffer();
		}
	}
	
	public void subtractPosition(Vector2 position){
		this.subtractPosition(position.getX(), position.getY());
	}
	
	public void subtractPosition(float x, float y){
		synchronized(this){
			vertX -= x;
			vertY -= y;
			this.updateBuffer();
		}
	}
	
	@Override
	public float[] getData(){
		synchronized(this){
			float arr[] = new float[Sprite.floatsPerVertex() * this.vertexCount()];
			buffer.get(arr);
			buffer.flip();
			return arr;
		}
	}
	
	@Override
	public void render(int bufferIndex){
		if(texture.isDirty()) texture.update();
		try{
			texture.bind();
		} catch(TextureNotGeneratedException e){
			return;
		}
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, bufferIndex, 4);
		texture.unbind();
	}

	@Override
	public int vertexCount() {
		return 4;
	}

	public void setRotation(float rotation) {
		synchronized(this){
			this.rotation = rotation;
			this.updateBuffer();
		}
	}
	
	public void setRotation(double rotation) {
		this.setRotation((float)rotation);
	}

	public void setRotationDegrees(float rotation) {
		this.setRotationDegrees((double)rotation);
	}
	
	public void setRotationDegrees(double rotation) {
		this.setRotation(Math.toRadians(rotation));
	}
	
	public void setRotationDirection(boolean clockwise){
		this.clockwiseRotation = clockwise;
	}
	
	public void setUV(float uvX, float uvY, float uvWidth, float uvHeight){
		synchronized(this){
			int textureWidth = texture.getWidth(), textureHeight = texture.getHeight();
			this.uvX = uvX;
			this.uvY = uvY;
			this.uvMaxX = uvWidth + uvX;
			if(this.uvMaxX > textureWidth) this.uvMaxX = textureWidth;
			this.uvMaxY = uvHeight + uvY;
			if(this.uvMaxY > textureHeight) this.uvMaxY = textureHeight;
			
			//This is a hack; if being called from the constructor, skip this update
			//because the constructor will update at end
			if(this.overrideColor != null) this.updateBuffer();
		}
	}

	@Override
	public void destroy() {
		if(this.texture != null) this.texture.destroy();
	}
	
	public void setOverrideColor(float red, float green, float blue, float alpha){
		synchronized(this){
			this.overrideColor.setXYZW(red, green, blue, alpha);
			this.updateBuffer();
		}
	}
	
	public void setOverrideColor(Vector4 overrideColor){
		synchronized(this){
			this.overrideColor = overrideColor;
			this.updateBuffer();
		}
	}
	
	public void setCameraIndependent(boolean cameraIndependent){
		synchronized(this){
			this.cameraIndependent = cameraIndependent;
			this.updateBuffer();
		}
	}
	
	public static int floatsPerVertex(){
		//return 8;
		return 9;
	}
	
	public static float[] packVertex(Vector2 coords, Vector2 uvCoords, Vector4 overrideColor, boolean cameraIndependent){
		float data[] = new float[Sprite.floatsPerVertex()];
		data[0] = coords.getX();
		data[1] = coords.getY();
		data[2] = uvCoords.getX();
		data[3] = uvCoords.getY();
		data[4] = overrideColor.getX();
		data[5] = overrideColor.getY();
		data[6] = overrideColor.getZ();
		data[7] = overrideColor.getW();
		data[8] = cameraIndependent ? 1f : 0f;
		return data;
	}
}
