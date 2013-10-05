package com.etk2000.Levels;

import org.lwjgl.opengl.Display;

import com.etk2000.GameHandling.GameType;
import com.etk2000.Levels.menu.SelectServer;
import com.etk2000.SpaceGame.GameBase;
import com.etk2000.client.multiplayer.LanClient;
import com.etk2000.packets.Packet;

public class MPLanGame {
	public void start() {
		LanClient lc = new LanClient(SelectServer.serverIP, 25566);
		GameType.curType = GameType.Server;
		GameBase.isMidLevel = true;

		try {
		}
		catch (Exception e) {
			if (e instanceof java.net.SocketException) {
				System.out.println("server closed!");
				lc.close();
				return;
			}
			else
				e.printStackTrace();
		}

		while (!Display.isCloseRequested()) {
			Packet p = null;
			try {
				p = lc.ReceivePacket();
				if (p != null) {
					switch (p.getID()) {
						case 1:// draw map
							break;
						case 2:// draw entities
							break;
						case 3:// ping
							break;
					}
					System.out.println("id" + p.getID());
				}
			}
			catch (Exception e) {
				if (e instanceof java.net.SocketException) {
					lc.close();
				}
			}
			Display.update();
			Display.sync(60);
		}
	}
}