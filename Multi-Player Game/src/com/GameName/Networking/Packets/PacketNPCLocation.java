package com.GameName.Networking.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketNPCLocation extends Packet {
	public static final int id = 6;	
	private float x, y, z;
	private int npcID, worldID;
	
	public PacketNPCLocation() {}
	
	public PacketNPCLocation(int npcID, float x, float y, float z, int worldID) {
		this.npcID = npcID;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.worldID = worldID;
	}

	public void readInfo(DataInputStream in) throws IOException {
		npcID = in.readInt();
		
		x = in.readFloat();
		y = in.readFloat();
		z = in.readFloat();
		
		worldID = in.readInt();
	}

	public void writeInfo(DataOutputStream out) throws IOException {
		out.write(npcID);
		
		out.writeFloat(x);
		out.writeFloat(y);
		out.writeFloat(z);
		
		out.writeInt(worldID);
	}
	
	public int getNpcID() {
		return npcID;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}
	
	public int getWorldID() {
		return worldID;
	}

}
