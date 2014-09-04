package com.GameName.Audio;

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_GAIN;
import static org.lwjgl.openal.AL10.AL_PITCH;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourcef;
import static org.lwjgl.openal.AL10.alSourcei;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.newdawn.slick.openal.WaveData;

public class Sound {
	private String name;
	private String type;
	private String location;
	private int soundId;
	
	private float pitch;
	private float volume;

	public Sound(String location) {
		this.location = location;
		this.name = getName(location);
		
		soundId = loadSound(location, type);
	}

	public Sound(String location, String type) {
		this.location = location;
		this.name = getName(location);
		this.type = type;

		soundId = loadSound(location, type);
	}

	public Sound(String location, String type, float pitch, float volume) {
		this.location = location;
		this.name = getName(location);
		this.type = type;
		this.pitch = pitch;
		this.volume = volume;

		soundId = loadSound(location, type);
	}
	
	public void playSound(int source) {
		alSourcef(source, AL_GAIN, volume);
		alSourcef(source, AL_PITCH, pitch);
		
		alSourcei(source, AL_BUFFER, soundId);	
		alSourcePlay(source);
	}
	
	private static int loadSound(String location, String type) {
		File file = new File("res/audio/" + location + "." + type);
		
		try {
			switch(type) {
				case "wav": 
					WaveData data = WaveData.create(new BufferedInputStream(new FileInputStream(file)));
					int buffer = alGenBuffers();
					alBufferData(buffer, data.format, data.data, data.samplerate);
					data.dispose();
				
				return buffer;
			}
			
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	private static String getName(String location) {
		int splitloc = location.lastIndexOf('/'); splitloc = splitloc < 0  ? 0 : splitloc;
		return location.substring(splitloc);
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getLocation() {
		return location;
	}

	public int getId() {
		return soundId;
	}

	public float getPitch() {
		return pitch;
	}

	public float getVolume() {
		return volume;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}
}
