package tech.kodiko.jgl2d.event;

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
	 * This is the window handle that the event occurred for.
	 */
	protected long window;
	
	/**
	 * Default constructor that sets the window handle the event
	 * was generated for.
	 * @param window The window handle the event was generated for.
	 */
	public Event(long window){
		this.window = window;
	}
	
	/**
	 * 
	 * @return The window handle that was associated with the event.
	 */
	public long getWindow(){
		return this.window;
	}
}
