package com.GameName.Audio;

import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alGenBuffers;

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

	public void cleanUp() {
		alDeleteBuffers(soundId);		
	}
}
