package com.GameName.Main.Threads;

import java.util.ArrayList;
import java.util.Collection;

import com.GameName.Physics.PhysicsEngine;
import com.GameName.Physics.Object.PhysicsObject;

public class PhysicsThread extends Timer {

	private PhysicsEngine physicsEngine;
	private ArrayList<PhysicsObject> objects;
	
	public PhysicsThread(int tickRate, PhysicsEngine physicsEngine) {
		super(tickRate, "Physics Thread");	
		
		this.physicsEngine = physicsEngine;
		objects = new ArrayList<PhysicsObject>();
	}

	void init() {

	}

	void tick() {
		physicsEngine.simulate(objects, 1);
		ArrayList<PhysicsEngine.Collision> collisions = physicsEngine.detectCollisions(objects);
		physicsEngine.handelCollisions(objects, collisions);
	}

	public void add(PhysicsObject object) {
		objects.add(object);
	}	
	
	public void addAll(Collection<PhysicsObject> objects) {
		this.objects.addAll(objects);
	}
	
	public boolean remove(PhysicsObject object) {
		return objects.remove(object);
	}
	
	public boolean removeAll(Collection<PhysicsObject> objects) {
		return this.objects.removeAll(objects);
	}
	
	public void clear() {
		objects.clear();
	}
}
