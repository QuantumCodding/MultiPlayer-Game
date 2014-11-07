package com.GameName.World.Generation;

import com.GameName.Cube.Cube;
import com.GameName.World.Chunk;
import com.GameName.World.World;

public class SineTestGenerator {

	int seed;
	World world;
	
	public SineTestGenerator(int seedI, World worldI) {
		seed = seedI;
		world = worldI;
	}
	
	public Chunk generate(int scale, World world, int x, int y, int z, int seed) {
		Chunk output = new Chunk(scale, world.getId(), x, y, z);
		
		int global_X, global_Y, global_Z;
		
		for(int i=0; i<World.CHUNK_SIZE; i++) {
			for(int j=0; j<World.CHUNK_SIZE; j++) {
				for(int k=0; k<World.CHUNK_SIZE; k++) {
					global_X = ((x*10)+i);
					global_Y = ((y*10)+j);
					global_Z = ((z*10)+k);
					if(global_Y <= (   (Math.sin(100*global_X)+Math.sin(100*global_Z))   )) {
						System.out.println((Math.sin(100*global_X)+Math.sin(100*global_Z)));
						output.setCubeWithoutUpdate(i, k, j, Cube.TestCube);
					}
					else {
						output.setCubeWithoutUpdate(i, k, j, Cube.Air);
					}
				}
			}
		}
		
		return output;
	}
}
