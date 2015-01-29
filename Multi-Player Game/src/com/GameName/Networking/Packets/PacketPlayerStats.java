package com.GameName.Networking.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPlayerStats extends Packet {
	public static final int id = 3;
	
	private int playerID;
	private int hunger, health, mana;
	
	public PacketPlayerStats() {}

	public PacketPlayerStats(int playerID, int hunger, int health, int mana) {
		this.playerID = playerID;
		
		this.hunger = hunger;
		this.health = health;
		this.mana = mana;
	}

	public void readInfo(DataInputStream in) throws IOException {
		playerID = in.readInt();
		
		health = in.readInt();
		hunger = in.readInt();
		mana = in.readInt();
	}

	public void writeInfo(DataOutputStream out) throws IOException {
		out.writeInt(playerID);
		
		out.writeInt(health);
		out.writeInt(hunger);
		out.writeInt(mana);
	}

	public int getPlayerID() {
		return playerID;
	}

	public int getHunger() {
		return hunger;
	}

	public int getHealth() {
		return health;
	}

	public int getMana() {
		return mana;
	}
}
