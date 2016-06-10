package tech.kodiko.jgl2d.event;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

import tech.kodiko.jgl2d.event.handler.EventHandler;
import tech.kodiko.jgl2d.graphics.window.Window;

/**
 * A default event handler for GLFW events.
 * 
 * @author Jean-Luc Roberts
 *
 */
public class GLFWEventHandler implements Runnable{
	/**
	 * The event queue that all events are stored on if event handling is to happen in
	 * its own thread.
	 */
	private static ConcurrentLinkedQueue<Event> eventQueue = new ConcurrentLinkedQueue<Event>();
	/**
	 * Whether or not event handling is happening it its own thread.
	 */
	private static boolean multithreaded = false;
	
	/**
	 * Adds an event to the event queue.
	 * @param event Event to add
	 */
	private static void addToQueue(Event event){
		GLFWEventHandler.eventQueue.add(event);
	}
	
	public static void newEvent(Event event){
		//if(GLFWEventHandler.multithreaded) eventQueue.add(event);
		if(GLFWEventHandler.multithreaded) GLFWEventHandler.addToQueue(event);
		else GLFWEventHandler.handleEvent(event);
	}
	
	/**
	 * The Keyboard callback class for GLFW Key Events.
	 * 
	 * @author Jean-Luc Roberts
	 *
	 */
	private static class GLFWKey implements GLFWKeyCallbackI{
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			Event event = new KeyEvent(window, key, scancode, action,mods);
			GLFWEventHandler.newEvent(event);
		}
	}
	/**
	 * The Keyboard handler to be installed to every window.
	 */
	private static GLFWKey glfwKeyHandler = new GLFWEventHandler.GLFWKey();
	
	/**
	 * The Mouse Button callback class for GLFW Mouse Button Events.
	 * 
	 * @author Jean-Luc Roberts
	 *
	 */
	private static class GLFWMouseButton implements GLFWMouseButtonCallbackI{
		@Override
		public void invoke(long window, int button, int action, int mods) {
			Event event = new MouseButtonEvent(window, button, action, mods);
			GLFWEventHandler.newEvent(event);
		}
	}
	/**
	 * The Mouse Button handler to be installed to every window.
	 */
	private static GLFWMouseButton glfwMouseButtonHandler = new GLFWEventHandler.GLFWMouseButton();
	
	private static class GLFWCursorPosition implements GLFWCursorPosCallbackI{
		@Override
		public void invoke(long window, double xpos, double ypos) {
			Event event = new CursorMoveEvent(window, xpos, ypos);
			GLFWEventHandler.newEvent(event);
		}
	}
	/**
	 * The Mouse Button handler to be installed to every window.
	 */
	private static GLFWCursorPosition glfwCursorPositionHandler = new GLFWEventHandler.GLFWCursorPosition();

	
	/**
	 * The list of event handlers to be called when an event is received.
	 */
	private static LinkedList<EventHandler> eventHandlers = new LinkedList<EventHandler>();
	/**
	 * Add an event handler to the list of handlers.
	 * 
	 * @param handler The handler to add
	 */
	public static void addEventHandler(EventHandler handler){
		GLFWEventHandler.eventHandlers.add(handler);
	}
	/**
	 * Remove an event handler from the list of handlers.
	 * 
	 * @param handler The handler to remove
	 */
	public static void removeEventHandler(EventHandler handler){
		GLFWEventHandler.eventHandlers.remove(handler);
	}
	/**
	 * Calls all of the event handlers for the event passed in. If an event handler
	 * returns true, it means that they have fully processed the event, and no other
	 * handler should be called.
	 * 
	 * @param event The event to be processed
	 */
	private static void handleEvent(Event event){
		for(EventHandler handler : GLFWEventHandler.eventHandlers){
			if(handler.handleEvent(event)) return;
		}
	}
	
	public static void installHandlers(Window window){
		long handle = window.getHandle();
		if(handle == -1) return;
		GLFW.glfwSetKeyCallback(handle, GLFWEventHandler.glfwKeyHandler);
		GLFW.glfwSetMouseButtonCallback(handle, GLFWEventHandler.glfwMouseButtonHandler);
		GLFW.glfwSetCursorPosCallback(handle, GLFWEventHandler.glfwCursorPositionHandler);
	}
	
	private boolean running = false;
	@Override
	public void run() {
		GLFWEventHandler.multithreaded = true;
		this.running = true;
		while(this.running){
			Event event = GLFWEventHandler.eventQueue.poll();
			if(event != null) GLFWEventHandler.handleEvent(event);
		}
	}
	
	public void end(){
		this.running = false;
	}
}
