package com.GameName.Networking.Packets;

import java.io.DataInputStream;
import java.io.IOException;

import com.GameName.Engine.GameEngine;
import com.GameName.Engine.Registries.WorldRegistry;
import com.GameName.Entity.EntityNPC;
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
				
				player.getAccess().setX(packet.getX());
				player.getAccess().setY(packet.getY());
				player.getAccess().setZ(packet.getZ());
				
			} else if(id == PacketConnectResponse.id) {
				PacketConnectResponse packet = new PacketConnectResponse();
				packet.readInfo(in);
				
				ENGINE.getClient().setID(packet.getUserID());
				ENGINE.getClient().setUsers(new NetworkPlayer[packet.getMaxUsers()]);
				
			} else if(id == PacketPlayerStats.id) {
				PacketPlayerStats packet = new PacketPlayerStats();
				packet.readInfo(in);
				
				NetworkPlayer player = ENGINE.getClient().getNetworkPlayer(packet.getPlayerID());
				
				player.getAccess().setHealth(packet.getHealth(), false);
				player.getAccess().setHunger(packet.getHunger(), false);
				player.getAccess().setMana(packet.getMana(), false);
				
				player.getAccess().setPower(packet.getPower(), false);
				player.getAccess().setMoney(packet.getMoney(), false);
				
			} else if(id == PacketPlayerInventorySize.id) {
				PacketPlayerInventorySize packet = new PacketPlayerInventorySize();
				packet.readInfo(in);
				
				NetworkPlayer player = ENGINE.getClient().getNetworkPlayer(packet.getPlayerID());
				player.getAccess().setInvSize(packet.getInvSize(), false);
				
			} else if(id == PacketPlayerInventorySlot.id) {
				PacketPlayerInventorySlot packet = new PacketPlayerInventorySlot();
				packet.readInfo(in);
				
				NetworkPlayer player = ENGINE.getClient().getNetworkPlayer(packet.getPlayerID());
				player.getAccess().setInvSlot(packet.getSlot(), packet.getInvSlot(), false);
				
			}  else if(id == PacketNPCLocation.id) {
				PacketNPCLocation packet = new PacketNPCLocation();
				packet.readInfo(in);
				
				EntityNPC npc = (EntityNPC) WorldRegistry.getWorld(packet.getWorldID()).getObject(packet.getNpcID());
								
				npc.getAccess().setX(packet.getX());
				npc.getAccess().setY(packet.getY());
				npc.getAccess().setZ(packet.getZ());
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
			case PacketNPCLocation.id: toRep = new PacketNPCLocation(); break;
			
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
