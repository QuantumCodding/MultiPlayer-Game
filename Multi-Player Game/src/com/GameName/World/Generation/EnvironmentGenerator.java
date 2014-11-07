package com.GameName.World.Generation;

import com.GameName.Cube.Cubes.AirCube;
import com.GameName.Cube.Cubes.TestCube;
import com.GameName.World.Chunk;
import com.GameName.World.World;

public class EnvironmentGenerator {

	int seed;
	World world;
	private SineTestGenerator test;
	
	
	public EnvironmentGenerator(int seedI, World worldI) {
		seed = seedI;
		world = worldI;
		test = new SineTestGenerator(seed, world);
	}
	
//	public Chunk generate(int scale, World world, int x, int y, int z, int seed) {
//		Chunk output = new Chunk(scale, world.getId(), x, y, z);
//		for(int i=0; i<World.CHUNK_SIZE; i++) {
//			for(int j=0; j<World.CHUNK_SIZE; j++) {
//				for(int k=0; k<World.CHUNK_SIZE/2; k++) {
//					output.setCube(i, k, j, new TestCube());
//				}
//				for(int k=World.CHUNK_SIZE/2; k<World.CHUNK_SIZE; k++) {
//					output.setCube(i, k, j, new AirCube());
//				}
//			}
//		}
//		
//		return output;
//	}
	
	public Chunk generate(int scale, World world, int x, int y, int z, int seed) {
		return test.generate(scale, world, x, y, z, seed);
	}
	
	
}
