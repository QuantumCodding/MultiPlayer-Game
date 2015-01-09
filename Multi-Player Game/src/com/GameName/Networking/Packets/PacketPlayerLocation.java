package com.GameName.Networking.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.GameName.Util.Vectors.MathVec3f;

public class PacketPlayerLocation extends Packet {
	public static final int id = 1;	
	private float x, y, z;
	private int playerID;
	
	public PacketPlayerLocation() {}
	
	public PacketPlayerLocation(int playerID, MathVec3f pos) {
		this.playerID = playerID;
		
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}

	public void readInfo(DataInputStream in) throws IOException {
		playerID = in.readInt();
		
		x = in.readFloat();
		y = in.readFloat();
		z = in.readFloat();
	}

	public void writeInfo(DataOutputStream out) throws IOException {
		out.write(playerID);
		
		out.writeFloat(x);
		out.writeFloat(y);
		out.writeFloat(z);
	}
	
	public int getPlayerID() {
		return playerID;
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

}
