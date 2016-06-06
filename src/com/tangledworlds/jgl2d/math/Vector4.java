package com.tangledworlds.jgl2d.math;

public class Vector4 extends Vector3 {
	private float w;
	
	public Vector4(float x, float y, float z, float w) {
		super(x, y, z);
		this.w = w;
	}
	
	public Vector4(double x, double y, double z, double w) {
		this((float)x, (float)y, (float)z, (float)w);
	}
	
	public Vector4(Vector2 copy, float z, float w){
		super(copy, z);
		this.w = w;
	}
	
	public Vector4(Vector3 copy, float w){
		super(copy);
		this.w = w;
	}
	
	public Vector4(Vector4 copy){
		this(copy, copy.w);
	}
	
	public Vector4(){
		this(0f, 0f, 0f, 0f);
	}

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}
	
	public float[] getXYZW(){
		return new float[] { this.x, this.y, this.z, this.w };
	}
	
	public static Vector4 identity(){
		return new Vector4(1, 1, 1, 1);
	}
	
	public void add(Vector4 add){
		this.add(add, add.w);
	}
	
	public void add(Vector3 add, float w){
		super.add(add);
		this.w += w;
	}
	
	public void add(Vector2 add, float z, float w){
		super.add(add, z);
		this.w += w;
	}
	
	public void add(float x, float y, float z, float w){
		super.add(x, y, w);
		this.z += z;
	}
	
	public void setXYZW(float x, float y, float z, float w){
		super.setXYZ(x, y, z);
		this.w = w;
	}
	
	public void setXYZW(Vector4 vec){
		super.setXYZ(vec);
		this.w = vec.getW();
	}
}
