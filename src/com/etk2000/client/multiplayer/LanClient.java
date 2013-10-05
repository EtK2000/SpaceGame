package com.etk2000.client.multiplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.etk2000.SpaceGame.GameBase;
import com.etk2000.packets.Packet;

public class LanClient {
	private Socket soc;
	private DataInputStream din;
	private DataOutputStream dout;

	public LanClient(String address, int port) {
		try {
			soc = new Socket(address, port);
			din = new DataInputStream(soc.getInputStream());
			dout = new DataOutputStream(soc.getOutputStream());

			System.out.println("ping: " + Math.abs((Long.parseLong(din.readUTF()) - System.currentTimeMillis())));
			dout.writeUTF("" + System.currentTimeMillis());
			while (true)
				readData();
		}
		catch (IOException e) {
		}
	}

	public void readData() {
		String packet;
		try {
			packet = din.readUTF();
			Packet p = Packet.buildPacket(packet);
			switch (p.getID()) {
				case 1:// Packet1MapData
					System.out.println(p.getClass());
					break;
				case 2:// Packet2EntityData
					System.out.println(p.getClass());
					break;
				case 3:// Packet3Ping
						// System.out.println("ping: " + Math.abs(Long.parseLong((String)
						// (p.getData().get(p.getData().size() - 1))) -
						// System.currentTimeMillis()));
					break;
				default:
					GameBase.logger.fine("IGNORING PACKET " + p.getID());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Packet ReceivePacket() throws Exception {
		String packet = din.readUTF();
		System.out.println("received!");
		return Packet.buildPacket(packet);
	}

	public void SendPacket(Packet p) throws Exception {
		dout.writeUTF(p.toString());
		System.out.println("sent!");
	}

	public void close() {
		try {
			din.close();
			dout.close();
			soc.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}