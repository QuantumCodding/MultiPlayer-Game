package com.GameName.Engine.Registries;

import java.util.ArrayList;

import com.GameName.Engine.GameEngine;
import com.GameName.World.World;

public class WorldRegistry {
	private static World[] worlds;
	private static ArrayList<World> unregisteredWorlds;
	
	private static int currentId;
	
	static {
		unregisteredWorlds = new ArrayList<World>();
	}
	
	public static void registryWorld(World reg) {
		unregisteredWorlds.add(reg);
	}
	
	public static void conclude(GameEngine ENGINE) {
		for(World world : unregisteredWorlds) {
			world.setId(currentId);
			world.setEngine(ENGINE);
			currentId ++;
		}
		
		worlds = unregisteredWorlds.toArray(new World[unregisteredWorlds.size()]);
		
		unregisteredWorlds.clear();
		unregisteredWorlds = null;
	}
	
	public static World[] getWorlds() {
		return worlds;
	}
	
	public static World accessByName(String name) {
		for(World world : getWorlds()) {
			if(world.getName().equals(name)) {
				return world;
			}
		}
		
		return null;
	}
	
	public static World getWorld(int index) {
		return getWorlds()[index];
	}
}
