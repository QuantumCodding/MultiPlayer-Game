package com.GameName.World.Generation;

import java.util.Random;

import com.GameName.Cube.Cubes.AirCube;
import com.GameName.Cube.Cubes.TestCube;
import com.GameName.World.Chunk;
import com.GameName.World.World;

public class EnvironmentGenerator {

	public EnvironmentGenerator(int seed, World world) {
		
	}
	
	public Chunk generate(int scale, World world, int x, int y, int z, int seed) {
		Chunk output = new Chunk(scale, world.getId(), x, y, z);
		Random r = new Random(seed);
		for(int i=0; i<world.CHUNK_SIZE; i++) {
			for(int j=0; j<world.CHUNK_SIZE; j++) {
				for(int k=0; k<world.CHUNK_SIZE/2; k++) {
					output.setCube(i, k, j, new TestCube());
				}
				for(int k=world.CHUNK_SIZE/2; k<world.CHUNK_SIZE; k++) {
					output.setCube(i, k, j, new AirCube());
				}
			}
		}
		
		return output;
	}
	
	
}
