package com.GameName.Audio;

import java.util.ArrayList;

public class SoundRegistry {
	private ArrayList<Sound> sounds = new ArrayList<Sound>();
	
	public void addSound(Sound sound) {
		sounds.add(sound);
	}
	
	public Sound[] toArray() {
		Sound[] toRep = new Sound[sounds.size()];
		toRep = sounds.toArray(toRep);
		
		return toRep;
	}
}
