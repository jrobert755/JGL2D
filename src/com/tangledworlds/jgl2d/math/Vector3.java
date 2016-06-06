package com.tangledworlds.jgl2d.math;

public class Vector3 extends Vector2 {
	protected float z;
	
	public Vector3(float x, float y, float z) {
		super(x, y);
		this.z = z;
	}
	
	public Vector3(Vector2 copy, float z){
		super(copy);
		this.z = z;
	}
	
	public Vector3(Vector3 copy){
		this(copy, copy.z);
	}
	
	public Vector3(){
		this(0f, 0f, 0f);
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
	public float[] getXYZ(){
		return new float[] { this.x, this.y, this.z };
	}
	
	public static Vector3 identity(){
		return new Vector3(1, 1, 1);
	}
	
	public void add(Vector3 add){
		this.add(add, add.z);
	}
	
	public void add(Vector2 add, float z){
		super.add(add);
		this.z += z;
	}
	
	public void add(float x, float y, float z){
		super.add(x, y);
		this.z += z;
	}
	
	public void setXYZ(float x, float y, float z){
		super.setXY(x, y);
		this.z = z;
	}
	
	public void setXYZ(Vector3 vec){
		super.setXY(vec);
		this.z = vec.getZ();
	}
}
