package tech.kodiko.jgl2d.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import tech.kodiko.jgl2d.GLObject;
import tech.kodiko.jgl2d.exception.TextureNotGeneratedException;

public class Texture extends GLObject{
	private int data[];
	private int width, height;
	//private int textureHandle;
	private boolean dirty, generated;
	
	public Texture(int data[], int width, int height){
		super();
		this.data = data;
		this.width = width;
		this.height = height;
		//this.textureHandle = -1;
		this.dirty = true;
		this.generated = false;
	}
	
	public Texture(int textureHandle, int width, int height){
		this.data = null;
		this.handle = textureHandle;
		this.width = width;
		this.height = height;
		if(textureHandle == -1){
			this.dirty = true;
			this.generated = false;
		} else{
			this.dirty = false;
			this.generated = true;
		}
	}

	public int[] getData() {
		return data;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public void generateTexture(){
		//this.textureHandle = GL11.glGenTextures();
		this.handle = GL11.glGenTextures();
		this.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		this.unbind();
		this.generated = true;
	}
	
	@Override
	public void destroy(){
		//GL11.glDeleteTextures(this.textureHandle);
		//this.textureHandle = -1;
		if(this.handle == -1) return;
		GL11.glDeleteTextures(this.handle);
		super.destroy();
	}
	
	public void bind() throws TextureNotGeneratedException{
		//if(this.textureHandle == -1) throw new TextureNotGeneratedException("Texture not generated!");
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textureHandle);
		if(this.handle == -1) throw new TextureNotGeneratedException("Texture not generated!");
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.handle);
	}
	
	public void unbind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public void setData(int data[]){
		this.data = data;
		//this.update();
		this.dirty = true;
	}
	
	public void update(){
		if(!generated) this.generateTexture();
		this.bind();
		
		if(this.data == null) GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, this.width, this.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0);
		else GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL13.GL_COMPRESSED_RGBA, this.width, this.height, 0, GL11.GL_RGBA, GL12.GL_UNSIGNED_INT_8_8_8_8, this.data);
		
		this.unbind();
		this.dirty = false;
	}
	
	public boolean isDirty(){
		return this.dirty;
	}
	
	public float normalizeX(float x){
		return x / this.width;
	}
	
	public float normalizeY(float y){
		return y / this.height;
	}
	
	public float normalizeYFlipped(float y){
		return 1f - this.normalizeY(y);
	}
}
