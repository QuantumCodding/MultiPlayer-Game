package com.GameName.Audio;

import com.GameName.Util.Vectors.MathVec3f;

public class SoundEvent {
	private int source;
	private Sound sound;
	
	private int pitch;
	private int gain;
	
	private MathVec3f pos;
	
	public SoundEvent(int source, Sound sound, int pitch, int gain) {
		this(source, sound, pitch, gain, new MathVec3f(0, 0, 0));
	}
	
	public SoundEvent(int source, Sound sound, int pitch, int gain, MathVec3f pos) {
		this.source = source;
		this.sound = sound;
		this.pitch = pitch;
		this.gain = gain;
		this.pos = pos;
	}

	public int getSource() {
		return source;
	}

	public Sound getSound() {
		return sound;
	}

	public int getPitch() {
		return pitch;
	}

	public int getGain() {
		return gain;
	}

	public MathVec3f getPos() {
		return pos;
	}

	public void setPos(MathVec3f pos) {
		this.pos = pos;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}

	public void setPitch(int pitch) {
		this.pitch = pitch;
	}

	public void setGain(int gain) {
		this.gain = gain;
	}	
}
