package com.etk2000.packets;

import java.util.ArrayList;

import com.etk2000.Entity.Entity2D;
import com.etk2000.Levels.Level;

public class Packet2EntityData extends Packet {
	ArrayList<Entity2D> entities;

	public Packet2EntityData(Level lvl) {
		super(2);
		for (int i = 0; i < lvl.numberOfEntities(); i++)
			data.add(lvl.getEntity(i).toString());
	}

	@Override
	public void send() {
		if (contains(packets, getClass())) {
			super.send();
		}
	}
}