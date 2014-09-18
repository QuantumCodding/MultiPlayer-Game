package com.GameName.Render.Effects;

import java.util.ArrayList;

import com.GameName.Util.Registry;

public class TextureRegistry extends Registry<Texture> {
private static Texture[] textures;
	
	public static void register() {
		ArrayList<Texture> unregisteredTextures = new ArrayList<Texture>();
		
		for(Registry<?> reg : getRegistries()) {
			for(Texture texture : (Texture[]) reg.toArray()) {
				unregisteredTextures.add(texture);
			}
		}
		
		getRegistries().clear();
		textures = unregisteredTextures.toArray(new Texture[unregisteredTextures.size()]);
				
		isConcluded = true;
	}
	
	public static Texture accessByName(String name) {
		for(Texture texture : getGuis()) {
			if(texture.getTextureName().equals(name)) {
				return texture;
			}
		}
		
		return null;
	}
	
	public static Texture getSound(int index) {
		return getGuis()[index];
	}
	
	public static Texture[] getGuis() {
		return textures;
	}
	
	public void addTexture(Texture texture) {
		register(texture);
	}	
}
