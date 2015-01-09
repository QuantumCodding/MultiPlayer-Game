package com.GameName.World.Object;

import com.GameName.Engine.GameEngine;
import com.GameName.Physics.Object.PhysicsAccess;
import com.GameName.Physics.Object.PhysicsObject;
import com.GameName.Util.Vectors.MathVec3f;
import com.GameName.World.World;

public class WorldObjectAccess extends PhysicsAccess {
	WorldObject wObject;
	
	public WorldObjectAccess(GameEngine eng, PhysicsObject object) {
		super(eng, object);
		
		wObject = (WorldObject) object;
	}
	
	public float getHeight() {
		return wObject.height;
	}

	public float getWidth() {
		return wObject.width;
	}

	public float getLength() {
		return wObject.length;
	}
	
	public World getCurrentWorld() {
		return wObject.currentWorld;
	}
	
	public MathVec3f getChunk() {
		return wObject.chunk;
	}
	
	public MathVec3f getRenderPos() {
		return wObject.renderPos;
	}

	public MathVec3f getAdjust() {
		return wObject.adjust;
	}

	public void setHeight(float height) {
		wObject.height = height;
	}

	public void setWidth(float width) {
		wObject.width = width;
	}

	public void setLength(float length) {
		wObject.length = length;
	}
	
	public void setCurrentWorld(World currentWorld) {
		wObject.currentWorld = currentWorld;
		
		wObject.adjust = new MathVec3f(
				(World.SCALE / 10f),
				(World.SCALE / 10f),
				(World.SCALE / 10f)
			);
	}
}
