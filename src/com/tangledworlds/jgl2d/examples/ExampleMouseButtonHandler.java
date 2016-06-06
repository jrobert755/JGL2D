package com.tangledworlds.jgl2d.examples;

import com.tangledworlds.jgl2d.event.MouseButtonEvent;
import com.tangledworlds.jgl2d.event.handler.MouseButtonEventHandler;

public class ExampleMouseButtonHandler implements MouseButtonEventHandler {
	@Override
	public boolean handleMouseButtonEvent(MouseButtonEvent event) {
		System.out.println("HERE IN MOUSE HANDLER");
		return false;
	}
}
