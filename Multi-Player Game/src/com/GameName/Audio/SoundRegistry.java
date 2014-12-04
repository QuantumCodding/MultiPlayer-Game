package com.GameName.Audio;

import java.util.ArrayList;

import com.GameName.Util.Registry;
import com.GameName.Util.RegistryStorage;

public class SoundRegistry extends Registry<Sound> {	
	private static Sound[] sounds;
	private static RegistryStorage<Sound> regstries;
	private static ArrayList<Sound> unregisteredSounds;
	
	static {
		regstries = new RegistryStorage<Sound>();
		unregisteredSounds = new ArrayList<Sound>();
	}
	
	public static Sound[] getSounds() {
		return sounds;
	}
	
	public void addSound(Sound sound) {
		registerOBJ(sound);
	}

	public static void register() {regstries.register();}
	
	public static void addRegistry(SoundRegistry reg) {
		regstries.addRegistry(reg);
	}
	
	protected void register(Sound e) {
		unregisteredSounds.add(e);
	}
	
	protected void registrtionConcluded() {
		sounds = unregisteredSounds.toArray(new Sound[unregisteredSounds.size()]);
		
		unregisteredSounds.clear();
		unregisteredSounds = null;
	}	
	
	public static Sound accessByName(String name) {
		for(Sound sound : getSounds()) {
			if(sound.getName().equals(name)) {
				return sound;
			}
		}
		
		return null;
	}
	
	public static Sound getSound(int index) {
		return getSounds()[index];
	}
	
	public static void cleanUp() {
		if(sounds == null) return;
		
		for(Sound sound : sounds) {
			if(sound != null) {
				sound.cleanUp();
			}
		}
	} 
}
