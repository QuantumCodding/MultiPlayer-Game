package com.GameName.World.Generation;

import java.util.Random;

import com.GameName.Engine.ResourceManager.Cubes;
import com.GameName.Physics.Object.PhysicsObject;
import com.GameName.World.Chunk;
import com.GameName.World.World;

public class EnvironmentGenerator {

	int seed;
	World world;
	private DensityGeneration test;
	Random r = new Random();
	
	
	public EnvironmentGenerator(int seedI, World worldI) {
		seed = seedI;
		world = worldI;
		test = new DensityGeneration(seed, (int)World.SCALE, world);
	}
	
	public Chunk generate(int scale, World world, int x, int y, int z, int seed) {
		Chunk output = new Chunk(null, scale, world.getId(), x, y, z);
		for(int i=0; i<World.CHUNK_SIZE; i++) {
			for(int j=0; j<World.CHUNK_SIZE; j++) {
				for(int k=0; k<World.CHUNK_SIZE; k++) {
					output.setCubeWithoutUpdate(i, k, j, (y+k <= 20+r.nextInt(2) ? Cubes.StoneCube:Cubes.Air));
				}
			}
		}
		
		return output;
	}
	
//	public Chunk generate(int scale, World world, int x, int y, int z, int seed) {
//		return test.generate(scale, x, y, z);
//	}
	
	
}
