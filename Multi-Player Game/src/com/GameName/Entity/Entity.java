package com.GameName.Entity;

import com.GameName.World.Object.WorldObject;

public abstract class Entity extends WorldObject {
	protected int maxHealth;
	protected int health;	
	
	protected int maxMana;
	protected int mana;	
	
	protected int maxHunger;
	protected int hunger;	

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
		
		maxHealth = 10;        health = maxHealth;
		maxMana = 10;          mana = maxMana;    
		maxHunger = 100;       hunger = maxHunger;
				
		init();
	}
	
	protected abstract void init();
	
	public void updata() {
		super.updata();
		
//		applyGravity(currentWorld);
	}
	
	public EntityAccess getAccess() {
		return access;
	}
}
