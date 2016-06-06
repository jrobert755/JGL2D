package com.tangledworlds.jgl2d.event.handler;

import com.tangledworlds.jgl2d.event.Event;

/**
 * The interface for an event handler to implement. Has a single method to
 * be called when an event occurs.
 * 
 * @author Jean-Luc Roberts
 *
 */
public interface EventHandler {
	/**
	 * 
	 * @param event The event details
	 * @return Whether or not to stop handling this event
	 */
	public boolean handleEvent(Event event);
}
