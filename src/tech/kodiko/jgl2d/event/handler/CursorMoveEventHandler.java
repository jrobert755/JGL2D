package tech.kodiko.jgl2d.event.handler;

import tech.kodiko.jgl2d.event.CursorMoveEvent;
import tech.kodiko.jgl2d.event.Event;

public interface CursorMoveEventHandler extends EventHandler {
	public default boolean handleEvent(Event event){
		if(!(event instanceof CursorMoveEvent)) return false;
		return this.handleCursorMoveEvent((CursorMoveEvent) event);
	}
	public boolean handleCursorMoveEvent(CursorMoveEvent event);
}
