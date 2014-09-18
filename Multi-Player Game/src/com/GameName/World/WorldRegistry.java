package com.GameName.World;

import java.util.ArrayList;

import com.GameName.Util.Registry;

public class WorldRegistry extends Registry<World> {
	private static World[] worlds;
	
	public static void register()  {
	ArrayList<World> unregisteredWorlds = new ArrayList<World>();
		
		int currentId = 0;
		for(Registry<?> reg : getRegistries()) {
			for(World world : (World[]) reg.toArray()) {
				world.setId(currentId);
				unregisteredWorlds.add(world);
				
				currentId ++;
			}
		}
		
		getRegistries().clear();
		worlds = unregisteredWorlds.toArray(new World[unregisteredWorlds.size()]);
				
		isConcluded = true;
	}	
	
	public static World[] getWorlds() {
		return worlds;
	}
	
	public static World getWorld(int index) {
		return worlds[index];
	}
	
	public static World accessByName(String name) {
		for(World world : worlds) {
			if(world != null && world.getName().equals(name)) {
				return world;
			}
		}
		
		return null;
	}
	
	public void addWorld(World world) {
		register(world);
	}
}
