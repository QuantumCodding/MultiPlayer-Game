package com.GameName.Render.Effects;

import java.util.ArrayList;

import com.GameName.Util.Registry;

public class ShaderRegistry extends Registry<Shader> {
	private static Shader[] shaders;
	
	public static void register() {
		ArrayList<Shader> unregisteredShaders = new ArrayList<Shader>();
		
		for(Registry<?> reg : getRegistries()) {
			for(Shader shader : (Shader[]) reg.toArray()) {
				unregisteredShaders.add(shader);
			}
		}
		
		getRegistries().clear();
		shaders = unregisteredShaders.toArray(new Shader[unregisteredShaders.size()]);
				
		isConcluded = true;
	}
	
	public static Shader accessByName(String name) {
		for(Shader shader : getGuis()) {
			if(shader.getName().equals(name)) {
				return shader;
			}
		}
		
		return null;
	}
	
	public static Shader getSound(int index) {
		return getGuis()[index];
	}
	
	public static Shader[] getGuis() {
		return shaders;
	}
	
	public void addShader(Shader shader) {
		register(shader);
	}	
}
