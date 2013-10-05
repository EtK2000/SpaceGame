package com.etk2000.packets;

import java.util.ArrayList;

import com.etk2000.Levels.menu.SelectServer;
import com.etk2000.client.multiplayer.LanGame;

public class Packet {
	protected final int id;
	protected ArrayList<String> data = new ArrayList<>();

	public Packet(int id) {
		this.id = id;
	}

	public void send() {
		if (data.size() == 0)
			return;// no data to send
		if (contains(packets, getClass())) {// only send it if it's valid!
			LanGame.loginToServer(SelectServer.serverIP);
		}
	}

	protected boolean contains(Class<?>[] arr, Class<?> single) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(single))
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		String pData = new String();
		pData += id + ":";
		for (int i = 0; i < data.size(); i++) {
			pData += data.get(i).toString();
			if (i + 1 < data.size())
				pData += ':';
		}
		return pData;
	}

	public static Packet buildPacket(String pData) {
		int id = Integer.parseInt(pData.substring(0, pData.indexOf(':')));
		pData = pData.substring(pData.indexOf(':') + 1);
		Packet p = new Packet(id);
		while (pData.length() > 0) {
			if (pData.contains(":")) {
				p.data.add(pData.substring(0, pData.indexOf(':')));
				pData = pData.substring(pData.indexOf(':') + 1);
			}
			else {
				p.data.add(pData);
				break;
			}
		}
		return p;
	}

	public ArrayList<String> getData() {
		return data;
	}

	public int getID() {
		return id;
	}

	public final static Class<?>[] packets = { Packet1MapData.class, Packet2EntityData.class, Packet3Ping.class };
}