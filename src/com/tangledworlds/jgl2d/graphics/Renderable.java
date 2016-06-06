package com.tangledworlds.jgl2d.graphics;

public abstract class Renderable {
	private int z;
	public abstract float[] getData();
	public abstract void render(int offset);
	public abstract int vertexCount();
	public abstract void destroy();
	public int getZ() {
		return this.z;
	}
	public void setZ(int z) {
		this.z = z;
	}
}
