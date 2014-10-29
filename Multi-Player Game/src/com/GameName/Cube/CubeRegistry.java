package com.GameName.Cube;

import java.util.ArrayList;

import com.GameName.Util.Registry;
import com.GameName.Util.RegistryStorage;

public class CubeRegistry extends Registry<Cube> {
	private static Cube[] cubes;
	private static RegistryStorage<Cube> regstries;
	private static ArrayList<Cube> unregisteredCubes;
	
	static {
		regstries = new RegistryStorage<Cube>();
		unregisteredCubes = new ArrayList<Cube>();
	}
	
	public static Cube[] getCubes() {
		return cubes;
	}
	
	public void addCube(Cube cube) {
		registerOBJ(cube);
	}

	public static void register() {regstries.register();}
	
	public static void addRegistry(CubeRegistry reg) {
		regstries.addRegistry(reg);
	}
	
	protected void register(Cube e) {
		unregisteredCubes.add(e);
	}
	
	protected void registrtionConcluded() {
		cubes = unregisteredCubes.toArray(new Cube[unregisteredCubes.size()]);
		
		unregisteredCubes.clear();
		unregisteredCubes = null;
	}
}
