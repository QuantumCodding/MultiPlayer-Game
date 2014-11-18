package com.GameName.Render.Effects;

import java.util.ArrayList;

import com.GameName.Util.Registry;
import com.GameName.Util.RegistryStorage;

public class TextureRegistry extends Registry<Texture> {
	private static Texture[] textures;
	private static RegistryStorage<Texture> regstries;
	private static ArrayList<Texture> unregisteredTextures;
	
	static {
		regstries = new RegistryStorage<Texture>();
		unregisteredTextures = new ArrayList<Texture>();
	}
	
	public static Texture[] getTextures() {
		return textures;
	}
	
	public void addTexture(Texture texture) {
		registerOBJ(texture);
	}

	public static void register() {regstries.register();}
	
	public static void addRegistry(TextureRegistry reg) {
		regstries.addRegistry(reg);
	}
	
	protected void register(Texture e) {
		unregisteredTextures.add(e);
	}
	
	protected void registrtionConcluded() {
		textures = unregisteredTextures.toArray(new Texture[unregisteredTextures.size()]);
		
		unregisteredTextures.clear();
		unregisteredTextures = null;
	}
	
	public static Texture accessByName(String name) {
		for(Texture texture : getTextures()) {
			if(texture.getTextureName().equals(name)) {
				return texture;
			}
		}
		
		return null;
	}
	
	public static void cleanUp() {
		if(textures == null) return;
		
		for(Texture texture : textures) {
			if(texture != null) {
				texture.cleanUp();
			}
		}
	} 
}
