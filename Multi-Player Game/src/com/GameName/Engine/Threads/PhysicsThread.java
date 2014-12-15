package com.GameName.Engine.Threads;

import com.GameName.Physics.PhysicsEngine;

public class PhysicsThread extends GameThread {

	private PhysicsEngine physicsEngine;
	
	public PhysicsThread(int tickRate) {
		super(tickRate, "Physics Thread");	
	}

	void init() {

	}

	void tick() {
		physicsEngine.step(timeSinceLastTick * getTickRate());
	}
	
	public void setEngine(PhysicsEngine physicsEngine) {
		this.physicsEngine = physicsEngine;
	}
}
