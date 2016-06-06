package tech.kodiko.jgl2d.examples;

import tech.kodiko.jgl2d.event.MouseButtonEvent;
import tech.kodiko.jgl2d.event.handler.MouseButtonEventHandler;

public class ExampleMouseButtonHandler implements MouseButtonEventHandler {
	@Override
	public boolean handleMouseButtonEvent(MouseButtonEvent event) {
		System.out.println("HERE IN MOUSE HANDLER");
		return false;
	}
}
