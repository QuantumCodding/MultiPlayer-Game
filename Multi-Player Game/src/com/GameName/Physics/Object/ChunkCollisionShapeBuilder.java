package com.GameName.Physics.Object;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import com.GameName.Cube.Cube;
import com.GameName.Engine.ResourceManager.Materials;
import com.GameName.World.Chunk;
import com.GameName.World.World;
import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;

public class ChunkCollisionShapeBuilder {
	public static RigidBody build(Chunk c) {
		ArrayList<Float> verts = new ArrayList<>();				
		
		for(int x = 0; x < c.getSize(); x ++) {
		for(int y = 0; y < c.getSize(); y ++) {
		for(int z = 0; z < c.getSize(); z ++) {
			if(!c.isAccessible()) continue;
			
			Cube cube = Cube.getCubeByID(c.getCube(x, y, z));					
			int metadata = c.getMetadata(x, y, z);			
			boolean[] visableFaces = getVisableFaces(x, y, z, c); //new boolean[] {true, true, true, true, true, true}; //
				
			if(!cube.isSolid(metadata)) continue;	
			verts.addAll(cube.getCollisionBox(metadata).getVertices(x, y, z, visableFaces));
//			verts.add(0f); verts.add(0f); verts.add(0f);
//			verts.add(1f); verts.add(0f); verts.add(0f);
//			verts.add(1f); verts.add(1f); verts.add(0f);
		}}}
	
		int vertexCount = verts.size() / 3, triangleCount = vertexCount / 3;
		
		ByteBuffer indexBuffer = BufferUtils.createByteBuffer(triangleCount * 3 * 4);
		ByteBuffer vertBuffer = BufferUtils.createByteBuffer(vertexCount * 3 * 4);

		for(int i = 0; i < triangleCount*3; i ++) {
			indexBuffer.putInt(i); 

			vertBuffer.putFloat((i*3 + 0)*4, verts.get(i*3 + 0) + c.getX()*World.CHUNK_SIZE);
			vertBuffer.putFloat((i*3 + 1)*4, verts.get(i*3 + 1) + c.getY()*World.CHUNK_SIZE);
			vertBuffer.putFloat((i*3 + 2)*4, verts.get(i*3 + 2) + c.getZ()*World.CHUNK_SIZE);
		}
		
		indexBuffer.flip();
		
		TriangleIndexVertexArray vertArray = new TriangleIndexVertexArray(
				triangleCount, indexBuffer, 3 * 4,
				vertexCount,   vertBuffer,  3 * 4
			);
		BvhTriangleMeshShape shape = new BvhTriangleMeshShape(vertArray, false);
		
		RigidBodyConstructionInfo info = new PhysicsObject.Builder()
			.setShape(shape).setPosition(c.getX(), c.getY(), c.getZ())
			.setVolume(0).setMaterial(Materials.Stone).buildInfo();
	
		return new RigidBody(info);
	}
	
	private static boolean[] getVisableFaces(int x, int y, int z, Chunk c) {
		boolean[] visableFaces = new boolean[6];		
		int metadata = c.getMetadata(x, y, z);
		
		if(!Cube.getCubeByID(c.getCube(x, y, z)).isVisable(metadata)) {
			return visableFaces;
		}
		
		Cube[] surroundingCubes = c.getSurroundingCubes(x, y, z);
			
		visableFaces[0] = surroundingCubes[2] != null ? !surroundingCubes[2].isVisable(metadata) : true; // 0 -x			1				z
		visableFaces[1] = surroundingCubes[3] != null ? !surroundingCubes[3].isVisable(metadata) : true; // 1 +z		0	C	2		-x	c	x
		visableFaces[2] = surroundingCubes[0] != null ? !surroundingCubes[0].isVisable(metadata) : true; // 2 +x			3			   -z
		visableFaces[3] = surroundingCubes[1] != null ? !surroundingCubes[1].isVisable(metadata) : true; // 3 -z					4			+y
		visableFaces[4] = surroundingCubes[5] != null ? !surroundingCubes[5].isVisable(metadata) : true; // 4 +y					C			 c
		visableFaces[5] = surroundingCubes[4] != null ? !surroundingCubes[4].isVisable(metadata) : true; // 5 -y					5			-y
				
		return visableFaces;
	}
}
