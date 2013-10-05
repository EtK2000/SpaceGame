package com.etk2000.gui;

public class ScrollAreaButton extends Button {
	public ScrollAreaButton(String text, double x, float width) {
		super(text, x, 0, width, 0);
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void setY(double y) {
		this.y = y;
	}
}