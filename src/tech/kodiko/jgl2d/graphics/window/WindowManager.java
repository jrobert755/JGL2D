package tech.kodiko.jgl2d.graphics.window;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

import java.util.HashMap;
import java.util.Map.Entry;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import tech.kodiko.jgl2d.event.GLFWEventHandler;
import tech.kodiko.jgl2d.exception.WindowCreationException;

public class WindowManager{
	private static HashMap<Long, Window> createdWindows = null;
	private static HashMap<Long, Thread> windowThreads = null;
	private static boolean initialized = false;
	private static Thread initThread;
	
	public static Window getWindow(long handle){
		return WindowManager.createdWindows.get(new Long(handle));
	}
	
	public static void updateWindows(){
		for(Entry<Long, Window> entry : WindowManager.createdWindows.entrySet()){
			Window window = entry.getValue();
			window.updateCursorState();
		}
	}
	
	public static int getWindowCount(){
		return WindowManager.createdWindows.size();
	}
	
	public static void closeWindows(){
		for(Entry<Long, Window> entry : WindowManager.createdWindows.entrySet()){
			Window window = entry.getValue();
			if(window.shouldClose()){
				WindowManager.removeWindow(window);
			}
		}
	}
	
	private static void addWindow(Window window, Thread windowThread){
		WindowManager.createdWindows.put(window.getHandle(), window);
		WindowManager.windowThreads.put(window.getHandle(), windowThread);
	}
	private static void removeWindow(Window window){
		long handle = window.getHandle();
		Thread thread = WindowManager.windowThreads.get(handle);
		window.hideWindow();
		GLFW.glfwSetWindowShouldClose(window.getHandle(), true);
		//window.destroy();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		window.destroy();
		
		WindowManager.createdWindows.remove(handle);
		WindowManager.windowThreads.remove(handle);
	}
	public static void initialize(){
		if(WindowManager.initialized) return;
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();
		
		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");
		
		WindowManager.initialized = true;
		WindowManager.initThread = Thread.currentThread();
		
		WindowManager.createdWindows = new HashMap<Long, Window>();
		WindowManager.windowThreads = new HashMap<Long, Thread>();
	}
	public static void terminate(){
		if(!WindowManager.isInitialized()) return;
		for(Entry<Long, Window> entry : WindowManager.createdWindows.entrySet()){
			Window window = entry.getValue();
			WindowManager.removeWindow(window);
		}
		
		glfwTerminate();
		glfwSetErrorCallback(null).free();
		WindowManager.initialized = false;
		WindowManager.createdWindows = null;
	}
	public static boolean isInitialized(){
		return WindowManager.initialized;
	}
	public static Window createWindow(String title, int width, int height, long monitor, boolean fullscreen, int numberOfSamples){
		if(!WindowManager.isInitialized()) throw new WindowCreationException("Window Manager is not initialized!");
		else if(!WindowManager.initThread.equals(Thread.currentThread())) throw new WindowCreationException("New windows can only be created in the thread that initialized the Window Managter!");
		Window window = new Window(title, width, height, monitor, fullscreen, numberOfSamples);
		GLFWEventHandler.installHandlers(window);
		Thread windowThread = new Thread(window);
		windowThread.start();
		WindowManager.addWindow(window, windowThread);
		return window;
	}
	public static Window createWindow(String title, int width, int height, long monitor, boolean fullscreen){
		return WindowManager.createWindow(title, width, height, monitor, fullscreen, 0);
	}
	public static Window createWindow(String title, int width, int height){
		return WindowManager.createWindow(title, width, height, glfwGetPrimaryMonitor(), false, 0);
	}
	public static Window createWindow(String title){
		return WindowManager.createWindow(title, 800, 600);
	}
	public static Window createWindow(){
		return WindowManager.createWindow("LWGJL Window");
	}
}
