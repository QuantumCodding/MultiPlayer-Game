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
	private static String worldFilesRootDir = "res/worlds/";
	protected static int loadRadius = 5;
	
	private LoadedWorldAccess access;
	protected World world;
	
	private File worldFiles;
	
	protected Chunk[] chunks;
	protected WorldObject[] objects;
	protected Vector3f center;
	
	public LoadedWorld(World world, String worldName) {
		this.world = world;
		worldFiles = new File(worldFilesRootDir + worldName);
		
		chunks = new Chunk[(int) ((4f / 3f) * Math.PI * Math.pow(loadRadius, 3))];
		objects = new WorldObject[100];
		center = new Vector3f(0, 0, 0);
		
		access = new LoadedWorldAccess(this);
	}
		
	public void loadWorld() {		
		float f1, f2, f3, f4;		
		
		for(int rotY = 0; rotY < 360; rotY ++) {
		for(int rotX = 0; rotX < 360; rotX ++) {
			
		for(int radius = 0; radius < loadRadius; radius ++) {			
			
			f1 = (float)  Math.cos(Math.toRadians(rotY));
			f2 = (float)  Math.sin(Math.toRadians(rotY));
			f3 = (float)  Math.cos(Math.toRadians(rotX));                  
			f4 = (float)  Math.sin(Math.toRadians(rotX));             
			
			Vector3f loadPos = new Vector3f(
					(f2 * f3 * radius), 
					(f4 * radius), 
					(f1 * f3 * radius))
				.add(this.center).round();
			
			if(loadPos.getX() < 0 || loadPos.getY() < 0 || loadPos.getZ() < 0 ||
					loadPos.getX() > world.getChunkX() || loadPos.getY() > world.getChunkY() || 
					loadPos.getZ() > world.getChunkZ()) 
			{				
				continue;
			}
			
			File loadFile = new File(worldFiles + "/chunks/" + loadPos.getX() + "x" + loadPos.getY() + "x" + loadPos.getZ() + ".dtg");
			
			Chunk chunk = access.getChunk(loadPos);
			if(chunk == null) {
				chunk = new Chunk(World.CHUNK_SIZE, this.world.getId(),
					(int) loadPos.getX(), (int) loadPos.getY(), (int) loadPos.getZ());
			}
			
			try {
				HashSet<Tag> chunkData = DTGLoader.readDTGFile(loadFile);
				
				int id = 0; Vector3f pos = center.clone();
				int count = 0;
				
				for(Tag tag : chunkData) {						
					if(tag.getTagName().equals("pos")) {
						pos = (Vector3f) tag.getTagInfo();	
						count ++;
						
					} else if(tag.getTagName().equals("cubeId")) {
						id = ((Integer) tag.getTagInfo()).intValue();
						count ++;
					}
					
					if(count == 2) {
						count = 0;
						
						chunk.setCube(
								(int) pos.getX(), (int) pos.getY(), (int) pos.getZ(), Cube.getCubeByID(id)
							);
					}
				}	
				
			} catch (IOException e) {
				System.err.println("Failed to load Chunk " + loadPos.toString().substring(9) + " from " + loadFile);
				e.printStackTrace();
				System.exit(1);
			}			
		}}}
	}
	
	public void saveWorld() {
		for(Chunk chunk : chunks) {
			File saveFile = new File(worldFiles + "/chunks/" + chunk.getX() + "x" + chunk.getY() + "x" + chunk.getZ() + ".dtg");
			
			try {
				DTGLoader.saveDTGFile(saveFile, chunk.getTagLines());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public boolean checkChunks() {
		for(Chunk chunk : chunks) {
			if(chunk != null && chunk.isVboUpdataRequested()) {
				return true;
			}	
		}	
		
		return false;
	}

	public void updataChunks() {
		for(Chunk chunk : chunks) {
			if(chunk != null && chunk.isVboUpdataRequested()) {
				chunk.updataVBO();
			}	
		}	
	}
	
	public LoadedWorldAccess getAccess() {
		return access;
	}
	
	public void cleanUp() {
//		saveWorld(); //TODO: Determine if this is needed
		
		for(Chunk chunk : chunks) {
			chunk.cleanUp();
		}
	}
}
