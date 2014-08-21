package com.GameName.Entity;

import com.GameName.Physics.Object.PhysicsAccess;
import com.GameName.Physics.Object.PhysicsObject;
import com.GameName.Util.Vector3f;
import com.GameName.World.World;

public class EntityAccess extends PhysicsAccess {
	Entity entity;
		
	protected EntityAccess(PhysicsObject object) {
		super(object);
		
		entity = (Entity) object;
	}
	
	public Entity getEntity() {
		return entity;
	}

	public float getHeight() {
		return entity.height;
	}

	public float getWidth() {
		return entity.width;
	}

	public float getLength() {
		return entity.length;
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
	
	public World getCurrentWorld() {
		return entity.currentWorld;
	}
	
	public Vector3f getRenderPos() {
		return entity.renderPos;
	}

	public Vector3f getAdjust() {
		return entity.adjust;
	}

	public void setHeight(float height) {
		entity.height = height;
	}

	public void setWidth(float width) {
		entity.width = width;
	}

	public void setLength(float length) {
		entity.length = length;
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

	public void setCurrentWorld(World currentWorld) {
		entity.currentWorld = currentWorld;
		
		entity.adjust = new Vector3f(
				(World.SCALE / 10f),
				(World.SCALE / 10f),
				(World.SCALE / 10f)
			);
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
		entity.getAccess().setPos(entity.getAccess().getPos().add(new Vector3f(				
			(float) (amount * Math.cos(Math.toRadians(entity.getAccess().getRot().getY()))), 0.0f,
			(float) (amount * Math.sin(Math.toRadians(entity.getAccess().getRot().getY())))
		)));
				
	}
	
	public void moveY(float amount) {
		entity.getAccess().setPos(entity.getAccess().getPos().add(new Vector3f(0.0f, amount, 0.0f)));
	}
	
	public void moveZ(float amount) {
		entity.getAccess().setPos(entity.getAccess().getPos().add(new Vector3f(				
				(float) (amount * Math.cos(Math.toRadians(entity.getAccess().getRot().getY() + 90))), 0.0f,
				(float) (amount * Math.sin(Math.toRadians(entity.getAccess().getRot().getY() + 90)))
			)));
	}
	
	public void rotateX(float amount) {
		entity.getAccess().setRot(entity.getAccess().getRot().add(new Vector3f(amount, 0.0f, 0.0f)));
	}
	
	public void rotateY(float amount) {
		entity.getAccess().setRot(entity.getAccess().getRot().add(new Vector3f(0.0f, amount, 0.0f)));
	}
	
	public void rotateZ(float amount) {
		entity.getAccess().setRot(entity.getAccess().getRot().add(new Vector3f(0.0f, amount, 0.0f)));
	}
}
