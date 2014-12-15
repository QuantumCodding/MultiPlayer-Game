package com.GameName.Networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.GameName.Engine.GameEngine;
import com.GameName.Networking.Packets.ClientPacketProcesor;
import com.GameName.Networking.Packets.Packet;

public class Client implements Runnable {
	private final GameEngine ENGINE;
	private Socket serverPort;

	private DataInputStream in;
	private DataOutputStream out;
	
	private boolean onServer;
	private int ID = -1;
	
	private NetworkPlayer[] users;
	
	public Client(GameEngine eng) {
		ENGINE = eng;
		onServer = false;
	}
	
	private Client(GameEngine eng, DataInputStream in) {
		ENGINE = eng;
		
		onServer = false;
		this.in = in;
	}
	
	public boolean isOnServer() {
		return onServer;
	}
	
	public int getID() {
		return ID;
	}
	
	public void setID(int iD) {
		ID = iD;
	}

	public void setUsers(NetworkPlayer[] users) {
		this.users = users;
	}

	public NetworkPlayer getNetworkPlayer(int id) {
		return users[id]; 
	}

	public static Client joinServer(GameEngine eng, String ip, int port) {		
		try {
			Client input = new Client(eng);
			
			input.serverPort = new Socket(ip, port);
			
			input.out = new DataOutputStream(input.serverPort.getOutputStream());	
			
			input = new Client(eng, new DataInputStream(input.serverPort.getInputStream()));
			input.onServer = true;
			
			Thread t = new Thread(input);
			t.setName("Client Thread for IPv4 " + input.serverPort.getInetAddress() + " on port " + input.serverPort.getLocalPort());
			t.start();
			
			return input;
		} catch (IOException e) {
			System.err.println("Failed to join server!");
			e.printStackTrace();
		}		
		
		return new Client(eng);
	}
	
	public boolean sendPacket(Packet p) {
		try {
			out.writeInt(p.id);
			p.writeInfo(out);
			
			return true;
			
		} catch (IOException e) {
			
			System.err.println("Failed to send message to server!");
			return false;
		}
	}
	
	public void run() {
		while(onServer) {
			try {
				int packetId = in.readInt();				
				ClientPacketProcesor.readData(ENGINE, packetId, in);
				
			} catch (IOException e) {
				System.err.println();
			}
		}
	}
}
