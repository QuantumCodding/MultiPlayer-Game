package com.GameName.World;

import java.io.IOException;
import java.util.HashSet;

import com.GameName.Engine.GameEngine;
import com.GameName.Util.Vectors.Vector3f;

public class ChunkLoader {
	private String fileLoc;
	
	private HashSet<Chunk> unloadedChunks;
	private HashSet<Chunk> loadedChunks;
	
	private Vector3f center;
	private int loadRadius;
	
	private World world;
	private final GameEngine ENGINE;
	
	public ChunkLoader(GameEngine eng, World world, int loadRadius) {
		ENGINE = eng;
		
		this.world = world;
		this.loadRadius = loadRadius;
		
		unloadedChunks = new HashSet<Chunk>();
		loadedChunks = new HashSet<Chunk>();
		
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
		Vector3f minPos = center.subtract(loadRadius).capMin(0);
		Vector3f maxPos = center.add(loadRadius).capMax(world.getChunkSizeAsVector()).subtract(1);
		
		for(Chunk chunk : getLoadedChunks()) {//int i = 0; i < getLoadedChunks().size(); i ++) { //Chunk chunk : getLoadedChunks()
//			Chunk chunk = getLoadedChunks().get(i);
			
			if(chunk != null && chunk.isLoaded()) {
				if( chunk.getX() < minPos.getX() || chunk.getY() < minPos.getY() || chunk.getZ() < minPos.getZ() ||
					chunk.getX() > maxPos.getX() || chunk.getY() > maxPos.getY() || chunk.getZ() > maxPos.getZ() ) {
					
					unloadedChunks.add(chunk);
					getLoadedChunks().remove(chunk);//.remove(i);
				}
			}
		}
	}
	
	public void unloadChunks() {
		for(Chunk chunk : unloadedChunks) {//int i = 0; i < unloadedChunks.size(); i ++) { // Chunk chunk : unloadedChunks
//			Chunk chunk = unloadedChunks.get(i);
			updateNeighbors(chunk, false);
			
			chunk.setIsLoaded(false);
			ENGINE.getRender().remove(chunk.getRender());
			chunk.getRender().cleanUp();
			unloadedChunks.remove(chunk);//.remove(i);
			
			chunk = null;
		}
	}
	
	public void loadChunks() {
		for(int radius = 0; radius < loadRadius; radius ++) {
			
			for(int x = -radius; x < radius + 1; x ++) {
			for(int y = -radius; y < radius + 1; y ++) {
			for(int z = -radius; z < radius + 1; z ++) {
				if(x < 0 || y < 0 || z < 0) continue;
				if(x > world.getChunkX() || y > world.getChunkY() || z > world.getChunkZ()) continue;
				
				Vector3f loadPos = new Vector3f(x, y, z).add(center).capMax(world.getChunkSizeAsVector().subtract(1)).capMin(0);
				Chunk chunk = getChunk(loadPos);
				
				if(chunk == null) {
					chunk = new Chunk(ENGINE, World.CHUNK_SIZE, world.getId(), 
							(int) loadPos.getX(), (int) loadPos.getY(), (int) loadPos.getZ()
						);
				}
				
				if(chunk.isLoaded()) continue;
				
				try {
					chunk = ChunkIO.loadChunk(chunk);
					
				} catch(IOException e) {
					System.out.println("Generateing: [" + x + ", " + y + ", " + z + "]");
					chunk = world.getEnvironmentGen().generate(World.CHUNK_SIZE, world, x, y, z, 10);
					
//					chunk = new Chunk(ENGINE, World.CHUNK_SIZE, world.getId(), x, y, z);
//					for(int x_ = 0; x_ < chunk.getSize(); x_ ++) {
//					for(int y_ = 0; y_ < chunk.getSize(); y_ ++) {
//					for(int z_ = 0; z_ < chunk.getSize(); z_ ++) {
//						if(x_ + x * chunk.getSize() == 90 || x_ + x * chunk.getSize() == 10) {
//							chunk.setCubeWithoutUpdate(x_, y_, z_, y_ == 0 || z_ == 0 ? Cubes.ColorfulTestCube : Cubes.StoneCube);
//						}
//						
//						if(y_ + y * chunk.getSize() == 90 || y_ + y * chunk.getSize() == 10) {
//							chunk.setCubeWithoutUpdate(x_, y_, z_, x_ == 0 || z_ == 0 ? Cubes.ColorfulTestCube : Cubes.StoneCube);
//						}
//						
//						if(z_ + z * chunk.getSize() == 90 || z_ + z * chunk.getSize() == 10) {
//							chunk.setCubeWithoutUpdate(x_, y_, z_, y_ == 0 || x_ == 0 ? Cubes.ColorfulTestCube : Cubes.StoneCube);
//						}
//					}}}
				}
				
				chunk.setIsLoaded(true);
				getLoadedChunks().add(chunk); 
				updateNeighbors(chunk, true);
			}}}			
		}
	}
	
