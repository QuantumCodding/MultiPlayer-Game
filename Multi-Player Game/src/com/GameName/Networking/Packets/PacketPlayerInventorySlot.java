package com.GameName.Networking.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.GameName.Items.ItemStack;

public class PacketPlayerInventorySlot extends Packet {
	public static final int id = 5;
	private int playerID;
	
	private int invSlot;	
	private ItemStack slot;
	
	public PacketPlayerInventorySlot() {}	

	public PacketPlayerInventorySlot(int playerID, int invSlot, ItemStack slot) {
		super();
		this.playerID = playerID;
		this.invSlot = invSlot;
		this.slot = slot;
	}

	public void readInfo(DataInputStream in) throws IOException {
		playerID = in.readInt();
		invSlot = in.readInt();
		slot = readItemStack(in);
	}

	public void writeInfo(DataOutputStream out) throws IOException {
		out.writeInt(playerID);
		out.writeInt(invSlot);
		writeItemStack(slot, out);
	}

	public int getPlayerID() {
		return playerID;
	}

	public int getInvSlot() {
		return invSlot;
	}

	public ItemStack getSlot() {
		return slot;
	}
}
