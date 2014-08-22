package com.GameName.Physics.Coalition;

import java.util.ArrayList;
import java.util.Collection;

import com.GameName.Util.Vector3f;

public class BoundingArea {
	private ArrayList<Collidable> boundingObjetcts;
	
	public BoundingArea() {
		boundingObjetcts = new ArrayList<Collidable>();
	}
	
	public BoundingArea(BoundingArea copy) {
		boundingObjetcts = copy.clone();
	}
	
	public void add(Collidable c) {
		boundingObjetcts.add(c);
	}
	
	public void addAll(Collection<Collidable> c) {
		boundingObjetcts.addAll(c);
	}
	
	public CollisionEvent intersect(BoundingArea other) {
		for(int i = 0; i < boundingObjetcts.size(); i ++) {
			for(int j = i + 1; j < other.getBoundingObjects().size(); j ++) {
				CollisionEvent event = boundingObjetcts.get(i).intersect(other.getCollidable(j));
				
				if(event.isColliding()) return event;
			}
		}
		
		return new CollisionEvent(false, getAvgCenter().distance(other.getAvgCenter()));
	}
	
	public Vector3f getAvgCenter() {
		Vector3f avgCenter = new Vector3f(0, 0, 0);
		
		for(Collidable c : boundingObjetcts) {
			avgCenter.add(c.getCenter());
		}
		
		return avgCenter.divide(boundingObjetcts.size());
	}
	
	public Collidable getCollidable(int get) {
		return boundingObjetcts.get(get);
	}
	
	public ArrayList<Collidable> getBoundingObjects() {
		return boundingObjetcts;
	}
	
	public ArrayList<Collidable> clone() {
		ArrayList<Collidable> toRep = new ArrayList<Collidable>();
		
		for(Collidable c : boundingObjetcts) {
			toRep.add(c);
		}
		
		return toRep;
	}
}
