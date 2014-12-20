package com.GameName.World.Generation;

import java.util.Random;

import com.GameName.Engine.GameEngine;
import com.GameName.Engine.ResourceManager.Cubes;
import com.GameName.World.Chunk;
import com.GameName.World.World;

public class EnvironmentGenerator {

	int seed;
	World world;
	private DensityGeneration test;
	Random r = new Random();
	private GameEngine ENGINE;
	int ran = 0;
	
	
	public EnvironmentGenerator(GameEngine eng, int seedI, World worldI) {
		seed = seedI;
		world = worldI;
		test = new DensityGeneration(eng, seed, (int)World.SCALE, world);
		ENGINE = eng;
	}
	
	public Chunk generate(int scale, World world, int x, int y, int z, int seed) {
		Chunk out = new Chunk(ENGINE, scale, world.getId(), x, y, z);
		for(int i=0; i<World.CHUNK_SIZE; i++) {
			for(int j=0; j<World.CHUNK_SIZE; j++) {
				ran=r.nextInt(10);
				for(int k=0; k<World.CHUNK_SIZE; k++) {
					if(y*World.CHUNK_SIZE+k <= 20+ran)
						out.setCubeWithoutUpdate(i, k, j, Cubes.StoneCube);
					else
						out.setCubeWithoutUpdate(i, k, j, Cubes.Air);
				}
			}
		}
		
		return out;
	}
	
//	public Chunk generate(int scale, World world, int x, int y, int z, int seed) {
//		return test.generate(scale, x, y, z);
//	}
	
	
}
