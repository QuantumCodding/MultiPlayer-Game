package com.GameName.Audio;

import static org.lwjgl.openal.AL10.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import org.newdawn.slick.openal.WaveData;

public class SoundEngin {
	private int[] sounds;
	public int defaultSource;
	
	public SoundEngin() {
		sounds = new int[] {
				loadSound("01 In The End"),
				loadSound("Awesome Music"),
//				loadSound("Can\'t Hold Us"),
//				loadSound("Come And Get It")
		};
		
		defaultSource = alGenSources();
		alSourcei(defaultSource, AL_GAIN, 100);
		alSourcei(defaultSource, AL_PITCH, 1);
		alSource3f(defaultSource, AL_POSITION, 0, 0, 0);
	}
	
	private int loadSound(String name) {
		try {
			WaveData data = WaveData.create(new BufferedInputStream(new FileInputStream(new File("res/audio/" + name + ".wav"))));
			int buffer = alGenBuffers();
			alBufferData(buffer, data.format, data.data, data.samplerate);
			data.dispose();
			
			return buffer;
		} catch (FileNotFoundException e) {
			return -1;
		}
	}
	
	public void playSound(final String sound) {		
		playSound(accessByName(sound));
	}
	
	public void playSound(final int sound) {		
		playSound(sound, defaultSource);
	}
	
	public synchronized void playSound(final int sound, final int source) {
		alSourcei(source, AL_BUFFER, sound);	
		alSourcePlay(source);
	}
	
	public int playRandom() {
		int r = new Random().nextInt(sounds.length);
		playSound(sounds[r]);
		return r;
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
	
	public int accessByName(String name) {
		switch(name) {
			case "In The End": return sounds[0];
			case "Awesome Music": return sounds[1];
			case "Cant Hold Us": return sounds[1];
			case "Come And Get It": return sounds[2];
			default: return -1;
		}
	}
	
	public String accessNameByID(int id) {
		int i;
		for(i = 0; i < sounds.length; i ++)
			if(sounds[i] == id)
				break;
		
		switch(i) {
			case 0: return "In The End";
			case 1: return "Awesome Music";//"Can\'t Hold Us";
			case 2: return "Come And Get It";
			
			default: return "";
		}
	}
	
	public void cleanUp() {
		for(int i = 0; i < sounds.length; i ++)
			alDeleteBuffers(sounds[i]);
		
		alDeleteSources(defaultSource);
	}
}
