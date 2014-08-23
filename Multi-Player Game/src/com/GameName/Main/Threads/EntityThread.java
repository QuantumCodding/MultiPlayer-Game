package com.GameName.Main.Threads;

import java.util.ArrayList;
import java.util.Collection;

import com.GameName.Entity.Entity;

public class EntityThread extends Timer {

	private ArrayList<Entity> entities;
	
	public EntityThread(int tickRate) {
		super(tickRate, "Entity Thread");
		
		entities = new ArrayList<Entity>();
	}

	void init() {

	}

	void tick() {
		for(Entity entity : entities) {
			entity.updata();
		}
	}
	
	public void add(Entity entity) {
		entities.add(entity);
	}	
	
	public void addAll(Collection<Entity> entities) {
		this.entities.addAll(entities);
	}
	
	public boolean remove(Entity entity) {
		return entities.remove(entity);
	}
	
	public boolean removeAll(Collection<Entity> entities) {
		return this.entities.removeAll(entities);
	}
	
	public void clear() {
		entities.clear();
	}

}
