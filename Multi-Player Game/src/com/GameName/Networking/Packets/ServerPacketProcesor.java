package com.GameName.Networking.Packets;

import java.io.DataInputStream;
import java.io.IOException;

import com.GameName.Engine.GameEngine;
import com.GameName.Networking.NetworkPlayer;

public class ServerPacketProcesor {
	public static void readData(GameEngine ENGINE, int id, DataInputStream in) {
		try {
			if(id == PacketMessage.id) {
				PacketMessage packet = new PacketMessage();
				packet.readInfo(in);
				
				System.out.println(packet.getMessage());
				
			} else if(id == PacketPlayerLocation.id) {
				PacketPlayerLocation packet = new PacketPlayerLocation();
				packet.readInfo(in);
				
				NetworkPlayer player = ENGINE.getServer().users[packet.getPlayerID()];
				player.setPosition(packet.getPos());				
				
			} else if(id == PacketConnectResponse.id) {
				System.err.println("Invalid Packet Read by Server Packet: PacketConnectResponse");
				
			} else if(id == PacketPlayerStats.id) {
				PacketPlayerStats packet = new PacketPlayerStats();
				packet.readInfo(in);
				
				NetworkPlayer player = ENGINE.getServer().users[packet.getPlayerID()];
				
				player.setHealth(packet.getHealth(), false);
				player.setHunger(packet.getHunger(), false);
				player.setMana(packet.getMana(), false);
				
			} else if(id == PacketPlayerInventorySize.id) {
				PacketPlayerInventorySize packet = new PacketPlayerInventorySize();
				packet.readInfo(in);
				
				NetworkPlayer player = ENGINE.getServer().users[packet.getPlayerID()];
				player.setInvSize(packet.getInvSize(), false);
				
			} else if(id == PacketPlayerInventorySlot.id) {
				PacketPlayerInventorySlot packet = new PacketPlayerInventorySlot();
				packet.readInfo(in);
				
				NetworkPlayer player = ENGINE.getServer().users[packet.getPlayerID()];
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
}
