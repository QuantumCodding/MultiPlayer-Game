package com.GameName.Main.Threads;

import com.GameName.Entity.EntityPlayer;

public class PlayerThread extends GameThread {

	private EntityPlayer player;
	
	public PlayerThread(int tickRate, EntityPlayer player) {
		super(tickRate, "Player Thread");
		
		this.player = player;
	}

	void init() {
//		player.resetPlayer();
	}

	void tick() {
		player.updata();
	}
}
