package com.GameName.World;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import com.GameName.Cube.Cube;
import com.GameName.Main.GameName;
import com.GameName.Main.Debugging.Logger;
import com.GameName.Render.Effects.TextureRegistry;
import com.GameName.Util.Tag.DTGLoader;
import com.GameName.Util.Tag.TagGroup;
import com.GameName.Util.Vectors.Vector3f;

public class ChunkLoader {
	private String fileLoc;
	
	private ArrayList<Chunk> unloadedChunks;
	private ArrayList<Chunk> loadedChunks;
	
	private Vector3f center;
	private int loadRadius;
	
	private World world;
	
	public ChunkLoader(World world, int loadRadius) {
		this.world = world;
		this.loadRadius = loadRadius;
		
		unloadedChunks = new ArrayList<Chunk>();
		loadedChunks = new ArrayList<Chunk>();
		
		center = new Vector3f(0, 0, 0);
		fileLoc = world.getFileLoc() + "/chunks/";
	}
	
	public void update() {
		if(center == null) center = new Vector3f(0, 0, 0);
		
		findUnloadedChunks();
		unloadChunks();
		loadChunks();
	}
	
	public void findUnloadedChunks() {
		Vector3f minPos = center.subtract(loadRadius);
		Vector3f maxPos = center.add(loadRadius);
		
		for(int i = 0; i < getLoadedChunks().size(); i ++) { //Chunk chunk : getLoadedChunks()
			Chunk chunk = getLoadedChunks().get(i);
			
			if(chunk != null && chunk.isLoaded()) {
				if( chunk.getX() < minPos.getX() || chunk.getY() < minPos.getY() || chunk.getZ() < minPos.getZ() ||
					chunk.getX() > maxPos.getX() || chunk.getY() > maxPos.getY() || chunk.getZ() > maxPos.getZ() ) {
					
					unloadedChunks.add(chunk);
					getLoadedChunks().remove(i);
				}
			}
		}
	}
	
	public void unloadChunks() {
		for (int i = 0; i < unloadedChunks.size(); i ++) { // Chunk chunk : unloadedChunks
			Chunk chunk = unloadedChunks.get(i);
			
			chunk.setIsLoaded(false);
			GameName.render.remove(chunk.getRender());
			chunk.getRender().cleanUp();
			unloadedChunks.remove(i);
			
			chunk = null;
		}
	}
	
	public void loadChunks() {
//		System.out.println("Loading Chunks");
		Logger.println("Chunk Loader is loding chunks");
				
		for(int radius = 0; radius < loadRadius; radius ++) {
			
			for(int x = -radius; x < loadRadius; x ++) {
			for(int y = -radius; y < loadRadius; y ++) {
			for(int z = -radius; z < loadRadius; z ++) {
				
				Vector3f loadPos = new Vector3f(x, y, z).add(center).capMax(world.getChunkSizeAsVector().subtract(1)).capMin(0);
				Chunk chunk = getChunk(loadPos);
				
				if(chunk == null) {
					chunk = new Chunk(World.CHUNK_SIZE, world.getId(), 
							(int) loadPos.getX(), (int) loadPos.getY(), (int) loadPos.getZ()
						);
					
					getLoadedChunks().add(chunk);
				}
				
				if(chunk.isLoaded()) {continue;}
				
				File loadFile = new File(fileLoc +
						(int) (loadPos.getX()) + " x " + (int) (loadPos.getY()) + " x " + (int) (loadPos.getZ())
					+ ".dtg");
				
				try {
					HashSet<TagGroup> tags = DTGLoader.readDTGFile(loadFile);
					Vector3f pos; int id;
					
					for(TagGroup group : tags) {
						if(!(((String) group.getIdTag().getTagInfo()).equals("cube"))) continue;
						
						pos = (Vector3f) group.getTagByName("pos").getTagInfo();					
						id = ((Integer) group.getTagByName("cubeId").getTagInfo()).intValue();
						
						chunk.setCube(
							(int) pos.getX(), (int) pos.getY(), (int) pos.getZ(), Cube.getCubeByID(id)
						);
					}
					
					chunk.setIsLoaded(true);
					chunk.getRender().setTexture(Cube.getTextureSheet());
					
					GameName.render.add(chunk.getRender());
				} catch(IOException e) {
					Logger.print("Failed to load Chunk " + loadPos.valuesToString() + " from World " + world.getName()).setType("ERROR").end();
					e.printStackTrace();
				}				
			}}}			
		}
	}
	
	public void saveChunks() {
		for(Chunk chunk : getLoadedChunks()) {
			try {
				File saveLoc =  new File(fileLoc +
						(int) (chunk.getX()) + " x " + (int) (chunk.getY()) + " x " + (int) (chunk.getZ())
					+ ".dtg");
				
				DTGLoader.saveDTGFile(saveLoc, chunk.getTagGroup());
				
			} catch(IOException e) {
				System.err.println("Failed to save Chunk " + chunk.getPos().valuesToString() + " im World " + world.getName());
				e.printStackTrace();
			}
		}
	}
	
	public boolean checkChunksVBO() {
		for(Chunk chunk : getLoadedChunks()) {
			if(chunk != null && chunk.getRender().isVboUpdateNeeded()) {
				return true;
			}	
		}	
		
		return false;
	}

	public void updataChunksVBO() {
		for(int i = 0; i < getLoadedChunks().size(); i ++) { //Chunk chunk : getLoadedChunks()
			Chunk chunk = getLoadedChunks().get(i);
			
			if(chunk != null && chunk.getRender().isVboUpdateNeeded()) {
				chunk.getRender().updateVBOs();
			}	
		}	
	}
	
	public void forceChunkUpdate() {
		for(int i = 0; i < getLoadedChunks().size(); i ++) { //Chunk chunk : getLoadedChunks()
			Chunk chunk = getLoadedChunks().get(i);
			
			if(chunk != null) {
				chunk.getRender().updateVBOs();
			}	
		}	
	}
		
	public Chunk getChunk(Vector3f pos) {		
		for(int i = 0; i < getLoadedChunks().size(); i ++) { //Chunk chunk : getLoadedChunks()
			Chunk chunk = getLoadedChunks().get(i);
			
			if(chunk != null && chunk.getPos().equals(pos)) {
				return chunk;
			}
		}
		
		return null;
	}

	public Vector3f getCenter() {
		return center;
	}

	public int getLoadRadius() {
		return loadRadius;
	}

	public void setCenter(Vector3f center) {
		this.center = center;
	}

	public void setLoadRadius(int loadRadius) {
		this.loadRadius = loadRadius;
	}
	
	public void cleanUp() {
		for(Chunk chunk : getLoadedChunks()) {
			if(chunk != null) {
				chunk.getRender().cleanUp();
			}
		}
	}

	public synchronized ArrayList<Chunk> getLoadedChunks() {
		return loadedChunks;
	}
}
