package com.GameName.Entity;

import com.GameName.Util.Vector3f;
import com.GameName.World.Object.WorldObject;
import com.GameName.World.Object.WorldObjectAccess;

public class EntityAccess extends WorldObjectAccess {
	Entity entity;
		
	protected EntityAccess(WorldObject object) {
		super(object);
		
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
		getPos().addE(new Vector3f(				
			(float) (amount * Math.cos(Math.toRadians(getRot().getY())) * (Math.abs(getRot().getX()) > 90 && Math.abs(getRot().getX()) <= 270 ? -1 : 1)), 0.0f,
			(float) (amount * Math.sin(Math.toRadians(getRot().getY())) * (Math.abs(getRot().getX()) > 90 && Math.abs(getRot().getX()) <= 270 ? -1 : 1))
		));
				
	}
	
	public void moveY(float amount) {
		setPos(getPos().add(new Vector3f(0.0f, amount, 0.0f)));
	}
	
	public void moveZ(float amount) {
		getPos().addE(new Vector3f(				
				(float) (amount * Math.cos(Math.toRadians(getRot().getY() + 90)) * (Math.abs(getRot().getX()) > 90 && Math.abs(getRot().getX()) <= 270 ? -1 : 1)), 0.0f,
				(float) (amount * Math.sin(Math.toRadians(getRot().getY() + 90)) * (Math.abs(getRot().getX()) > 90 && Math.abs(getRot().getX()) <= 270 ? -1 : 1))
			));
	}
	
	public void rotateX(float amount) {
		getRot().addE(new Vector3f(amount, 0.0f, 0.0f));
	}
	
	public void rotateY(float amount) {
		getRot().addE(new Vector3f(0.0f, amount * (Math.abs(getRot().getX()) > 90 && Math.abs(getRot().getX()) <= 270 ? -1 : 1), 0.0f));
	}
	
	public void rotateZ(float amount) {
		getRot().addE(new Vector3f(0.0f, amount, 0.0f));
	}
}
