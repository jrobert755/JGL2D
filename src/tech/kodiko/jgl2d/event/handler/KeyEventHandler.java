package tech.kodiko.jgl2d.event.handler;

import tech.kodiko.jgl2d.event.Event;
import tech.kodiko.jgl2d.event.KeyEvent;

public interface KeyEventHandler extends EventHandler{
	public default boolean handleEvent(Event event){
		if(!(event instanceof KeyEvent)) return false;
		return this.handleKeyEvent((KeyEvent) event);
	}
	public boolean handleKeyEvent(KeyEvent event);
}
