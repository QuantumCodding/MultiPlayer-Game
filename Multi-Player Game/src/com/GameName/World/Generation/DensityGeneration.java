package com.GameName.World.Generation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import com.GameName.Cube.Cube;
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
	private File file;
	
	public DensityGeneration(GameEngine eng, int seedI, int scaleI, World worldI) {
		seed = seedI;
		scale = scaleI;
		world = worldI;
		r = new Random(seed);
		ENGINE = eng;
		file = new File(world.getFileLoc()+"/generation/node.nodes");
		try {
			if(file.createNewFile()) {
				System.out.println("Node file created.");
			}
			else {
				System.out.println("Pre-existing node file found.");
			}
		} catch (IOException e) { System.out.println("FUCK."); }

		pullNodes();
	}
	
	public Chunk generate(int scale,int x,int y,int z) {
		Chunk out = new Chunk(ENGINE, scale, world.getId(), x, y, z);
		ArrayList<DensityNode> nearNodes = new ArrayList<>();
		for(int i=x; i<x+2; i++) {
			for(int j=y; j<y+2; j++) {
				for(int k=z; k<z+2; k++) {
					if(!nodeExists(i, j, k)) {
						nodes.put(new Vector3f(i, j, k), new DensityNode(i, j, k, r.nextFloat()));
						addToNodeFile(getNode(i, j, k));
						System.out.println("Node added at ["+i+","+j+","+k+"]");
					}
					nearNodes.add(getNode(i, j, k));
				}
			}
		}
		
		for(int i=0; i<nearNodes.size(); i++) {
			System.out.println("Node "+i+" at ["+nearNodes.get(i).getX()+","+nearNodes.get(i).getY()+","+nearNodes.get(i).getZ()+"]");
		}
		
		for(int i=0; i<World.CHUNK_SIZE; i++) {
			for(int j=0; j<World.CHUNK_SIZE; j++) {
				for(int k=0; k<World.CHUNK_SIZE; k++) {					
					float val1=Math.abs(((nearNodes.get(0).getValue()*k)+(nearNodes.get(1).getValue()*(10f-k)))/10),
					val2=Math.abs(((nearNodes.get(2).getValue()*k)+(nearNodes.get(3).getValue()*(10f-k)))/10),
					val3=Math.abs(((nearNodes.get(4).getValue()*k)+(nearNodes.get(5).getValue()*(10f-k)))/10),
					val4=Math.abs(((nearNodes.get(6).getValue()*k)+(nearNodes.get(7).getValue()*(10f-k)))/10);
					
					float val5=Math.abs(((val1*j)+(val2*(10f-j)))/10),
					val6=Math.abs(((val3*j)+(val4*(10f-j)))/10);
					
					float value=Math.abs(((val5*i)+(val6*(10f-i)))/10);
					int block = r.nextInt(4)+1;
//					System.out.println("Sum:" + value);
					if(value >= .5f)
						out.setCubeWithoutUpdate(i, j, k, Cube.getCubeByID(block));
					else
						out.setCubeWithoutUpdate(i, j, k, Cubes.Air);
				}
			}
		}
		
		
		
		
		
		return out;
	}
			
			
	private void addToNodeFile(DensityNode n) {
		try {
			nodeReader = new Scanner(new File(world.getFileLoc()+"/generation/node.nodes"));
		} catch (IOException e) { e.printStackTrace(); }
		ArrayList<DensityNode> nodz = new ArrayList<DensityNode>();
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
		nodeReader.close();
		try {
			nodeWriter = new BufferedWriter(new FileWriter(new File(world.getFileLoc()+"/generation/node.nodes")));
		} catch (IOException e) { e.printStackTrace(); }
		for(DensityNode no : nodz) {
			try { nodeWriter.write(no.getX()+" "+no.getY()+" "+no.getZ()+" "+no.getValue()+" "); nodeWriter.newLine(); } catch (IOException e) { e.printStackTrace(); }
		}
		try { nodeWriter.close(); } catch (IOException e) { e.printStackTrace(); }
	}
	
	private void pullNodes() {
		try {
			nodeReader = new Scanner(new File(world.getFileLoc()+"/generation/node.nodes"));
		} catch (IOException e) { System.out.println("Nope."); }
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
