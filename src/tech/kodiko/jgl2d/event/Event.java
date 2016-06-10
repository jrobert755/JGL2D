package tech.kodiko.jgl2d.event;

import tech.kodiko.jgl2d.graphics.window.Window;
import tech.kodiko.jgl2d.graphics.window.WindowManager;

/**
 * A base class for all events.
 * <p>
 * Only handles creating an event that references a {@link tech.kodiko.jgl2d.graphics.window.Window} handle.
 * 
 * @author Jean-Luc Roberts
 *
 */
public abstract class Event {
	/**
	 * This is the window that the event occurred for.
	 */
	protected Window window;
	
	/**
	 * Default constructor that sets the window handle the event
	 * was generated for.
	 * @param windowHandle The window handle the event was generated for.
	 */
	public Event(long windowHandle){
		//this.window = window;
		this.window = WindowManager.getWindow(windowHandle);
	}
	
	/**
	 * 
	 * @return The window that was associated with the event.
	 */
	public Window getWindow(){
		return this.window;
	}
}
