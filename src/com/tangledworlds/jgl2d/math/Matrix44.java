package com.tangledworlds.jgl2d.math;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Matrix44 {
	private Vector4 columns[];
	
	public Matrix44(){
		this.columns = new Vector4[4];
		for(int i = 0; i < 4; i++)
			this.columns[i] = new Vector4();
	}
	
	public Matrix44(Vector4 one, Vector4 two, Vector4 three, Vector4 four){
		this.columns = new Vector4[4];
		this.columns[0] = new Vector4(one);
		this.columns[1] = new Vector4(two);
		this.columns[2] = new Vector4(three);
		this.columns[3] = new Vector4(four);
	}
	
	public float[] getData(){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(4*4);
		for(int i = 0; i < 4; i++)
			buffer.put(this.columns[i].getXYZW());
		buffer.flip();
		
		float arr[] = new float[16];
		buffer.get(arr);
		buffer.flip();
		return arr;
	}
	
	public static Matrix44 getOrthogonal(float left, float right, float top, float bottom){
		float rml = right - left;
		float rpl = right + left;
		float tmb = top - bottom;
		float tpb = top + bottom;
		return new Matrix44(
				new Vector4(2f/rml, 0f, 0f, 0f),
				new Vector4(0f, 2f/tmb, 0f, 0f),
				new Vector4(0f, 0f, 0f, 0f),
				new Vector4(-(rpl/rml), -(tpb/tmb), 0f, 1f)
			);
	}
	
	public static Matrix44 getOrthogonal(float width, float height){
		return Matrix44.getOrthogonal(0f, width, 0f, height);
	}
}
