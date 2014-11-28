package com.GameName.World.Generation;

public class DensityNode {

	private int x, y, z;
	private float value;
	
	public DensityNode(int chunk_X, int chunk_Y, int chunk_Z, float node_value) {
		x = chunk_X;
		y = chunk_Y;
		z = chunk_Z;
		value = node_value;
	}
	
	public int getX() {return x;}
	public int getY() {return y;}
	public int getZ() {return z;}
	public float getValue() {return value;}
}
