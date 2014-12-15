package com.GameName.Engine.Threads;

import java.util.ArrayList;

import com.GameName.Entity.Entity;

public class EntityThread extends GameThread {
	private ArrayList<Entity> entities;
	
	public EntityThread(int tickRate) {
		super(tickRate, "Entity Thread");		
		entities = new ArrayList<>();
	}

	void init() {

	}

	void tick() {
		for(Entity entity : entities) {
			if(entity == null) {
				removeEntity(entity); 
				continue;
			}			
			
			entity.update();
		}
	}
	
	public boolean addEntity(Entity entity) {
		return entities.add(entity);
	}
	
	public boolean removeEntity(Entity entity) {
		return entities.remove(entity);
	}
	
	public void clear() {
		entities.clear();
	}
}
