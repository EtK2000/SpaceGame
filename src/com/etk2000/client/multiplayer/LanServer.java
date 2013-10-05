package com.etk2000.client.multiplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import com.etk2000.packets.Packet;
import com.etk2000.packets.Packet3Ping;

public class LanServer extends Thread {
	private Socket ClientSoc;
	private DataInputStream din;
	private DataOutputStream dout;
	private String address;
	
	boolean stopSignal = false;

	LanServer(Socket soc) {
		try {
			ClientSoc = soc;
			din = new DataInputStream(ClientSoc.getInputStream());
			dout = new DataOutputStream(ClientSoc.getOutputStream());
			address = ClientSoc.getInetAddress().toString();
			
			System.out.print(address + " connected...");
			dout.writeUTF("" + System.currentTimeMillis());// send ping start time
			System.out.println("ping: " + Math.abs((Long.parseLong(din.readUTF()) - System.currentTimeMillis())));
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (!stopSignal) {
						try {
							SendPacket(new Packet3Ping());
							// ReceivePacket();
						}
						catch (Exception e) {
							if (e instanceof java.net.SocketException) {
								System.out.println(address + " disconnected...");
								close();
							}
							else
								e.printStackTrace();
						}
					}

				}
			}).start();
		}
		catch (Exception ex) {
		}
	}

	Packet ReceivePacket() throws Exception {
		String packet = new String(), temp = new String();
		do {
			temp = din.readUTF();
			packet += temp;
		} while (temp != null);
		dout.writeUTF("Packet Received Successfully");
		System.out.println("received!");
		return Packet.buildPacket(packet);
	}

	void SendPacket(Packet p) throws Exception {
		dout.writeUTF(p.toString());
	}

	public void close() {
		try {
			din.close();
			dout.close();
			stopSignal = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}