	private void updateNeighbors(Chunk current, boolean state) {
		Vector3f min = center.subtract(loadRadius).capMin(0);
		Vector3f max = center.add(loadRadius).capMax(world.getChunkSizeAsVector()).subtract(1);
		Vector3f pos = current.getPos().clone(); 
				
		if(pos.lessThen(min) || pos.greaterThen(max)) return;
		
		updateNeighbor(pos.subtract(1, 0, 0), current, min, max, false, 2, state);
		updateNeighbor(pos.subtract(0, 1, 0), current, min, max, false, 4, state);
		updateNeighbor(pos.subtract(0, 0, 1), current, min, max, false, 1, state);
		
		updateNeighbor(pos.add(1, 0, 0), current, min, max, true, 0, state);
		updateNeighbor(pos.add(0, 1, 0), current, min, max, true, 5, state);
		updateNeighbor(pos.add(0, 0, 1), current, min, max, true, 3, state);
	}
	
	public void updateNeighbor(Vector3f pos, Chunk current, Vector3f min, Vector3f max, boolean greater, int side, boolean state) {
		int oppSide = side == 4 ? 5 : side == 5 ? 4 : (side + 2) % 4;		
		if((pos.lessThenOrEqual(max) && greater) || (pos.greaterThenOrEqual(min) && !greater)) {
			Chunk chunk = getChunk(pos);
//			System.out.println("+++++++++++++++++++++ " + (chunk!=null?chunk:"null") + " " + (chunk!=null?chunk.isInitialized():false) + "   " + current.getPos());
			
			if(chunk != null && chunk.isInitialized()){
				if(chunk.isLoaded() && current.isLoaded()) {
//					System.out.println("------BackTrack------ Chunk " + current.getPos().valuesToString() + " Side: " + oppSide + " State: " + state);
					current.loadNeighbor(oppSide, state);
				}
				
				if(!chunk.isAccessible() && chunk.isLoaded()) {
//					System.out.println("-------Neighbor------ Chunk " + chunk.getPos().valuesToString() + " Side: " + side + " State: " + state);
					chunk.loadNeighbor(side, state);
					if(chunk.isAccessible()) registerChunk(chunk);
				}
			}
		} else { 
//			System.out.println("--------Edge--------- Chunk " + current.getPos().valuesToString() + " Side: " + oppSide + " State: " + state);
			current.loadNeighbor(oppSide, state);
		}
	}
	
	private void registerChunk(Chunk chunk) {
		chunk.handelMassUpdate();
		chunk.updateTextureMap();
		
		ENGINE.add(chunk.getRender()); 
		chunk.forceVBOUpdate();
	}
	
	public void saveChunks() {
		for(Chunk chunk : getLoadedChunks()) {
			chunk.save(fileLoc);
		}
	}
	
	public boolean checkChunksVBO() {
		for(Chunk chunk : getLoadedChunks()) {
			if(chunk != null && chunk.isAccessible() && chunk.getRender().isVboUpdateNeeded()) {
				return true;
			}	
		}	
		
		return false;
	}

