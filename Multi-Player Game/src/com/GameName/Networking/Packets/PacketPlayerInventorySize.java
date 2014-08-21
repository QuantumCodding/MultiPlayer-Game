package com.GameName.Networking.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPlayerInventorySize extends Packet {
	public static final int id = 4;
	private int invSize;
	private int playerID;
	
	public PacketPlayerInventorySize() {}	
	
	public PacketPlayerInventorySize(int invSize, int playerID) {
		this.invSize = invSize;
		this.playerID = playerID;
	}

	public void readInfo(DataInputStream in) throws IOException {
		playerID = in.readInt();
		invSize = in.readInt();
	}

	public void writeInfo(DataOutputStream out) throws IOException {
		out.writeInt(playerID);
		out.writeInt(invSize);
	}

	public int getInvSize() {
		return invSize;
	}

	public int getPlayerID() {
		return playerID;
	}

}
