package com.etk2000.packets;

import java.util.Random;

public class Packet3Ping extends Packet {

	public Packet3Ping() {
		super(3);
		byte[] ping = new byte[256];// ping length should ALWAYS be 256!
		new Random().nextBytes(ping);
		for (int i = 0; i < ping.length; i++)
			data.add("" + ping[i]);
	}

	@Override
	public void send() {
		data.add("" + (long)System.currentTimeMillis());// start time
		super.send();
	}
}