package tech.kodiko.jgl2d.math;

public class Vector2 {
	protected float x, y;
	
	public Vector2(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Vector2 copy){
		this.x = copy.x;
		this.y = copy.y;
	}
	
	public Vector2(){
		this(0f, 0f);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public float[] getXY(){
		return new float[] { this.x, this.y };
	}
	
	public static Vector2 identity(){
		return new Vector2(1f, 1f);
	}
	
	public void add(Vector2 add){
		this.add(add.x, add.y);
	}
	
	public void add(float x, float y){
		this.x += x;
		this.y += y;
	}
	
	public Vector2 rotate(float delta){
		float cos = (float) Math.cos(delta);
		float sin = (float) Math.sin(delta);
		Matrix22 multMatrix = new Matrix22(new Vector2(cos, sin), new Vector2(-sin, cos));
		return multMatrix.multiply(this);
	}
	
	public Vector2 rotateClockwise(float delta){
		return this.rotate(-delta);
	}
	
	public void setXY(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void setXY(Vector2 vec){
		this.x = vec.getX();
		this.y = vec.getY();
	}
}
