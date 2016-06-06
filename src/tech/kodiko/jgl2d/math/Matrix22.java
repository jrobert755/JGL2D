package tech.kodiko.jgl2d.math;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Matrix22 {
	private Vector2 columns[];
	
	public Matrix22(){
		this.columns = new Vector2[2];
		this.columns[0] = new Vector2();
		this.columns[1] = new Vector2();
	}
	
	public Matrix22(Vector2 one, Vector2 two){
		this.columns = new Vector2[2];
		this.columns[0] = new Vector2(one);
		this.columns[1] = new Vector2(two);
	}
	
	public float[] getData(){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(4*4);
		for(int i = 0; i < 4; i++)
			buffer.put(this.columns[i].getXY());
		buffer.flip();
		
		float arr[] = new float[16];
		buffer.get(arr);
		buffer.flip();
		return arr;
	}
	
	public Vector2 multiply(Vector2 vec){
		return this.multiply(vec.getX(), 0, vec.getY(), 0);
	}
	
	public Vector2 multiply(Vector2 vec, Vector2 origin){
		return this.multiply(vec.getX(), origin.getX(), vec.getY(), origin.getY());
	}
	
	public Vector2 multiply(float x, float xOrigin, float y, float yOrigin){
		float newX = x - xOrigin;
		float newY = y - yOrigin;
		float one = this.columns[0].getX() * newX + this.columns[1].getX() * newY;
		float two = this.columns[0].getY() * newX + this.columns[1].getY() * newY;
		return new Vector2(one + xOrigin, two + yOrigin);
	}
	
	public static Vector2 rotate(Vector2 vec, float delta){
		return Matrix22.rotate(vec.getX(), 0, vec.getY(), 0, delta);
	}
	
	public static Vector2 rotate(Vector2 vec, Vector2 origin, float delta){
		return Matrix22.rotate(vec.getX(), origin.getX(), vec.getY(), origin.getY(), delta);
	}
	
	public static Vector2 rotate(float x, float xOrigin, float y, float yOrigin, float delta){
		float cos = (float) Math.cos(delta);
		float sin = (float) Math.sin(delta);
		Matrix22 multMatrix = new Matrix22(new Vector2(cos, sin), new Vector2(-sin, cos));
		return multMatrix.multiply(x, xOrigin, y, yOrigin);
	}
}
