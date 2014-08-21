package com.GameName.Networking.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketConnectResponse extends Packet {
	public static final int id = 2;
	private int userID, maxUsers;
	
	public PacketConnectResponse() {}
	
	public PacketConnectResponse(int userID, int maxUsers) {
		this.userID = userID;
		this.maxUsers = maxUsers;
	}

	public void readInfo(DataInputStream in) throws IOException {
		userID = in.readInt();
		maxUsers = in.readInt();
	}

	public void writeInfo(DataOutputStream out) throws IOException {
		out.write(userID);
		out.write(maxUsers);
	}

	public int getUserID() {
		return userID;
	}

	public int getMaxUsers() {
		return maxUsers;
	}

}
