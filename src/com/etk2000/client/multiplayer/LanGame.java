package com.etk2000.client.multiplayer;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import com.etk2000.SideThreads.SideThread;
import com.etk2000.SpaceGame.GameBase;

public class LanGame extends SideThread {
	static ServerSocket server; // Server side's server
	static Socket socket; // Client side socket
	static String serverIP; // Server socket will bind to this address
	static int port = 25566; // Client and server will connect/host on this port

	ArrayList<LanServer> clients = new ArrayList<>();

	public void Init() {
		run = new Runnable() {
			@Override
			public void run() {
				try {
					serverIP = getIP();
					GameBase.logger.info("opening game to lan, IP: " + serverIP);
					server = new ServerSocket(port);
				}
				catch (IOException e) {
					GameBase.logger.warning("could not bind port " + port + " on " + serverIP);
					cleanup();
					return;
				}
				while (!stopSignal) {
					try {
						clients.add(new LanServer(server.accept()));
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				for (int i = 0; clients.size() > 0;) {// kick all clients off
					clients.get(i).close();
					clients.remove(i);
				}
				cleanup();
			}
		};
		super.Init(this);
	}

	/**
	 * @param: IP and Port
	 * @return: Is Server online
	 **/
	public static boolean loginToServer(String IP) {
		int splitPoint = IP.indexOf(':');
		String address;
		if (splitPoint > 0) {
			address = IP.substring(0, IP.indexOf(':') - 1);
			port = Integer.parseInt(IP.substring(IP.indexOf(':')));// should only have only one ':'
		}
		else
			address = IP;

		if (address.equals("127.0.0.1") || address.equalsIgnoreCase("localhost"))
			try {
				address = getIP();
			}
			catch (SocketException e) {
				e.printStackTrace();
			}
		try {
			socket = new Socket(address, port);
			GameBase.logger.info("connected to server, setting up streams...");
			socket.getOutputStream().write(new String("abc").getBytes());
			System.out.println(7);
			// in = new ObjectInputStream(socket.getInputStream());
			// out = new ObjectOutputStream(socket.getOutputStream());

			return true;
		}
		catch (IOException e) {
			GameBase.logger.warning(address + ':' + port + " -> " + e.getMessage());
			return false;
		}

	}

	/**
	 * @param: IP and Port
	 * @return: Is Server online
	 **/
	public static boolean pingServer(String IP) {
		int splitPoint = IP.indexOf(':');
		String address;
		if (splitPoint > 0) {
			address = IP.substring(0, IP.indexOf(':') - 1);
			port = Integer.parseInt(IP.substring(IP.indexOf(':')));// should only have only one ':'
		}
		else
			address = IP;
		if (address.equals("127.0.0.1") || address.equalsIgnoreCase("localhost"))
			try {
				address = getIP();
			}
			catch (SocketException e) {
				e.printStackTrace();
			}
		try {
			socket = new Socket(address, port);
			if (socket != null) {
				socket.close();
				return true;
			}
		}
		catch (IOException e) {
			if (e instanceof java.net.ConnectException)
				GameBase.logger.warning("port " + port + " isn't open on " + address);
			else
				e.printStackTrace();
		}
		return false;
	}

	public static void cleanup() {
		serverIP = null;
		try {
			server.close();
		}
		catch (Exception e) {// not the server, most likely a NullPointerException
		}
		try {
			socket.close();
		}
		catch (Exception e) {// not the client, most likely a NullPointerException
		}
	}

	public static String getIP() throws SocketException {
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface current = interfaces.nextElement();
			if (!current.isUp() || current.isLoopback() || current.isVirtual())
				continue;
			Enumeration<InetAddress> addresses = current.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress current_addr = addresses.nextElement();
				if (current_addr.isLoopbackAddress())
					continue;
				if (current_addr instanceof Inet4Address)
					return current_addr.getHostAddress();
			}
		}
		throw new SocketException("you are not connected to a network!");
	}
}