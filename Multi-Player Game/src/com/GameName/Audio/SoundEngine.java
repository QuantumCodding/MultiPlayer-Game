package com.GameName.Audio;

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_GAIN;
import static org.lwjgl.openal.AL10.AL_PITCH;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alGetSourcei;
import static org.lwjgl.openal.AL10.alSource3f;
import static org.lwjgl.openal.AL10.alSourcePause;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourcei;

import java.util.ArrayList;

import javax.management.InstanceAlreadyExistsException;

public class SoundEngine {
	private Sound[] sounds;
	public static int defaultSource;
	private boolean isConcluded;
	
	private ArrayList<SoundRegistry> registries;
	
	static {
		defaultSource = alGenSources();
		alSourcei(defaultSource, AL_GAIN, 100);
		alSourcei(defaultSource, AL_PITCH, 1);
		alSource3f(defaultSource, AL_POSITION, 0, 0, 0);
	}
	
	public SoundEngine() {
		registries = new ArrayList<SoundRegistry>();
		
		SoundRegistry reg = new SoundRegistry();
		
		reg.addSound(new Sound("01 In The End", "wav"));		
		reg.addSound(new Sound("Awesome Music", "wav"));	
		reg.addSound(new Sound("Can\'t Hold Us", "wav"));	
		reg.addSound(new Sound("Come And Get It", "wav"));
				
		addRegister(reg);
	}
	
	public void addRegister(SoundRegistry regester) {
		registries.add(regester);
	}
	
	public void registerSounds() throws InstanceAlreadyExistsException {
		if(isConcluded) throw new InstanceAlreadyExistsException("Sound Engine is already Concluded! Run clean-up befor Concluded");
		
		ArrayList<Sound> toAdd = new ArrayList<Sound>();
		for(SoundRegistry reg : registries) {
			for(Sound sound : reg.toArray()) {
				toAdd.add(sound);
			}
		}
		
		registries.clear();
		sounds = toAdd.toArray(new Sound[toAdd.size()]);
		
		isConcluded = true;
	}
		
	public synchronized void pauseSound(final int source) {
		alSourcePause(source);
	}
	
	public synchronized void stopSound(final int source) {
		alSourceStop(source);
	}
	
	public int getPlaying(int source) {
		return alGetSourcei(source, AL_BUFFER);
	}
	
	public boolean isConcluded() {
		return isConcluded;
	}
	
	public Sound accessByName(String name) {
		for(Sound sound : sounds) {
			if(sound.getName().equals(name)) {
				return sound;
			}
		}
		
		return null;
	}
	
	public Sound accessById(int id) {
		for(Sound sound : sounds) {
			if(sound.getId() == id) {
				return sound;
			}
		}
		
		return null;
	}
	
	public Sound getSound(int index) {
		return sounds[index];
	}

	public void cleanUp() {
		for(int i = 0; i < sounds.length; i ++)
			alDeleteBuffers(sounds[i].getId());
		
		alDeleteSources(defaultSource);
	}
}
