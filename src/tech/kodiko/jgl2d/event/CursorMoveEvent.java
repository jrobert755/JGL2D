package tech.kodiko.jgl2d.event;

public class CursorMoveEvent extends Event{
	private double xPosition, yPosition;
	
	public CursorMoveEvent(long window, double xPosition, double yPosition) {
		super(window);
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}

	public double getxPosition() {
		return xPosition;
	}

	public double getyPosition() {
		return yPosition;
	}
}
