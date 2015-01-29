package com.GameName.Physics.Object;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.GameName.Physics.PhysicsWorld;
import com.GameName.Util.Vectors.MathVec3f;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

public class PhysicsObject extends RigidBody {
	private boolean noClip, lastClipState;
	private CollisionShape oldShape;
	
	public PhysicsObject(RigidBodyConstructionInfo constructionInfo) {
		super(constructionInfo);
		noClip = true;
	}

	public void reset() {
		super.clearForces();
		System.out.println("Physics Reset");
	}
	
	public void update() {
		if(lastClipState != noClip) {
			if(lastClipState) {
				setGravity(PhysicsWorld.GRAVITY);
				setCollisionShape(oldShape);
			} else {
				setGravity(new Vector3f(0, 0, 0));
				oldShape = getCollisionShape();
				setCollisionShape(new BoxShape(new Vector3f(0, 0, 0)));
			}
		}
		
		if(noClip) {
			setDamping(0.9f, 0.0f);
			setGravity(new Vector3f(0, 0, 0));
		}
		
		lastClipState = noClip;
	}
	
	public boolean isNoClip() { return noClip; }
	public void setNoClip(boolean noClip) { this.noClip = noClip; }
	
	public MathVec3f getPos() {
		Transform transform = new Transform();
        getMotionState().getWorldTransform(transform);
        return MathVec3f.convert(transform.origin);
	}
	
	public MathVec3f getRot() {
		Transform transform = new Transform();
        getMotionState().getWorldTransform(transform);
        Quat4f out = new Quat4f(); transform.getRotation(out);
        return new MathVec3f(out.x, out.y, out.z);
	}
	
	public void applyForce(MathVec3f force) {
		applyForce(force.convert());}
	public void applyForce(Vector3f force) {
		super.activate(true);
		super.applyCentralForce(force);
	}

	public void applyRotation(MathVec3f torque) {
		applyRotation(torque.convert());}
	public void applyRotation(Vector3f torque) {
		super.activate(true);
		super.applyTorque(torque);
	}
	
	public void setPosition(MathVec3f position) {
		setPosition(position.convert());}
	public void setPosition(Vector3f position) {
		Transform transform = new Transform();
        getMotionState().getWorldTransform(transform);
        
        Quat4f rotation = new Quat4f();
        transform.getRotation(rotation);
        float scale = transform.basis.getScale();
        
        Transform newTransform = new Transform(new Matrix4f(
        		rotation, position, scale));
        setCenterOfMassTransform(newTransform);
	}
	
	public void setRotation(MathVec3f rot) {
		setRotation(rot.convert());}
	public void setRotation(Vector3f rot) {
		setRotation(new Quat4f(rot.x, rot.y, rot.z, 1.0f));}
	public void setRotation(Quat4f rotation) {
		Transform transform = new Transform();
        getMotionState().getWorldTransform(transform);
        transform.setRotation(rotation);
	}
	
	public static class Builder {
		private Quat4f rotation;
		private Vector3f position;
		private float scale = 1;
		
		private Material material;
		private float volume = -1;
		
		private CollisionShape shape;
		
		public Builder setRotation(MathVec3f rotation) {
			return setRotation(rotation.convert());}
		public Builder setRotation(Vector3f rotation) { 
			this.rotation = new Quat4f(rotation.x, rotation.y, rotation.z, 1.0f); 
			return this;
		}
		
		public Builder setRotation(Quat4f rotation) { this.rotation = rotation; return this; }

		public Builder setPosition(float x, float y, float z) { return setPosition(new Vector3f(x, y, z)); }
		public Builder setPosition(MathVec3f position) { return setPosition(position.convert()); }
		public Builder setPosition(Vector3f position) { this.position = position; return this; }
		
		public Builder setMaterial(Material material) { this.material = material; return this; }
		public Builder setShape(CollisionShape shape) { this.shape = shape; return this; }
		public Builder setScale(float scale) { this.scale = scale; return this; }
		public Builder setVolume(float volume) { this.volume = volume; return this; }

		public PhysicsObject build() {			
			return new PhysicsObject(buildInfo());}		
		public RigidBodyConstructionInfo buildInfo() {
			if(shape == null) throw new IllegalArgumentException("No Shape was provided");
			if(material == null) throw new IllegalArgumentException("No Material was provided");			
			if(rotation == null) rotation = new Quat4f();
			if(position == null) position = new Vector3f();
			
			MotionState motionState = new DefaultMotionState(new Transform(
					new Matrix4f( rotation, position, scale )
				));
			
			if(volume == -1) {
				Vector3f minPos = new Vector3f(), maxPos = new Vector3f();
				shape.getAabb(new Transform(new Matrix4f(
						new Quat4f(), new Vector3f(1, 1, 1), 1)), minPos, maxPos);
				maxPos.sub(minPos); volume = maxPos.x * maxPos.y * maxPos.z;
			}
			
			float mass = volume * material.getDensity();
			Vector3f inertia = new Vector3f(); shape.calculateLocalInertia(mass, inertia);
			RigidBodyConstructionInfo constructInfo = new RigidBodyConstructionInfo(mass, motionState, shape, inertia);
			
			constructInfo.restitution = material.getElasticity();
			constructInfo.friction = material.getFrictionalCoefficient();
			
			return constructInfo;
		}
	}
}
