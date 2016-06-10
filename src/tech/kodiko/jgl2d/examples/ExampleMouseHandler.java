package tech.kodiko.jgl2d.examples;

import org.lwjgl.glfw.GLFW;

import tech.kodiko.jgl2d.event.CursorMoveEvent;
import tech.kodiko.jgl2d.event.Event;
import tech.kodiko.jgl2d.event.MouseButtonEvent;
import tech.kodiko.jgl2d.event.handler.CursorMoveEventHandler;
import tech.kodiko.jgl2d.event.handler.MouseButtonEventHandler;
import tech.kodiko.jgl2d.graphics.Batch2DRenderer;
import tech.kodiko.jgl2d.math.Vector2;

public class ExampleMouseHandler implements MouseButtonEventHandler, CursorMoveEventHandler{
	private boolean mousePressed = false;
	private double xPosition = 0, yPosition = 0;
	private Vector2 camera;
	private Batch2DRenderer renderer;
	private int mouseButton;
	
	public ExampleMouseHandler(Batch2DRenderer renderer, int mouseButton){
		this.renderer = renderer;
		this.camera = new Vector2();
		this.renderer.setCamera(camera);
		this.mouseButton = mouseButton;
	}
	
	@Override
	public boolean handleEvent(Event event) {
		if(event instanceof CursorMoveEvent) return this.handleCursorMoveEvent((CursorMoveEvent) event);
		else if(event instanceof MouseButtonEvent) return this.handleMouseButtonEvent((MouseButtonEvent) event);
		else return false;
	}

	@Override
	public boolean handleCursorMoveEvent(CursorMoveEvent event) {
		double newX = event.getxPosition(), newY = event.getyPosition();
		if(this.mousePressed){
			double deltaX = newX - this.xPosition;
			double deltaY = newY - this.yPosition;
			this.camera.add((float)deltaX, (float)deltaY);
			this.renderer.setCamera(this.camera);
		}
		this.xPosition = newX;
		this.yPosition = newY;
		return false;
	}

	@Override
	public boolean handleMouseButtonEvent(MouseButtonEvent event) {
		if(event.getButton() == this.mouseButton){
			if(event.getAction() == GLFW.GLFW_PRESS){
				this.mousePressed = true;
				event.getWindow().setCursorState(GLFW.GLFW_CURSOR_DISABLED);
			}
			else if(event.getAction() == GLFW.GLFW_RELEASE){
				this.mousePressed = false;
				event.getWindow().setCursorState(GLFW.GLFW_CURSOR_NORMAL);
			}
		}
		return false;
	}
	
}
