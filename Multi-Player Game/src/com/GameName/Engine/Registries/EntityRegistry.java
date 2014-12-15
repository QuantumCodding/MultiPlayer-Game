package com.GameName.Engine.Registries;

import java.util.ArrayList;

import com.GameName.Entity.Entity;

public class EntityRegistry {
	private static Entity[] entities;
	private static ArrayList<Entity> unregisteredEntities;
	
	static {
		unregisteredEntities = new ArrayList<>();
	}
	
	public static void registerEntity(Entity entity) {
		unregisteredEntities.add(entity);
	}	
	
	public static void conclude() {
		entities = unregisteredEntities.toArray(new Entity[unregisteredEntities.size()]);
		
		unregisteredEntities.clear();
		unregisteredEntities = null;
	}	
	
	public static Entity[] getEntities() {
		return entities;
	}
}
