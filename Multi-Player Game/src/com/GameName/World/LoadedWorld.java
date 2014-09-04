package com.GameName.World;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import com.GameName.Util.Tag.DTGLoader;
import com.GameName.Util.Tag.Tag;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.Cube.Cube;
import com.GameName.World.Object.WorldObject;

public class LoadedWorld {
	private static String worldFilesRootDir = "";
	private static int loadRadius = 5;
	
	private File worldFiles;
	
	private Chunk[] chunks;
	private WorldObject[] objects;
	private Vector3f center;
	
	public LoadedWorld(String worldName) {
		worldFiles = new File(worldFilesRootDir + worldName);
		
		chunks = new Chunk[(int) ((4f / 3f) * Math.PI * Math.pow(loadRadius, 3))];
		objects = new WorldObject[100];
		center = new Vector3f(0, 0, 0);
	}
		
	public void loadWorld() {	
		for(int i = 0 ; i < loadRadius; i ++) {
			
			for(int x = 0; x < 360; x ++) {
			for(int y = 0; y < 360; y ++) {
			for(int z = 0; z < 360; z ++) {
				Vector3f loadPos = new Vector3f((float) Math.cos(x) * i, (float) Math.sin(y) * i, (float) Math.sin(z) * i).add(this.center);
				File loadFile = new File(worldFiles + "/chunks/" + loadPos.getX() + "x" + loadPos.getY() + "x" + loadPos.getZ() + ".dtg");
				
				try {
					HashSet<Tag> chunkData = DTGLoader.readDTGFile(loadFile);
					
					int id = 0; Vector3f pos = center.clone();
					int count = 0;
					
					for(Tag tag : chunkData) {						
						if(tag.getTagName().equals("pos")) {
							pos = (Vector3f) tag.getTagInfo();	
							count ++;
							
						} else if(tag.getTagName().equals("id")) {
							id = ((Integer) tag.getTagInfo()).intValue();
							count ++;
						}
						
						if(count == 2) {
							count = 0;
							getChunk(loadPos).setCube((int) pos.getX(), (int) pos.getY(), (int) pos.getZ(), Cube.getCubeByID(id));
						}
					}	
					
				} catch (IOException e) {
					System.err.println("Failed to load Chunk " + loadPos.toString().substring(9) + " from " + loadFile);
					e.printStackTrace();
					System.exit(1);
				}				
			}}}
		}
	}
	
	public Chunk getChunk(int x, int y, int z) {
		return chunks[x + (y * loadRadius) + (z * loadRadius * loadRadius)];
	}
	
	public Chunk getChunk(Vector3f pos) {
		return getChunk((int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
	}
	
	public void setCenter(Vector3f center) {
		this.center = center;
	}
}
