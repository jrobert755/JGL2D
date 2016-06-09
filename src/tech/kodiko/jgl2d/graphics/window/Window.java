package tech.kodiko.jgl2d.graphics.window;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import tech.kodiko.jgl2d.graphics.Renderer;

public class Window implements Runnable{
	private long handle;
	private int width, height;
	private long monitor;
	private boolean fullscreen;
	private String title;
	private GLCapabilities capabilities;
	private Renderer renderer;
	private boolean shown;
	private int numberOfSamples;
	
	public Window(String title, int width, int height, long monitor, boolean fullscreen, int numberOfSamples){
		// Configure our window
		this.width = width;
		this.height = height;
		this.monitor = monitor;
		this.fullscreen = fullscreen;
		this.title = title;
		
		this.capabilities = null;
		this.renderer = null;
		this.shown = false;
		this.numberOfSamples = numberOfSamples;
		this.init();
	}
	
	private void init(){
		if(this.handle > 0) return;
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_SAMPLES, this.numberOfSamples);
		
		if(this.fullscreen) this.handle = glfwCreateWindow(this.width, this.height, title, monitor, NULL);
		else this.handle = glfwCreateWindow(this.width, this.height, title, NULL, NULL);
		if ( this.handle == NULL )
			throw new RuntimeException("Failed to create the GLFW window");
		
		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(
				this.handle,
				(vidmode.width() - this.width) / 2,
				(vidmode.height() - this.height) / 2
			);
		this.showWindow();
	}
	
	public void setKeyCallback(GLFWKeyCallbackI callback){
		glfwSetKeyCallback(this.handle, callback);
	}
	
	public void makeCurrent(){
		glfwMakeContextCurrent(this.handle);
		glfwSwapInterval(1);
		if(this.capabilities == null) this.capabilities = GL.createCapabilities();
		else GL.setCapabilities(this.capabilities);
	}
	
	public void showWindow(){
		glfwShowWindow(this.handle);
		this.shown = true;
	}
	
	public void hideWindow(){
		glfwHideWindow(this.handle);
		this.shown = false;
	}
	
	public boolean shown(){
		return this.shown;
	}
	
	public void destroy(){
		// Free the window callbacks and destroy the window
		//this.renderer.destroy();
		glfwFreeCallbacks(this.handle);
		glfwDestroyWindow(this.handle);
		this.handle = -1;
		this.capabilities = null;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public long getHandle(){
		return this.handle;
	}
	
	public boolean shouldClose(){
		if(this.handle == -1) return true;
		return glfwWindowShouldClose(this.handle);
	}
	
	public void swapBuffers(){
		glfwSwapBuffers(this.handle);
	}
	
	public Renderer getRenderer(){
		return this.renderer;
	}
	
	public void setRenderer(Renderer renderer){
		this.renderer = renderer;
		
	}
	
	public void update(){
		this.makeCurrent();
		
		if(this.renderer != null){
			if(!this.renderer.isInitialized()) renderer.init(this.width, this.height);
			if(this.renderer != null) this.renderer.render();
		}
		
		this.swapBuffers();
	}

	@Override
	public void run() {
		this.makeCurrent();
		if(this.renderer != null)
			if(!this.renderer.isInitialized()) renderer.init(this.width, this.height);
		
		while(!this.shouldClose()){
			this.update();
			//Try to help prevent threads from getting locked out
			//if they modify the renderer's renderables
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(this.renderer != null)
			this.renderer.destroy();
	}
}
