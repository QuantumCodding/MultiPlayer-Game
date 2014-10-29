package com.GameName.World;

import java.util.ArrayList;

import com.GameName.Util.Registry;
import com.GameName.Util.RegistryStorage;

public class WorldRegistry extends Registry<World> {
	private static World[] worlds;
	private static RegistryStorage<World> regstries;
	private static ArrayList<World> unregisteredWorlds;
	
	private int currentId;
	
	static {
		regstries = new RegistryStorage<World>();
		unregisteredWorlds = new ArrayList<World>();
	}
	
	public static World[] getWorlds() {
		return worlds;
	}
	
	public void addWorld(World world) {
		registerOBJ(world);
	}

	public static void register() {regstries.register();}
	
	public static void addRegistry(WorldRegistry reg) {
		regstries.addRegistry(reg);
	}
	
	protected void register(World e) {
		e.setId(currentId);
		unregisteredWorlds.add(e);
		currentId ++;
	}
	
	protected void registrtionConcluded() {
		worlds = unregisteredWorlds.toArray(new World[unregisteredWorlds.size()]);
		
		unregisteredWorlds.clear();
		unregisteredWorlds = null;
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
