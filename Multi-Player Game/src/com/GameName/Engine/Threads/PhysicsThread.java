package com.GameName.Engine.Threads;

import com.GameName.Physics.PhysicsWorld;

public class PhysicsThread extends GameThread {

	private PhysicsWorld physicsWorld;
	
	public PhysicsThread(int tickRate) {
		super(tickRate, "Physics Thread");	
	}

	void init() {
		
	}

	void tick() {
		physicsWorld.simulate(1f/60f);//timeSinceLastTick * getTickRate());
	}
	
	public void setEngine(PhysicsWorld physicsWorld) {
		this.physicsWorld = physicsWorld;
	}
}
