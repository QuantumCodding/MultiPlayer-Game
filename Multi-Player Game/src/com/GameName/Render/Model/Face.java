package com.GameName.Render.Model;

public class Face {
	private int[] vertexIndices = {-1, -1, -1};
	private int[] normalIndices = {-1, -1, -1};
	private int[] textureCoordIndices = {-1, -1, -1};
	private Material material;
	
	public Face(int[] vertexIndices) {
		this.vertexIndices = vertexIndices.clone();
	}
	
	public Face(int[] vertexIndices, int[] normalIndices) {
		this.vertexIndices = vertexIndices.clone();		
		this.normalIndices = normalIndices.clone();
	}
	
	public Face(int[] vertexIndices, int[] normalIndices, int[] textureCoordIndices, Material material) {
		this.vertexIndices = vertexIndices.clone();		
		this.textureCoordIndices = textureCoordIndices.clone();
		this.normalIndices = normalIndices.clone();
		
		this.material = material;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public boolean hasNormals() {
		return normalIndices[0] != -1;
	}
	
	public boolean hasTextureCoord() {
		return textureCoordIndices[0] != -1;
	}
	
	public int[] getVertexIndices() {
		return vertexIndices;
	}
	
	public int[] getTextureCoordinateIndices() {
		return textureCoordIndices;
	}
	
	public int[] getNormalIndices() {
		return normalIndices;
	}
}
