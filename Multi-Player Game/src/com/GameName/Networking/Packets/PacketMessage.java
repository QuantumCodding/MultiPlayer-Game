package com.GameName.Networking.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketMessage extends Packet {
	public static final int id = 0;
	private String message;
	
	public PacketMessage() {}
	
	public PacketMessage(String message) {
		this.message = message;
	}
	
	public void readInfo(DataInputStream in) throws IOException {
		message = in.readUTF();
	}

	public void writeInfo(DataOutputStream out) throws IOException {
		out.writeUTF(message);
	}

	public String getMessage() {
		return message;
	}

}
