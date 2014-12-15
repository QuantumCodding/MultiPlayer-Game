package com.GameName.Physics;

import com.GameName.Physics.Collision.CollisionEvent;
import com.GameName.Physics.Object.PhysicsObject;

public class PhysicsUtil {
	public static class Collision {
		private CollisionEvent event;
		
		private PhysicsObject object1;
		private PhysicsObject object2;
		
		public Collision(CollisionEvent event, PhysicsObject object1, PhysicsObject object2) {
			this.event = event;
			this.object1 = object1;
			this.object2 = object2;
		}

		public CollisionEvent getEvent() {return event;}
		public PhysicsObject getObject1() {return object1;}
		public PhysicsObject getObject2() {return object2;}		
	}	
	
	public static enum Direction {
		Up, Down, Left, Right, 
		 Forward, Backward
	}
	
	public static enum CardinalDirection {
		North, South, East, West, Top, Bottom
	}
}
