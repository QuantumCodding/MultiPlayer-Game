package com.GameName.World.Object;

import com.GameName.Physics.Coalition.BoundingBox;
import com.GameName.Physics.Object.PhysicsObject;
import com.GameName.Util.Vector3f;
import com.GameName.World.World;

public abstract class WorldObject extends PhysicsObject {

	protected Vector3f renderPos, adjust;	
	protected float height, width, length;
	protected World currentWorld;
	
	WorldObjectAccess access;
	
	public WorldObject() {
		super();
		
		height = 1;	width = 1; length = 1;		
		renderPos = new Vector3f(0, 0, 0);
	}
	
	public void updata() {
		super.updata();
		
		renderPos = pos.multiply(adjust);
	}
	
	public void addBounding() {
		bounding.add(new BoundingBox(
				new Vector3f(pos.getX() - (width / 2f), pos.getY() - (height / 2f), pos.getZ() - (length / 2f)), 
				new Vector3f(pos.getX() + (width / 2f), pos.getY() + (height / 2f), pos.getZ() + (length / 2f))
			));
	}

	public WorldObjectAccess getAccess() {
		return access;
	}
}
