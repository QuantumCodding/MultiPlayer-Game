package com.GameName.Networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.GameName.Engine.GameEngine;
import com.GameName.Entity.EntityPlayer;
import com.GameName.Networking.Packets.Packet;
import com.GameName.Networking.Packets.ServerPacketProcesor;

public class NetworkPlayer extends EntityPlayer implements Runnable {
	private DataInputStream in;
	private DataOutputStream out;
	
	private NetworkPlayer[] users;
	private int id;
	private boolean online;
	
	public NetworkPlayer(GameEngine eng, DataInputStream in, DataOutputStream out, NetworkPlayer[] users, int id) {
		super(eng);
		
		this.in = in;
		this.out = out;
		
		this.users = users;
		this.id = id;
		
		online = true;
	}
	
	public boolean sendPacket(Packet p) {
		try {
			out.writeInt(p.id);
			p.writeInfo(out);
			
			return true;
			
		} catch (IOException e) {
			
			System.err.println("Failed to send message to client!");
			return false;
		}
	}
	
	public void run() {
		while(online) {
			try {
				int packetId = in.readInt();				
				ServerPacketProcesor.readData(ENGINE, packetId, in);
				
			} catch (IOException e) {				
				in = null;
				out = null;
				users[id] = null;					
			}
			
		}
		
		try {
			for(int i = 0; i < users.length; i ++)
				if(users[i] != null) users[i].out.writeUTF(id + " has left the server.");
		} catch (IOException e) {}
		
		Thread t = new Thread(users[id]);
		t.interrupt();
		
		in = null;
		out = null;
		users[id] = null;
	}		
}

