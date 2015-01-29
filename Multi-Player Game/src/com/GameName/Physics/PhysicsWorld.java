package com.GameName.Physics;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

public class PhysicsWorld {
	public static final Vector3f GRAVITY = new Vector3f(0, -1, 0);	
	private DynamicsWorld dynamicsWorld;
	
	public PhysicsWorld() {
		BroadphaseInterface broadphase = new DbvtBroadphase();
        CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
        ConstraintSolver solver = new SequentialImpulseConstraintSolver();
        dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
        
        dynamicsWorld.setGravity(GRAVITY);     
	}
	
	public void addObject(RigidBody obj) {
		dynamicsWorld.addRigidBody(obj);
	}
	
	public void removeObject(RigidBody obj) {
		dynamicsWorld.removeRigidBody(obj);
	}
	
	public void simulate(float delta) {
        dynamicsWorld.stepSimulation(delta);
	}
	
	public void cleanUp() {
		
	}
}
