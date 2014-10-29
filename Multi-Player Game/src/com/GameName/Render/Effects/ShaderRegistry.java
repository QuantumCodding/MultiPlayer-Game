package com.GameName.Render.Effects;

import java.util.ArrayList;

import com.GameName.Util.Registry;
import com.GameName.Util.RegistryStorage;

public class ShaderRegistry extends Registry<Shader> {
	private static Shader[] shaders;
	private static RegistryStorage<Shader> regstries;
	private static ArrayList<Shader> unregisteredShaders;
	
	static {
		regstries = new RegistryStorage<Shader>();
		unregisteredShaders = new ArrayList<Shader>();
	}
	
	public static Shader[] getShaders() {
		return shaders;
	}
	
	public void addShader(Shader shader) {
		registerOBJ(shader);
	}

	public static void register() {regstries.register();}
	
	public static void addRegistry(ShaderRegistry reg) {
		regstries.addRegistry(reg);
	}
	
	protected void register(Shader e) {
		unregisteredShaders.add(e);
	}
	
	protected void registrtionConcluded() {
		shaders = unregisteredShaders.toArray(new Shader[unregisteredShaders.size()]);
		
		unregisteredShaders.clear();
		unregisteredShaders = null;
	}
	
	public static Shader accessByName(String name) {
		for(Shader shader : getShaders()) {
			if(shader.getName().equals(name)) {
				return shader;
			}
		}
		
		return null;
	}
	
	public static void cleanUp() {
		for(Shader shader : shaders) {
			shader.cleanUp();
		}
	} 
}
