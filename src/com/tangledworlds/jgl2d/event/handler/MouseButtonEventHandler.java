package com.tangledworlds.jgl2d.event.handler;

import com.tangledworlds.jgl2d.event.Event;
import com.tangledworlds.jgl2d.event.MouseButtonEvent;

public interface MouseButtonEventHandler extends EventHandler{
	public default boolean handleEvent(Event event){
		if(!(event instanceof MouseButtonEvent)) return false;
		return this.handleMouseButtonEvent((MouseButtonEvent) event);
	}
	public boolean handleMouseButtonEvent(MouseButtonEvent event);
}
