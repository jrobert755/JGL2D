package tech.kodiko.jgl2d;

/**
 * A basic wrapper around an OpenGL or GLFW handle.
 * 
 * @author Jean-Luc Roberts
 *
 */
public abstract class GLObject {
	/**
	 * The handle created by calling an OpenGL/GLFW function. Can be
	 * anything from a texture to a buffer to a shader to a window.
	 */
	protected int handle;
	
	public GLObject(){
		this.handle = -1;
	}
	
	public int getHandle(){
		return this.handle;
	}
	
	/**
	 * This only sets the handle to -1, an invalid handle. Must be
	 * destroyed according to what this actually represents.
	 */
	public void destroy(){
		this.handle = -1;
	}
}
