package com.tangledworlds.jgl2d.event;

public class MouseButtonEvent extends Event {
	private int button, action, mods;

	public MouseButtonEvent(long window, int button, int action, int mods) {
		super(window);
		this.button = button;
		this.action = action;
		this.mods = mods;
	}

	public int getButton() {
		return button;
	}

	public int getAction() {
		return action;
	}

	public int getMods() {
		return mods;
	}
}
