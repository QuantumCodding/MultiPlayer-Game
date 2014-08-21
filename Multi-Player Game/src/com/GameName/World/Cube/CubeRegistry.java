package com.GameName.World.Cube;

import java.util.ArrayList;

public class CubeRegistry {
	private ArrayList<Cube> cubes = new ArrayList<Cube>();
		
	public void addCube(Cube cube) {
		cubes.add(cube);
	}
	
	public Cube[] toArray() {
		Cube[] toRep = new Cube[cubes.size()];
		toRep = cubes.toArray(toRep);
		
		return toRep;
	}
}
