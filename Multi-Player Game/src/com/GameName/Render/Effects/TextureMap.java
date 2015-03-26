package com.GameName.Render.Effects;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;

import org.lwjgl.BufferUtils;

import com.GameName.Engine.GameEngine;
import com.GameName.Render.Effects.Color.ColorFormat;
import com.GameName.Util.Vectors.Vector3f;

public class TextureMap { //            31, 13, 19, 45                   RGBA
	private int CLEAR_COLOR = new Color(31, 13, 19, 255).get(ColorFormat.RGBA);
	
	private Texture3D textureMap;
	private HashMap<String, Vector3f> map;
	
	private int[] rawTextureData;
	private int neededVolume;
	private int sideLength;
	
	private boolean useMipmap;
	private final GameEngine ENGINE;
	
	public TextureMap(GameEngine eng, boolean useMipmap) {
		ENGINE = eng;
		this.useMipmap = useMipmap;
		this.map = new HashMap<>();
		
		this.sideLength = 2;
		this.neededVolume = 8;
		
		rawTextureData = new int[8];
		Arrays.fill(rawTextureData, CLEAR_COLOR);
	}

	public Vector3f get(Object key) { return map.get(key); }
	public Texture3D getTextureMap() { return textureMap; }
	public int getSideLength() { return sideLength; }

	public void addTexture(String key, Texture... textures) {
		for(Texture texture : textures) {
			ByteBuffer textureBytes = ENGINE.getGLContext().getTextureData(texture);
			
			neededVolume += textureBytes.capacity() / 4;
			Vector3f position = findSpace(texture.getWidth(), texture.getHeight(), texture.getDepth());
			
			for(int sizeX = 0; sizeX < texture.getWidth();  sizeX ++) {
			for(int sizeY = 0; sizeY < texture.getHeight(); sizeY ++) {
			for(int sizeZ = 0; sizeZ < texture.getDepth();  sizeZ ++) {
				rawTextureData[(int) ((position.x + sizeX) + ((position.y + sizeY) * sideLength) + 
						((position.z + sizeZ) * sideLength * sideLength))] = textureBytes.getInt();
			}}}
			
			map.put(key, position);
		}
		
		generateTextureMap();
	}
	
	private void generateTextureMap() {
		ByteBuffer buffer = BufferUtils.createByteBuffer(rawTextureData.length * 4);
		for(int i : rawTextureData) {
			buffer.putInt(i);
		} buffer.flip();
		
		textureMap = ENGINE.getGLContext().genTexture3D(
				buffer, new Vector3f(sideLength, sideLength, sideLength), useMipmap);
	}
	
	private Vector3f findSpace(int width, int height, int depth) {
//		System.out.println("SideLength: " + sideLength + " Width: " + width + " Height: " + height + " Depth: " + depth);

		for(int z = 0; z < sideLength; z ++) {
		for(int y = 0; y < sideLength; y ++) {
		for(int x = 0; x < sideLength; x ++) {
			boolean foundSpace = true;
			
			sizeCheck:
			// Checks to see if Texture will Fit in current space	
			for(int sizeX = 0; sizeX < width;  sizeX ++) {
			for(int sizeY = 0; sizeY < height; sizeY ++) {
			for(int sizeZ = 0; sizeZ < depth;  sizeZ ++) {
				if((x + sizeX >= sideLength || y + sizeY >= sideLength || z + sizeZ >= sideLength) || 
					rawTextureData[(x + sizeX) + ((y + sizeY) * sideLength) + ((z + sizeZ) * sideLength * sideLength)] != CLEAR_COLOR) 
				{
					foundSpace = false;
					break sizeCheck;
				}
			}}}
			
			if(foundSpace)
				return new Vector3f(x, y, z);			
		}}}
		
		expand();
		return findSpace(width, height, depth);
	}
	
	private void expand() {
		int oldSideLength = sideLength; sideLength ++;
		
//				Perfect Square (2^x = SideLength)	   && SideLength^3 >= Needed Volume
		while((sideLength & -sideLength) != sideLength || Math.pow(sideLength, 3) < neededVolume) {
			sideLength ++;
		}
		
//		System.out.println("Old: "  + oldSideLength + " -> New: " + sideLength + "^3 = " + (Math.pow(sideLength, 3)) + " ~ Volume: " + neededVolume);
		int[] newRawData = new int[sideLength * sideLength * sideLength];
		Arrays.fill(newRawData, CLEAR_COLOR);
		
		//Copies data into new Arrays
		for(int x = 0; x < oldSideLength; x ++) {
		for(int y = 0; y < oldSideLength; y ++) {
		for(int z = 0; z < oldSideLength; z ++) {
			newRawData[x + (y * sideLength) + (z * sideLength * sideLength)] = 
					rawTextureData[x + (y * oldSideLength) + (z * oldSideLength * oldSideLength)];
		}}}
		
		rawTextureData = newRawData;
	}
	
	public void bind() {
		if(textureMap == null) getTextureMap();
		textureMap.bind();
	}
}
