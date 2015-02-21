package com.GameName.World;

import java.util.ArrayList;

import com.GameName.Cube.Cube;
import com.GameName.Util.Vectors.Vector3f;

public class RayTrace {
	private Vector3f[] results;
	private World world;
	
	private Vector3f startPos;
	private Vector3f endPos;
	
	private RayTrace(Vector3f[] results, World world, Vector3f startPos, Vector3f endPos) {
		this.results = results;
		this.world = world;
		this.startPos = startPos;
		this.endPos = endPos;
	}
	
	public static RayTrace preformTrace(Vector3f start, float reach, boolean collideOnce, boolean collideLiquids, World world) {
		return preformTrace(start, start.add(start.direction().multiply(reach)), collideOnce, collideLiquids, world);
	}
	
	public static RayTrace preformTrace(Vector3f start, Vector3f end, boolean collideOnce, boolean collideLiquids, World world) {
		Vector3f pos, step = start.subtract(end).direction(); 
		ArrayList<Vector3f> resluts = new ArrayList<>();
		
		Vector3f max = world.getLoadedWorld().getAccess().getCenter().add(LoadedWorldAccess.getLoadRadius())
				.capMax(world.getChunkSizeAsVector()).multiply(World.CHUNK_SIZE).subtract(1);
		Vector3f min = world.getLoadedWorld().getAccess().getCenter().subtract(LoadedWorldAccess.getLoadRadius())
				.capMin(0).multiply(World.CHUNK_SIZE);
		
		int i = -1;
		while((pos = start.add(step.multiply(++ i))).abs().lessThen(end.abs())) {
			if(pos.greaterThen(max) || pos.lessThen(min)) {
				resluts.add(pos);
				
				if(collideOnce) {
					return new RayTrace((Vector3f[]) resluts.toArray(new Vector3f[resluts.size()]), world, start, pos);
				}
			}
			
			Cube cube = world.getCube(pos);
			int metadata = world.getCubeMetadata(pos);
			
			if((cube.isLiquid(metadata) && collideLiquids) || cube.isSolid(metadata)) {
				resluts.add(pos);
				
				if(collideOnce) {
					return new RayTrace((Vector3f[]) resluts.toArray(new Vector3f[resluts.size()]), world, start, pos);
				}
			}
		}
		
		return new RayTrace((Vector3f[]) resluts.toArray(new Vector3f[resluts.size()]), world, start, end);
	}
	
	public Vector3f[] getResults() { return results; }
	public World getWorld() { return world; }
	
	public Vector3f getStartPosition() { return startPos; }
	public Vector3f getEndPosition() { return endPos; }
	
	public boolean traceSuccessful() { return results.length != 0; };
	public Vector3f getFirstIntersection() {
		if(traceSuccessful())
			return results[0];
		else
			return null;
	}
}
