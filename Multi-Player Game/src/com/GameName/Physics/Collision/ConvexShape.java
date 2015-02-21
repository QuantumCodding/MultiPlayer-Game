package com.GameName.Physics.Collision;

import java.util.ArrayList;

import com.GameName.Util.Vectors.Vector3f;

public class ConvexShape extends CollisionShape {
	private ArrayList<Triangle> faces;
	private Vector3f center;
	
	public ConvexShape(ArrayList<Triangle> faces) {
		//TODO: Check to see if shape is convex
		this.faces = faces;
		
		for(Triangle triangle : faces) {
			center = center.add(triangle.getCenter());
		}
		
		center = center.divide(faces.size());
	} 
	
	public AxisAligneBoundingBox getAABB() {
		Vector3f maxPos = center.clone(), minPos = center.clone();
		for(Triangle triangle : faces) {
			for(Vector3f vert : triangle.getVertsArray()) {
				maxPos.x = Math.max(vert.x, maxPos.x);
				maxPos.y = Math.max(vert.y, maxPos.y);
				maxPos.z = Math.max(vert.z, maxPos.z);
				
				minPos.x = Math.min(vert.x, minPos.x);
				minPos.y = Math.min(vert.y, minPos.y);
				minPos.z = Math.min(vert.z, minPos.z);
			}
		}
		
		return new AxisAligneBoundingBox(minPos, maxPos);
	}

	public void rotate(Vector3f amount) {
		for(Triangle triangle : faces) {
			triangle.rotate(amount);
		}
	}

	public void translate(Vector3f amount) {
		for(Triangle triangle : faces) {
			triangle.translate(amount);
		}
		
		center.add(amount);
	}

	public Vector3f getCenter() {
		return center;
	}
	
	public float getVolume() {
		float volume = 0.0f;		
		for(Triangle triangle : faces) {
			volume += (float) ((1.0/3.0) * triangle.getArea() * 
					triangle.getCenter().distance(getCenter()));
		}
		
		return volume;
	}
	
	public boolean intersect(Vector3f vertex) {
		for(Triangle plane : getFaces()) {
			if(!intersectsPlane(plane, vertex))
				return false;
		}
		
		return true;
	}
	
	private boolean intersectsPlane(Triangle plane, Vector3f checking) {
		Vector3f normal = plane.getNormal(), vertex = plane.getVertA();
		Vector3f external = plane.getCenter().add(normal), intersectionPoint; 
		int t = 0;
		
		while(!(intersectionPoint = external.add(checking.subtract(external).multiply(t ++))).equals(checking)) {
			if(0 == normal.x * (intersectionPoint.x - vertex.x) +
					normal.y * (intersectionPoint.y - vertex.y) +
					normal.z * (intersectionPoint.z - vertex.z) ) 
				return true;
		}
		
		return false;
	}
	
	public ArrayList<Triangle> getFaces() { 
		return faces;
	}
	
	public class Triangle {
		private Vector3f vertA, vertB, vertC;
		private float angleA, angleB, angleC;
		private float sideAB, sideAC, sideBC;
		
		public Triangle(Vector3f vertA, Vector3f vertB, Vector3f vertC) {
			this.vertA = vertA;
			this.vertB = vertB;
			this.vertC = vertC;
			
			sideAB = vertA.distance(vertB);
			sideAC = vertA.distance(vertC);
			sideBC = vertB.distance(vertC); 
			
			angleA = (float) Math.acos((sideAB*sideAB + sideAC*sideAC - sideBC*sideBC) / (2 * sideAB * sideAC));
			angleB = (float) Math.asin((Math.sin(angleA) * sideAC) / sideBC);
			angleC = 180 - angleA - angleB;
		}

		public float getArea() {
			float s = (sideAB + sideAC + sideBC) / 2.0f;
			return (float) Math.sqrt(s * (s - sideAB) * (s - sideAC) * (s - sideBC));
		}
		
		public Vector3f getCenter() {
			return new Vector3f(
					(vertA.x + vertB.x + vertC.x) / 3.0f, 
					(vertA.y + vertB.y + vertC.y) / 3.0f, 
					(vertA.z + vertB.z + vertC.z) / 3.0f
				);
		}
		
		public Vector3f getNormal() {
			Vector3f AB = vertA.subtract(vertB);
			Vector3f AC = vertA.subtract(vertC);
			return AB.crossProduct(AC);			
		}
		
		public void rotate(Vector3f amount) {
			vertA = vertA.rotate(amount);
			vertB = vertB.rotate(amount);
			vertC = vertC.rotate(amount);
		}
		
		public void translate(Vector3f amount) {
			vertA = vertA.add(amount);
			vertB = vertB.add(amount);
			vertC = vertC.add(amount);
		}
		
		public Vector3f getVertA() { return vertA; }  public float getAngleA() { return angleA; }
		public Vector3f getVertB() { return vertB; }  public float getAngleB() { return angleB; }
		public Vector3f getVertC() { return vertC; }  public float getAngleC() { return angleC; }

		public float getSideAB() { return sideAB; }   public float getSideC() { return sideAB; } 
		public float getSideAC() { return sideAC; }   public float getSideB() { return sideAC; } 
		public float getSideBC() { return sideBC; }   public float getSideA() { return sideBC; }	
		
		public Vector3f[] getVertsArray() { return new Vector3f[]{vertA, vertB, vertC}; }
	}
}
