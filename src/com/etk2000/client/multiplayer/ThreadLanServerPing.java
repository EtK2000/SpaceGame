package com.etk2000.client.multiplayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.etk2000.SpaceGame.GameBase;

//TODO: make client only
public class ThreadLanServerPing extends Thread {
	private final String description;

	/** The socket we're using to send packets on. */
	private final DatagramSocket socket;
	private boolean isStopping = true;
	private final String address;

	public ThreadLanServerPing(String description, String address) throws IOException {
		super("LanServerPinger");
		this.description = description;
		this.address = address;
		this.setDaemon(true);
		this.socket = new DatagramSocket();
	}

	public void run() {
		String s = getPingResponse(description, address);
		byte[] abyte = s.getBytes();

		while (!this.isInterrupted() && this.isStopping) {
			try {
				InetAddress inetaddress = InetAddress.getByName("224.0.2.60");
				DatagramPacket datagrampacket = new DatagramPacket(abyte, abyte.length, inetaddress, 4445);
				this.socket.send(datagrampacket);
			}
			catch (IOException e) {
				GameBase.logger.warning("LanServerPinger: " + e.getMessage());
				break;
			}

			try {
				sleep(1500L);
			}
			catch (InterruptedException interruptedexception) {
				;
			}
		}
	}

	public void interrupt() {
		super.interrupt();
		this.isStopping = false;
	}

	public static String getPingResponse(String description, String par1Str) {
		return "[DESC]" + description + "[/DESC][AD]" + par1Str + "[/AD]";
	}

	public static String getDescriptionFromPingResponse(String par0Str) {
		int i = par0Str.indexOf("[DESC]");

		if (i < 0) {
			return "no description!";
		}
		else {
			int j = par0Str.indexOf("[/DESC]", i + "[DESC]".length());
			return j < i ? "infinate description!" : par0Str.substring(i + "[DESC]".length(), j);
		}
	}

	public static String getAdFromPingResponse(String response) {
		int i = response.indexOf("[/DESC]");

		if (i < 0) {
			return null;
		}
		else {
			int j = response.indexOf("[/DESC]", i + "[/DESC]".length());

			if (j >= 0) {
				return null;
			}
			else {
				int k = response.indexOf("[AD]", i + "[/DESC]".length());

				if (k < 0) {
					return null;
				}
				else {
					int l = response.indexOf("[/AD]", k + "[AD]".length());
					return l < k ? null : response.substring(k + "[AD]".length(), l);
				}
			}
		}
	}
}