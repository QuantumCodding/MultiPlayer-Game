package com.GameName.Physics;

import com.GameName.Cube.Cube;
import com.GameName.Engine.ResourceManager.Materials;
import com.GameName.Physics.Collision.CollisionShape;
import com.GameName.Util.Side;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.LoadedWorldAccess;
import com.GameName.World.RayTrace;
import com.GameName.World.World;

public abstract class PhysicsObject {
	private MotionState state;
	private boolean noClip;
	
	private Vector3f position, rotation;
	private Vector3f velocity, angularVelocity;
	private Vector3f force, torque;
	
	private CollisionShape collisionShape;
	private Material material; 
	private float volume, mass;
	
	private World currentWorld;
	private Material median, surface;

	public PhysicsObject(CollisionShape collisionShape, Material material) {
		this.collisionShape = collisionShape;
		this.material = material;
		
		volume = collisionShape.getVolume();
		mass = volume * material.getDensity();
		
		median = Materials.Air;
		surface = Materials.Stone;
		
		resetVectors();
	}
	
	public void reset() {
		noClip = true;
		this.clearForce();
	}
	
	public void intergrate(float delta) {
		//Gravity
		if(!(noClip || state == MotionState.Flying || state == MotionState.OnGround)) {
			force = force.add(new Vector3f(0, -10, 0));//PhysicsEngine.DEFAULT_GRAVITY);
		}
		
		//Apply Force
		velocity = velocity.add(force.divide(mass));
		
		//World Collision
		if(!noClip) {
			if(state == MotionState.OnGround && velocity.getY() < 0) 
				velocity.y = 0;
			
			RayTrace trace = RayTrace.preformTrace(position, position.add(velocity.multiply(delta)), true, false, currentWorld);
			if(trace.traceSuccessful()) {
				position = trace.getEndPosition();
				System.out.println(trace.getEndPosition());
			}
		}
		
		//Apply Velocity
		position = position.add(velocity.multiply(delta));
		rotation = rotation.add(torque.multiply(1));
		collisionShape.translate(velocity.multiply(delta));
		
		//World Interaction
		if(!noClip) {	
			//Cap Position
			LoadedWorldAccess access = currentWorld.getLoadedWorld().getAccess();
			Vector3f max = access.getCenter().add(LoadedWorldAccess.getLoadRadius()).capMax(currentWorld.getChunkSizeAsVector());
			Vector3f min = access.getCenter().subtract(LoadedWorldAccess.getLoadRadius()).capMin(0);
			position = position.capMax(max.multiply(World.CHUNK_SIZE).subtract(1)).capMin(min.multiply(World.CHUNK_SIZE).add(1));
			
			//World info
			Vector3f length = new Vector3f(2, 3, 2);//collisionShape.getAABB().getLength();
			median = currentWorld.getMaterial(position);
			
			Cube ground = currentWorld.getCube(position.subtract(0, (float) length.getY() / 2f, 0));
			int metadata = currentWorld.getCubeMetadata(position.subtract(0, (float) length.getY() / 2f, 0));
			surface = ground.getMaterial(metadata);
			
			if(ground.isSolid(metadata))
				state = MotionState.OnGround;
			else if(ground.isLiquid(metadata))
				state = MotionState.Floating;
			else if(state != MotionState.Flying)
				state = MotionState.Falling;
		}
		
		clip:
		//Velocity Decrees
		if(!noClip) {
			
			//Flying 
			if(state == MotionState.Flying) {
				velocity = velocity.add(new Vector3f(0.5f, 0.5f, 0.5f).capMax(velocity.abs()).multiply(getOppDir(velocity)));
				break clip;
			}
			
			//Drag
			float dragFactor = 0.5f * median.getDensity(); 
			if(dragFactor < 1) {				
				velocity = velocity.add( new Vector3f(dragFactor, dragFactor, dragFactor
//						collisionShape.getSurfaceArea(velocity.x > 0 ? Side.RightFace : Side.LeftFace) *
//								dragFactor * (float) Math.pow(velocity.getX(), 2),
//						collisionShape.getSurfaceArea(velocity.y > 0 ? Side.TopFace : Side.BottomFace) * 
//								dragFactor * (float) Math.pow(velocity.getY(), 2),
//						collisionShape.getSurfaceArea(velocity.z > 0 ? Side.FrontFace : Side.BackFace) * 
//								dragFactor * (float) Math.pow(velocity.getZ(), 2)
					).capMax(velocity.abs()).multiply(getOppDir(velocity)));
			}
			
			//"Friction"
			if(state == MotionState.OnGround) {
				float friction = surface.getFrictionalCoefficient(); //0.25f * Math.abs(PhysicsEngine.DEFAULT_GRAVITY.y); //surface.getFrictionalCoefficient()
				
				velocity = velocity.add(new Vector3f(
						friction, 0, friction).capMax(velocity.abs())
					.multiply(getOppDir(velocity)));
			}
		}
		
		if(noClip) 
			stopMotion();
		else
			clearForce();
	}
	
