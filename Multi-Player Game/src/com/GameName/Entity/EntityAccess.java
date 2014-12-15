package com.GameName.Entity;

import com.GameName.Engine.GameEngine;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.Object.WorldObject;
import com.GameName.World.Object.WorldObjectAccess;

public class EntityAccess extends WorldObjectAccess {
	Entity entity;
		
	protected EntityAccess(GameEngine eng, WorldObject object) {
		super(eng, object);
		
		entity = (Entity) object;
	}
	
	public Entity getEntity() {
		return entity;
	}

	public int getMaxHealth() {
		return entity.maxHealth;
	}

	public int getHealth() {
		return entity.health;
	}

	public int getMaxMana() {
		return entity.maxMana;
	}

	public int getMana() {
		return entity.mana;
	}

	public int getMaxHunger() {
		return entity.maxHunger;
	}

	public int getHunger() {
		return entity.hunger;
	}
	
	public void setMaxHealth(int maxHealth) {
		entity.maxHealth = maxHealth;
	}

	public void setMaxMana(int maxMana) {
		entity.maxMana = maxMana;
	}

	public void setMaxHunger(int maxHunger) {
		entity.maxHunger = maxHunger;
	}

	public void setHealth(int health) {
		entity.health = health;
	}

	public void setMana(int mana) {
		entity.mana = mana;
	}

	public void setHunger(int hunger) {
		entity.hunger = hunger;
	}
	
	public void moveX(float amount) {
		getForce().setX(getForce().getX() + (float) (amount * Math.cos(Math.toRadians(getRot().getY())) * -1));
		getForce().setZ(getForce().getZ() + (float) (amount * Math.sin(Math.toRadians(getRot().getY())) * -1));
	}
	
	public void moveY(float amount) {
		getForce().setY(getForce().getY() + amount);
	}
	
	public void moveZ(float amount) {
		getForce().setX(getForce().getX() + (float) (amount * Math.cos(Math.toRadians(getRot().getY() + 90)) * -1));
		getForce().setZ(getForce().getZ() + (float) (amount * Math.sin(Math.toRadians(getRot().getY() + 90)) * -1));
	}
	
	public void rotateX(float amount) {
		getRot().addAndSet(new Vector3f(amount, 0.0f, 0.0f));
	}
	
	public void rotateY(float amount) {
		getRot().addAndSet(new Vector3f(0.0f, amount * -1, 0.0f));
	}
	
	public void rotateZ(float amount) {
		getRot().addAndSet(new Vector3f(0.0f, amount, 0.0f));
	}
}
