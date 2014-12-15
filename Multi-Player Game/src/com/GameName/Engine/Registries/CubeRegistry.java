package com.GameName.Engine.Registries;

import java.util.ArrayList;

import com.GameName.Cube.Cube;

public class CubeRegistry {
	private static Cube[] cubes;
	private static ArrayList<Cube> unregisteredCubes;
	
	static {
		unregisteredCubes = new ArrayList<>();
	}

	public static void registerCube(Cube reg) {
		unregisteredCubes.add(reg);
	}
	
	public static void conclude() {
		for(Cube cube : unregisteredCubes) {
			cube.concludeInit(); //TODO: FIX WARNING
		}
		
		cubes = unregisteredCubes.toArray(new Cube[unregisteredCubes.size()]);
		
		unregisteredCubes.clear();
		unregisteredCubes = null;
	}
	
	public static Cube[] getCubes() {
		return cubes;
	}
}
