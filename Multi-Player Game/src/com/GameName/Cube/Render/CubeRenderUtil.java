package com.GameName.Cube.Render;

import java.util.HashSet;

import com.GameName.Cube.Cube;
import com.GameName.Engine.GameEngine;
import com.GameName.Render.Effects.Texture;
import com.GameName.Render.Effects.TextureMap;


public class CubeRenderUtil {
	public static TextureMap generateTexturMap(GameEngine ENIGNE, HashSet<Cube> cubes, boolean useMipmap) { 
		TextureMap textureMap = new TextureMap(ENIGNE, useMipmap);
		
		for(Cube cube : cubes) {
			Texture[][] cubeTextures = cube.getTextures();
			
			for(int frame = 0; frame < cubeTextures.length; frame ++) {
			for(int side = 0; side < cubeTextures[frame].length; side ++) {
				textureMap.addTexture(cube.getName() + ":" + frame + ":" + side, cubeTextures[frame][side]);
			}}
		}
		
		return textureMap;
	}
	
	public static TextureMap addCubeToTextureMap(Cube cube, TextureMap textureMap) { 
		Texture[][] cubeTextures = cube.getTextures();
		
		for(int frame = 0; frame < cubeTextures.length; frame ++) {
		for(int side = 0; side < cubeTextures[frame].length; side ++) {
			textureMap.addTexture(cube.getName() + ":" + frame + ":" + side, cubeTextures[frame][side]);
		}}
		
		return textureMap;
	}
}