	public void handelCollision() {
//		TODO: Handle Collisions
	}
	
	private Vector3f getOppDir(Vector3f dir) {
		return dir.direction().multiply(-1);
	}

	public MotionState getState() { return state; }
	public boolean noClipEnabled() { return noClip; }
	
	public Vector3f getPosition() {	return position; }
	public Vector3f getRotation() {	return rotation; }
		
	public Vector3f getVelocity() { return velocity; }
	public Vector3f getAngularVelocity() { return angularVelocity; }
	
	public Vector3f getForce() { return force; }
	public Vector3f getTorque() { return torque; }

	public CollisionShape getCollisionShape() { return collisionShape; }
	public Material getMaterial() {	return material; }
	public float getVolume() { return volume; }
	public float getMass() { return mass; }

	public World getCurrentWorld() { return currentWorld; }
	public Material getMedian() { return median; }
	public Material getSurface() { return surface; }
	
	public void applyForce(Vector3f force) { this.force = this.force.add(force);}
	public void applyTorque(Vector3f torque) { this.torque = this.torque.add(torque); }
	
	public void setPosition(Vector3f position) { this.position = position; };
	public void setRotation(Vector3f rotation) { this.rotation = rotation; };
	
	public void setCurrentWorld(World currentWorld) { this.currentWorld = currentWorld; }
	
	public void setNoClip(boolean noClip) { 
		if(this.noClip != noClip) toggleNoClip(); }
	public void toggleNoClip() {
		noClip = !noClip;
	}
	
	public void setFlying(boolean flying) { 
		if(flying != (state == MotionState.Flying)) toggleFlying(); }
	public void toggleFlying() {
		if(noClip) return;
		
		if(state == MotionState.Flying) 
			state = MotionState.Falling;			
		else
			state = MotionState.Flying;	
		
	}
	
	public enum MotionState {
		OnGround, Falling, Flying, Floating
	}
	
	public void resetVectors() {
		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);

		velocity = new Vector3f(0, 0, 0);
		angularVelocity = new Vector3f(0, 0, 0);
		
		force = new Vector3f(0, 0, 0);
		torque = new Vector3f(0, 0, 0);
	}
	
	public void clearForce() {		
		force = new Vector3f(0, 0, 0);
		torque = new Vector3f(0, 0, 0);
	}
	
	public void stopMotion() {
		clearForce();
		
		velocity = new Vector3f(0, 0, 0);
		angularVelocity = new Vector3f(0, 0, 0);
	}
	
//	Vector3f addPos, subPos;
//	
//	Cube cubePos = currentWorld.getCube(addPos = position.add((float) length.getX() / 2f, 0, 0));
//	Cube cubeNeg = currentWorld.getCube(subPos = position.add((float) -length.getX() / 2f, 0, 0));
//	
//	if(velocity.getX() > 0 && (cubePos == null || cubePos.isSolid(currentWorld.getCubeMetadata(addPos)))) 
//		velocity.setX(0);				
//	else if(velocity.getX() < 0 && (cubeNeg == null || cubeNeg.isSolid(currentWorld.getCubeMetadata(subPos)))) 
//		velocity.setX(0);
//
//	cubePos = currentWorld.getCube(addPos = position.add(0, (float) length.getY() / 2f, 0));
//	cubeNeg = currentWorld.getCube(subPos = position.add(0, (float) -length.getY() / 2f, 0));
//	
//	if(velocity.getY() > 0 && (cubePos == null || cubePos.isSolid(currentWorld.getCubeMetadata(addPos))))
//		velocity.setY(0);				
//	else if(velocity.getY() < 0 && (cubeNeg == null || cubeNeg.isSolid(currentWorld.getCubeMetadata(subPos))))
//		velocity.setY(0); 
//
//	cubePos = currentWorld.getCube(addPos = position.add(0, 0, (float) length.getZ() / 2f));
//	cubeNeg = currentWorld.getCube(subPos = position.add(0, 0, (float) -length.getZ() / 2f));
//	
//	if(velocity.getZ() > 0 && (cubePos == null || cubePos.isSolid(currentWorld.getCubeMetadata(addPos)))) 
//		velocity.setZ(0);				
//	else if(velocity.getZ() < 0 && (cubeNeg == null || cubeNeg.isSolid(currentWorld.getCubeMetadata(subPos))))
//		velocity.setZ(0);
}
