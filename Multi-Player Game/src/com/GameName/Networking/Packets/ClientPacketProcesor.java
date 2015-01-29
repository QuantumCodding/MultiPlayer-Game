package com.GameName.Networking.Packets;

import java.io.DataInputStream;
import java.io.IOException;

import com.GameName.Engine.GameEngine;
import com.GameName.Networking.NetworkPlayer;

public class ClientPacketProcesor {
	public static void readData(GameEngine ENGINE, int id, DataInputStream in) {
		try {
			if(id == PacketMessage.id) {
				PacketMessage packet = new PacketMessage();
				packet.readInfo(in);
				
				System.out.println(packet.getMessage());
				
			} else if(id == PacketPlayerLocation.id) {
				PacketPlayerLocation packet = new PacketPlayerLocation();
				packet.readInfo(in);
				
				NetworkPlayer player = ENGINE.getClient().getNetworkPlayer(packet.getPlayerID());
				player.setPosition(packet.getPos());
				
			} else if(id == PacketConnectResponse.id) {
				PacketConnectResponse packet = new PacketConnectResponse();
				packet.readInfo(in);
				
				ENGINE.getClient().setID(packet.getUserID());
				ENGINE.getClient().setUsers(new NetworkPlayer[packet.getMaxUsers()]);
				
			} else if(id == PacketPlayerStats.id) {
				PacketPlayerStats packet = new PacketPlayerStats();
				packet.readInfo(in);
				
				NetworkPlayer player = ENGINE.getClient().getNetworkPlayer(packet.getPlayerID());
				
				player.setHealth(packet.getHealth(), false);
				player.setHunger(packet.getHunger(), false);
				player.setMana(packet.getMana(), false);
				
			} else if(id == PacketPlayerInventorySize.id) {
				PacketPlayerInventorySize packet = new PacketPlayerInventorySize();
				packet.readInfo(in);
				
				NetworkPlayer player = ENGINE.getClient().getNetworkPlayer(packet.getPlayerID());
				player.setInvSize(packet.getInvSize(), false);
				
			} else if(id == PacketPlayerInventorySlot.id) {
				PacketPlayerInventorySlot packet = new PacketPlayerInventorySlot();
				packet.readInfo(in);
				
				NetworkPlayer player = ENGINE.getClient().getNetworkPlayer(packet.getPlayerID());
				player.setInvSlot(packet.getSlot(), packet.getInvSlot(), false);
				
			}  			
			
		} catch(IOException e) {
			e.printStackTrace();
		} catch(ClassCastException e) {
			StackTraceElement[] statckTrace = e.getStackTrace();
			
			ClassCastException toThrow = new ClassCastException("Invalid Packet Convertion on ID " + id);
			toThrow.setStackTrace(statckTrace);
			
			throw toThrow;
		}
	}
	
	public static Packet loadPacket(int id, DataInputStream in) {
		Packet toRep;
		
		switch(id) {
			case PacketMessage.id: toRep = new PacketMessage(); break;
			case PacketPlayerLocation.id: toRep = new PacketMessage(); break;
			case PacketConnectResponse.id: toRep = new PacketConnectResponse(); break;
			case PacketPlayerStats.id: toRep = new PacketPlayerStats(); break;
			case PacketPlayerInventorySize.id: toRep = new PacketPlayerInventorySize(); break;
			case PacketPlayerInventorySlot.id: toRep = new PacketPlayerInventorySlot(); break;
			
			default: toRep = null; break;
		}
		
		if(toRep != null) {
			try {
				toRep.readInfo(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return toRep;
	}
}
