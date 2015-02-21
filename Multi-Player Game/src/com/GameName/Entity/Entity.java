package com.GameName.Entity;

import com.GameName.Engine.GameEngine;
import com.GameName.Physics.Material;
import com.GameName.Physics.Collision.CollisionShape;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.Object.WorldObject;

public abstract class Entity extends WorldObject {
	protected int mana, hunger, health;
	protected int maxHealth, maxMana, maxHunger;
	protected final GameEngine ENGINE;
	
	/**
	 * Default Constructor 
	 *    Sets x, y, z, rotX, rotY, and rotZ to 0
	 *    Sets length, width, and height to 1
	 *    Sets max health and max mana to 10 
	 *    Sets max hunger to 100
	 */
	public Entity(CollisionShape shape, Material material, GameEngine eng) {
		super(shape, material);
		ENGINE = eng;
		
		maxHealth = 10;        health = maxHealth;
		maxMana = 10;          mana = maxMana;    
		maxHunger = 100;       hunger = maxHunger;
		
		init();
	}
	
	abstract void init();
	public void reset() {	
		super.reset();
		
		health = maxHealth;
		hunger = maxHunger;
		mana = maxMana;
	}
	
	
	public void moveX(float amount) {
		applyForce(new Vector3f((float) (amount * Math.cos(Math.toRadians(getRotation().y)) * -1), 
				0, (float) (amount * Math.sin(Math.toRadians(getRotation().y)) * -1)));
	}
	
	public void moveZ(float amount) {
		applyForce(new Vector3f((float) (amount * Math.cos(Math.toRadians(getRotation().y + 90)) * -1), 
				0, (float) (amount * Math.sin(Math.toRadians(getRotation().y + 90)) * -1)));
	}
	
	public void moveY(float amount) {
		applyForce(new Vector3f(0, amount, 0));
	}
	
	public void rotateX(float amount) {applyTorque(new Vector3f(amount, 0, 0));}
	public void rotateY(float amount) {applyTorque(new Vector3f(0, amount, 0));}
	public void rotateZ(float amount) {applyTorque(new Vector3f(0, 0, amount));}

	public int getHealth() 	{ return health; }
	public int getHunger() 	{ return hunger; }
	public int getMana() 	{ return mana; }
	
	public int getMaxHealth() { return maxHealth; }
	public int getMaxHunger() { return maxHunger; }
	public int getMaxMana()   { return maxMana; }
	
	public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }
	public void setMaxHunger(int maxHunger) { this.maxHunger = maxHunger; }
	public void setMaxMana(int maxMana) { this.maxMana = maxMana; }

	public void setHealth(int health) { this.health = health; }
	public void setHunger(int hunger) { this.hunger = hunger; }
	public void setMana(int mana) { this.mana = mana; }
}
