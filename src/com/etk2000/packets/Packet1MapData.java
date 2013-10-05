package com.etk2000.packets;

import com.etk2000.Levels.Level;

public class Packet1MapData extends Packet {
	/**
	 * data:
	 * 0: width
	 * 1: height
	 **/

	public Packet1MapData(Level lvl) {
		super(1);
		data.add("" + lvl.getWidth());
		data.add("" + lvl.getHeight());
	}

	@Override
	public void send() {
		if (contains(packets, getClass())) {
			super.send();
		}
	}
}