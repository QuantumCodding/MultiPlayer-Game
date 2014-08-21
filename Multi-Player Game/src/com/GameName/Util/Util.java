package com.GameName.Util;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;

public class Util {
	
	public static FloatBuffer createFillipedFloatBuffer(List<Float> values) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(values.size());
		
		for(int i = 0; i < values.size(); i ++) {
			buffer.put(values.get(i));
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static DoubleBuffer createFillipedDoubleBuffer(List<Double> values) {
		DoubleBuffer buffer = BufferUtils.createDoubleBuffer(values.size());
		
		for(int i = 0; i < values.size(); i ++) {
			buffer.put(values.get(i));
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static IntBuffer createFillipedIntBuffer(List<Integer> values) {
		IntBuffer buffer = BufferUtils.createIntBuffer(values.size());
		
		for(int i = 0; i < values.size(); i ++) {
			buffer.put(values.get(i));
		}
		
		buffer.flip();
		
		return buffer;
	}
}
