package com.GameName.Audio;

import java.util.ArrayList;

import com.GameName.Util.Registry;

public class SoundRegistry extends Registry<Sound> {	
	private static Sound[] sounds;
	
	public static void register() {
		ArrayList<Sound> unregisteredSounds = new ArrayList<Sound>();
		
		for(Registry<?> reg : getRegistries()) {
			for(Sound sound : (Sound[]) reg.toArray()) {
				unregisteredSounds.add(sound);
			}
		}
		
		getRegistries().clear();
		sounds = unregisteredSounds.toArray(new Sound[unregisteredSounds.size()]);
				
		isConcluded = true;
	}
	
	public static Sound accessByName(String name) {
		for(Sound sound : getSounds()) {
			if(sound.getName().equals(name)) {
				return sound;
			}
		}
		
		return null;
	}
	
	public static Sound accessById(int id) {
		for(Sound sound : getSounds()) {
			if(sound.getId() == id) {
				return sound;
			}
		}
		
		return null;
	}
	
	public static Sound getSound(int index) {
		return getSounds()[index];
	}
	
	public static Sound[] getSounds() {
		return sounds;
	}
	
	public void addSound(Sound sound) {
		register(sound);
	}
}
