package com.GameName.Entity;

import com.GameName.Physics.Coalition.BoundingBox;
import com.GameName.Physics.Object.PhysicsObject;
import com.GameName.Util.Vector3f;
import com.GameName.World.World;

public abstract class Entity extends PhysicsObject {
	protected Vector3f renderPos, adjust;	
	protected float height, width, length;
	
	protected int maxHealth;
	protected int health;	
	
	protected int maxMana;
	protected int mana;	
	
	protected int maxHunger;
	protected int hunger;	

	protected World currentWorld;	
	
	EntityAccess access;
	
	/**
	 * Default Constructor 
	 *    Sets x, y, z, rotX, rotY, and rotZ to 0
	 *    Sets length, width, and height to 1
	 *    Sets max health and max mana to 10 
	 *    Sets max hunger to 100
	 */
	public Entity() {
		super();		
		height = 1;	width = 1; length = 1;
		
		renderPos = new Vector3f(0, 0, 0);
		
		maxHealth = 10;        health = maxHealth;
		maxMana = 10;          mana = maxMana;    
		maxHunger = 100;       hunger = maxHunger;
				
		init();
		addBounding();
	}
	
	protected abstract void init();
	
	public void updata() {
		renderPos = pos.multiply(adjust);
		
		applyGravity(currentWorld);
	}
	
	public void addBounding() {
		bounding.add(new BoundingBox(
				new Vector3f(pos.getX() - (width / 2f), pos.getY() - (height / 2f), pos.getZ() - (length / 2f)), 
				new Vector3f(pos.getX() + (width / 2f), pos.getY() + (height / 2f), pos.getZ() + (length / 2f))
			));
	}
	
	public EntityAccess getAccess() {
		return access;
	}
}