	public void updataChunksVBO() {
		for(Chunk chunk : getLoadedChunks()) {//int i = 0; i < getLoadedChunks().size(); i ++) { //Chunk chunk : getLoadedChunks()
//			Chunk chunk = getLoadedChunks().get(i);
			
			if(chunk != null && chunk.isAccessible() && chunk.getRender().isVboUpdateNeeded()) {
				chunk.getRender().updateVBOs();
			}	
		}	
	}
	
	public void forceChunkUpdate() {
		for(Chunk chunk : getLoadedChunks()) {//int i = 0; i < getLoadedChunks().size(); i ++) { //Chunk chunk : getLoadedChunks()
//			Chunk chunk = getLoadedChunks().get(i);
			
			if(chunk != null && chunk.isAccessible()) {
				chunk.getRender().updateVBOs();
			}	
		}	
	}
		
	public Chunk getChunk(float x, float y, float z) {
		return getChunk(new Vector3f(x, y, z));}
	public Chunk getChunk(Vector3f pos) {		
		for(Chunk chunk : getLoadedChunks()) {//int i = 0; i < getLoadedChunks().size(); i ++) { //Chunk chunk : getLoadedChunks()
//			Chunk chunk = getLoadedChunks().get(i);
			
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

	public synchronized HashSet<Chunk> getLoadedChunks() {
		return loadedChunks;
	}
}



//if(sub.x >= min.x) {
//	chunk = getChunk(sub.x, pos.y, pos.z);
//	if(chunk != null && chunk.isInitialized()){
//		if(chunk.isLoaded()) 
//			current.loadNeighbors(0, state);
//		if(!chunk.isAccessible()) {
//			chunk.loadNeighbors(2, state);
//			if(chunk.isAccessible()) registerChunk(chunk);
//		}
//}} else { current.loadNeighbors(0, state); }
//
//if(sub.y >= min.y) {
//	chunk = getChunk(pos.x, sub.y, pos.z);
//	if(chunk != null && chunk.isInitialized()){
//		if(chunk.isLoaded()) 
//			current.loadNeighbors(5, state);
//		if(!chunk.isAccessible()) {
//			chunk.loadNeighbors(4, state);
//			if(chunk.isAccessible()) registerChunk(chunk);
//		}
//}} else { current.loadNeighbors(5, state); }	
//
//if(sub.z >= min.z) {
//	chunk = getChunk(pos.x, pos.y, sub.z);
//	if(chunk != null && chunk.isInitialized()){
//		if(chunk.isLoaded()) 
//			current.loadNeighbors(3, state);
//		if(!chunk.isAccessible()) {
//			chunk.loadNeighbors(1, state);
//			if(chunk.isAccessible()) registerChunk(chunk);
//		}
//}} else { current.loadNeighbors(3, state); }	
//
//if(add.x >= max.x) {
//	chunk = getChunk(add.x, pos.y, pos.z);
//	if(chunk != null && chunk.isInitialized()){
//		if(chunk.isLoaded()) 
//			current.loadNeighbors(2, state);
//		if(!chunk.isAccessible()) {
//			chunk.loadNeighbors(0, state);
//			if(chunk.isAccessible()) registerChunk(chunk);
//		}
//}} else { current.loadNeighbors(2, state); }	
//
//if(add.y >= max.y) {
//	chunk = getChunk(pos.x, add.y, pos.z);
//	if(chunk != null && chunk.isInitialized()){
//		if(chunk.isLoaded()) 
//			current.loadNeighbors(4, state);
//		if(!chunk.isAccessible()) {
//			chunk.loadNeighbors(5, state);
//			if(chunk.isAccessible()) registerChunk(chunk);
//		}
//}} else { current.loadNeighbors(4, state); }
//
//if(add.z >= max.z) {
//	chunk = getChunk(pos.x, pos.y, add.z);
//	if(chunk != null && chunk.isInitialized()){
//		if(chunk.isLoaded()) 
//			current.loadNeighbors(1, state);
//		if(!chunk.isAccessible()) {
//			chunk.loadNeighbors(3, state);
//			if(chunk.isAccessible()) registerChunk(chunk);
//		}
//}} else { current.loadNeighbors(1, state); }