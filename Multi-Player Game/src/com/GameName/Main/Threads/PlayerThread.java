package com.GameName.Main.Threads;

import com.GameName.Entity.EntityPlayer;

public class PlayerThread extends Timer {

	private EntityPlayer player;
	
	public PlayerThread(int tickRate, EntityPlayer player) {
		super(tickRate, "Player Thread");
		
		this.player = player;
	}

	void init() {

	}

	void tick() {
		player.updata();
	}
}
