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
		Chunk out = new Chunk(ENGINE, World.CHUNK_SIZE, world.getId(), x, y, z);
		
		for(int xi=0; xi<World.CHUNK_SIZE; xi++) {
			for(int zi=0; zi<World.CHUNK_SIZE; zi++) {
				double elevation = SimplexNoise.noise((double)(x*World.CHUNK_SIZE+xi)/World.CHUNK_SIZE/8, (double)(z*World.CHUNK_SIZE+zi)/World.CHUNK_SIZE/8);
//				double roughness = SimplexNoise.noise((double)(x*World.CHUNK_SIZE+xi)/World.CHUNK_SIZE/4, (double)(z*World.CHUNK_SIZE+zi)/World.CHUNK_SIZE/4)*2;
//				double detail = SimplexNoise.noise((double)(x*World.CHUNK_SIZE+xi)/World.CHUNK_SIZE, (double)(z*World.CHUNK_SIZE+zi)/World.CHUNK_SIZE);
				double h = elevation;
				h = (h/2)*World.CHUNK_SIZE;
				for(int yi=0; yi<World.CHUNK_SIZE; yi++) {
					
					if(y*World.CHUNK_SIZE+yi<h+world.getSizeY())
						out.setCubeWithoutUpdate(xi, yi, zi, Cubes.StoneCube);
					else
						out.setCubeWithoutUpdate(xi, yi, zi, Cubes.Air);
				}
			}
		}
		
		return out;
	}
	
	
}
