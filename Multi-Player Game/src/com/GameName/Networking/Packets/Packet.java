package com.GameName.Networking.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.GameName.Items.ItemStack;

public abstract class Packet {
	public int id = -1;
	
	public Packet() {}
	
	public abstract void readInfo(DataInputStream in) throws IOException;
	public abstract void writeInfo(DataOutputStream out) throws IOException;
	
	protected void writeItemStack(ItemStack item, DataOutputStream out) throws IOException {
		out.writeInt(item.getItem().getId());
		out.writeInt(item.getSize());
	}
	
	protected ItemStack readItemStack(DataInputStream in) throws IOException {
		return new ItemStack(in.readInt(), in.readInt());
	}
}
