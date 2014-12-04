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
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourcef;
import static org.lwjgl.openal.AL10.alSourcei;

import java.util.ArrayList;

import com.GameName.Main.GameName;
import com.GameName.Util.IEngine;
import com.GameName.Util.Vectors.Vector3f;

public class SoundEngine implements IEngine<SoundEvent> {
	public static int defaultSource;
	
	public ArrayList<SoundEvent> soundEvents;
	
	static {
		defaultSource = alGenSources();
		alSourcei(defaultSource, AL_GAIN, 100);
		alSourcei(defaultSource, AL_PITCH, 1);
		alSource3f(defaultSource, AL_POSITION, 0, 0, 0);
	}
	
	public SoundEngine() {		
		soundEvents = new ArrayList<SoundEvent>();
	}
		
	public void add(SoundEvent obj) {
		soundEvents.add(obj);
	}

	public void remove(SoundEvent obj) {
		soundEvents.remove(obj);
	}

	public void step(float delta) {
		for (int i = 0; i < soundEvents.size(); i ++) {
			playSound(soundEvents.get(i));
			soundEvents.remove(i);
		}
	}
	
	public void playSound(SoundEvent event) {
		alSourcef(event.getSource(), AL_GAIN, event.getGain());
		alSourcef(event.getSource(), AL_PITCH, event.getPitch());
		
		Vector3f playPos = GameName.player.getAccess().getPos().subtract(event.getPos());
		alSource3f(event.getSource(), AL_POSITION, playPos.getX(), playPos.getY(), playPos.getZ());
		
		alSourcei(event.getSource(), AL_BUFFER, event.getSound().getId());	
		alSourcePlay(event.getSource());
	}
	
	
	public void pauseSound(int source) {
		alSourcePause(source);
	}
	
	public void stopSound(int source) {
		alSourceStop(source);
	}
	
	public int getIsPlaying(int source) {
		return alGetSourcei(source, AL_BUFFER);
	}

	public void cleanUp() {
		if(SoundRegistry.getSounds() == null) return;
		
		for(int i = 0; i < SoundRegistry.getSounds().length; i ++) {
			if(SoundRegistry.getSounds()[i] != null) {
				alDeleteBuffers(SoundRegistry.getSounds()[i].getId());
			}
		}
		
		alDeleteSources(defaultSource);
	}
}
