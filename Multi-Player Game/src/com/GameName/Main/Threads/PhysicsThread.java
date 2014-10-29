package com.GameName.Main.Threads;

import com.GameName.Physics.PhysicsEngine;

public class PhysicsThread extends GameThread {

	private PhysicsEngine physicsEngine;
	
	public PhysicsThread(int tickRate, PhysicsEngine physicsEngine) {
		super(tickRate, "Physics Thread");	
		
		this.physicsEngine = physicsEngine;
	}

	void init() {

	}

	void tick() {
		physicsEngine.step(1);
	}
}
