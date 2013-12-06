package com.etk2000.gui;

import java.util.ArrayList;

public class ScrollArea {
	private ArrayList<ScrollAreaButton> buttons = new ArrayList<ScrollAreaButton>();
	private Button scrollDown, scrollUp;
	private int buttonsShown = 2, frameskip = 0, startButton = 0;// startButton is the first button
																	// shown
	private double x, y;
	private float width, height;

	public ScrollArea(double x, double y, float width, float height) {
		scrollUp = new Button(".", x + width - 10, y - height / 2 + 10, 30, 30, "GUIArrow_up");
		scrollDown = new Button(",", x + width - 10, y + height / 2 - 10, 30, 30, "GUIArrow_down");
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/** @returns the button's index **/
	public int addButton(String text) {
		buttons.add(new ScrollAreaButton(text, x + width / 2 - 20, width - 40));
		return buttons.size();
	}

	public void removeButton(int number) {
		buttons.remove(number);
	}

	public void render() {
		while (startButton > 0 && startButton + buttonsShown > buttons.size())
			startButton--;// fix bugs
		if (buttons.size() > buttonsShown) {
			/** blur not needed buttons **/
			if(startButton == 0)
				scrollUp.setSelectable(false);
			if(startButton + buttonsShown == buttons.size())
				scrollDown.setSelectable(false);

			/** render **/
			scrollDown.render();
			scrollUp.render();
			for(int i = 0; i < startButton; i++)
				buttons.get(i).setHeight(0);
			for (int i = 0; i < buttonsShown; i++) {// works!
				float buttonHeight = height / buttonsShown;
				buttons.get(i + startButton).setHeight(buttonHeight);
				buttons.get(i + startButton).setY(y - height + (i + 1.5) * buttonHeight);
				buttons.get(i + startButton).render();
			}
			for(int i = buttonsShown + startButton; i < buttons.size(); i++)
				buttons.get(i).setHeight(0);
		}
		else if (buttons.size() > 1) {
			for (int i = 0; i < buttons.size(); i++) {// works!
				float buttonHeight = height / buttons.size() + 1;
				buttons.get(i).setHeight(buttonHeight);
				buttons.get(i).setY(y - height + (i + 1.5) * buttonHeight);
				buttons.get(i).render();
			}
		}
		else if (buttons.size() == 1) {// (for ease)
			buttons.get(0).setHeight(height);
			buttons.get(0).setY(y);
			buttons.get(0).render();
		}
	}

	public int update() {
		if (frameskip-- > 0)// ---}
			return -1; // --------} fix a few bugs
		frameskip = 5; // --------}

		for (int i = 0; i < buttons.size(); i++) {
			if (buttons.get(i).clicked())
				return i;
		}

		if (scrollUp.clicked() && startButton > 0)
			startButton--;
		else if (scrollDown.clicked() && startButton + buttonsShown < buttons.size())
			startButton++;
		return -1;
	}

	public void setShown(int buttonsShown) {
		this.buttonsShown = buttonsShown;// TODO: make work! (causes old buttons to be out of screen)
	}

	public ScrollAreaButton getButton(int buttonNum) {
		return buttons.get(buttonNum);
	}
}