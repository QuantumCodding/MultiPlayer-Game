package com.GameName.Cube.Render;

import java.util.HashMap;

import javax.vecmath.Vector2f;

import com.GameName.Cube.Cube;
import com.GameName.Render.Effects.Texture;

public class CubeTextureMap {
	private Texture texture;
	
	/**
	 * 	Strings format is "cubeId:frame"
	 * 	@see Cube.texture
	 */
	private HashMap<String, Vector2f> textCordMap;
	
	private int textureSheetSideLength;
	private int textureSheetMaxFrames;
	
	public CubeTextureMap(Texture texture, HashMap<String, Vector2f> textCordMap,	int textureSheetSideLength, int textureSheetMaxFrames) {
		this.texture = texture;
		this.textCordMap = textCordMap;
		
		this.textureSheetSideLength = textureSheetSideLength;
		this.textureSheetMaxFrames = textureSheetMaxFrames;
	}

	public Vector2f get(String key) {
		return textCordMap.get(key);
	}

	public Texture getTexture() {
		return texture;
	}

	public HashMap<String, Vector2f> getTextCordMap() {
		return textCordMap;
	}

	public int getTextureSheetSideLength() {
		return textureSheetSideLength;
	}

	public int getTextureSheetMaxFrames() {
		return textureSheetMaxFrames;
	}
	
	protected void setTexture(Texture texture) {
		this.texture = texture;
	}

	protected Vector2f put(String key, Vector2f value) {
		return textCordMap.put(key, value);
	}
}
