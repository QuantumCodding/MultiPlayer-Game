package com.GameName.World.Generation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

import com.GameName.Engine.GameEngine;
import com.GameName.Engine.ResourceManager.Cubes;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.Chunk;
import com.GameName.World.World;

public class DensityGeneration {

	private int seed, scale;
	private World world;
	private Random r;
	private HashMap<Vector3f, DensityNode> nodes = new HashMap<>();
	private GameEngine ENGINE;
	private Scanner nodeReader;
	private BufferedWriter nodeWriter;
	
	public DensityGeneration(GameEngine eng, int seedI, int scaleI, World worldI) {
		seed = seedI;
		scale = scaleI;
		world = worldI;
		r = new Random(seed);
		pullNodes();
		ENGINE = eng;
	}
	
	public Chunk generate(int scale,int x,int y,int z) {
		Chunk out = new Chunk(ENGINE, scale, world.getId(), x, y, z);;
		addToNodeFile(new DensityNode(x, y, z,(((float)r.nextInt(8)+1f)/10f)));
		ArrayList<DensityNode> nearNodes = new ArrayList<>();
		for(int i=0; i<2; i++) {
			for(int j=0; j<2; j++) {
				for(int k=0; k<2; k++) {
					if(nodeExists(i, j, k))
						nearNodes.add(getNode(i, j, k));
					else {
						nodes.put(new Vector3f(i, j, k), new DensityNode(i, j, k, r.nextFloat()));
					}
				}
			}
		}
		for(int i=0; i<World.CHUNK_SIZE; i++) {
			for(int j=0; j<World.CHUNK_SIZE; j++) {
				for(int k=(i==0&&j==0 ? 1:0); k<World.CHUNK_SIZE; k++) {
					ArrayList<Float> distances = new  ArrayList<>();
					for(DensityNode n : nearNodes) {
						distances.add((float)(Math.sqrt(
								Math.abs((n.getX()*World.CHUNK_SIZE)-(i+x*World.CHUNK_SIZE))+
								Math.abs((n.getY()*World.CHUNK_SIZE)-(j+y*World.CHUNK_SIZE))+
								Math.abs((n.getZ()*World.CHUNK_SIZE)-(k+z*World.CHUNK_SIZE)))));
					}					
					float totalSum=0;
					
					for(int l=0; l<distances.size(); l++) {
						totalSum += (nearNodes.get(l).getValue()/
								distances.get(l));
					}
//					System.out.println("Sum:" + totalSum);
					if(totalSum >= .3f)
						out.setCubeWithoutUpdate(1, j, k, Cubes.StoneCube);
					else
						out.setCubeWithoutUpdate(1, j, k, Cubes.Air);
				}
			}
		}
		
		
		
		
		
		return out;
	}
			
			
	private void addToNodeFile(DensityNode n) {
		try {
			nodeReader = new Scanner(new File(world.getFileLoc()+"/generation/node.nodes"));
			nodeWriter = new BufferedWriter(new FileWriter(new File(world.getFileLoc()+"/generation/node.nodes")));
		} catch (IOException e) { e.printStackTrace(); }
		nodeReader.useDelimiter("-");
		HashSet<DensityNode> nodz = new HashSet<DensityNode>();
		int xi, yi, zi;
		float valuei;
		while(nodeReader.hasNext()) {
			xi=nodeReader.nextInt();
			yi=nodeReader.nextInt();
			zi=nodeReader.nextInt();
			valuei=nodeReader.nextFloat();
			nodz.add(new DensityNode(xi, yi, zi, valuei));
		}
		nodz.add(n);
		
		for(DensityNode no : nodz) {
			try { nodeWriter.write(no.getX()+"-"+no.getY()+"-"+no.getZ()+"-"+no.getValue()+"-"); } catch (IOException e) { e.printStackTrace(); }
		}
		
		nodeReader.close();
		try { nodeWriter.close(); } catch (IOException e) { e.printStackTrace(); }
	}
	
	private void pullNodes() {
		try {
			nodeReader = new Scanner(new File(world.getFileLoc()+"/generation/node.nodes"));
		} catch (IOException e) { e.printStackTrace(); }
		nodeReader.useDelimiter("-");
		int xi, yi, zi; 	
		float valuei;
		while(nodeReader.hasNext()) {
			xi=Integer.parseInt(nodeReader.next());
			yi=nodeReader.nextInt();
			zi=nodeReader.nextInt();
			valuei=nodeReader.nextFloat();
			nodes.put(new Vector3f(xi, yi, zi), new DensityNode(xi, yi, zi, valuei));
		}
		nodeReader.close();
	}
	
	private boolean nodeExists(int x, int y, int z) {
		return nodes.containsKey(new Vector3f(x, y, z));
	}
	
	private DensityNode getNode(int x, int y, int z) {
		return nodes.get(new Vector3f(x, y, z));
	}
}
