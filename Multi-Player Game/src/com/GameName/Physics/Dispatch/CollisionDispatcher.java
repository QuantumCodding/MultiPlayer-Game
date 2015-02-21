package com.GameName.Physics.Dispatch;

import java.util.ArrayList;

import com.GameName.Physics.PhysicsObject;

public abstract class CollisionDispatcher {
	public abstract ArrayList<IntersectionResult> checkCollisions(ArrayList<PhysicsObject> objects);
}
