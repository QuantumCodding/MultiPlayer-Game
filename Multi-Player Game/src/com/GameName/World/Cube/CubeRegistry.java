package com.GameName.World.Cube;

import java.util.ArrayList;

import com.GameName.Util.Registry;

public class CubeRegistry extends Registry<Cube> {
	private static Cube[] cubes;
	
	public static void register() {
		ArrayList<Cube> unregisteredCubes = new ArrayList<Cube>();
		
		for(Registry<?> reg : getRegistries()) {
			for(Cube cube : (Cube[]) reg.toArray()) {				
				unregisteredCubes.add(cube);
			}
		}
		
		getRegistries().clear();
		cubes = unregisteredCubes.toArray(new Cube[unregisteredCubes.size()]);
				
		isConcluded = true;
	}
	
	public static Cube[] getCubes() {
		return cubes;
	}
	
	public void addCube(Cube cube) {
		register(cube);
	}
}
