package tech.kodiko.jgl2d.examples;

import tech.kodiko.jgl2d.event.KeyEvent;
import tech.kodiko.jgl2d.event.handler.KeyEventHandler;

public class ExampleKeyHandler implements KeyEventHandler {
	@Override
	public boolean handleKeyEvent(KeyEvent event) {
		System.out.println("HERE IN HANDLER");
		return false;
	}
}
