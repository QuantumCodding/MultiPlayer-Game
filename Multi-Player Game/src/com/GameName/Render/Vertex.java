package com.GameName.Render;

import com.GameName.Util.Vector2d;
import com.GameName.Util.Vector3f;

public class Vertex {
	public static final int SIZE = 3 + (2 * 2) + 3;
	
	private Vector3f position;
	private Vector2d texCoord;
	private Vector3f lightValue;
	
	public Vertex(Vector3f position, Vector2d texCoord, Vector3f lightValue) {
		this.position = position;
		this.texCoord = texCoord;
		this.lightValue = lightValue;
	}

	public String toString() {
		return "Vertex [position=" + position + ", texCoord=" + texCoord
				+ ", lightValue=" + lightValue + "]";
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result
				+ ((lightValue == null) ? 0 : lightValue.hashCode());
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		result = prime * result
				+ ((texCoord == null) ? 0 : texCoord.hashCode());
		
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Vertex)) return false;
		
		Vertex other = (Vertex) obj;
		
		if (lightValue == null) {
			if (other.lightValue != null)
				return false;
			
		} else if (!lightValue.equals(other.lightValue))
			return false;
		
		if (position == null) {
			if (other.position != null)
				return false;
			
		} else if (!position.equals(other.position))
			return false;
		
		if (texCoord == null) {
			if (other.texCoord != null)
				return false;
			
		} else if (!texCoord.equals(other.texCoord))
			return false;
		
		return true;
	}



	public Vector3f getPosition() {
		return position;
	}

	public Vector2d getTexCoord() {
		return texCoord;
	}

	public Vector3f getLightValue() {
		return lightValue;
	}
	
	
}
