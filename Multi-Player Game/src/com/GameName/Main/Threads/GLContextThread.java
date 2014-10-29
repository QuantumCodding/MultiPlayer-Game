package com.GameName.Main.Threads;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.Buffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import com.GameName.Main.Start;
import com.GameName.Main.Debugging.Logger;
import com.GameName.Util.QueuedArray;

public class GLContextThread extends GameThread {

	private boolean prossessing;
	
	private QueuedArray<Buffer> buffers;
	private QueuedArray<Integer> targets;
	private QueuedArray<Integer> bufferIds;
	private QueuedArray<Integer> usages;
	private QueuedArray<Character> types;
	
	private boolean generatingBufferIds;
	private int requestAmount;
	private int[] generatedBufferIds;
	
	private QueuedArray<Integer> buffersToDelete;
	
	public GLContextThread(int tickRate) {
		super(tickRate, "GLContext Thread");
		
		buffers = new QueuedArray<Buffer>();
		targets = new QueuedArray<Integer>();
		bufferIds = new QueuedArray<Integer>();
		usages = new QueuedArray<Integer>();
		types = new QueuedArray<Character>();
		
		buffersToDelete = new QueuedArray<Integer>();
	}

	void init() {
		Start.initDisplay(Start.WIDTH, Start.HEIGHT);
	}

	public void tick() {
		pushQueue();
		
		int i = 0;	
		if(sizeCheck(bufferIds, targets, usages, types, buffers)) {
			for(Integer bufferId : bufferIds.getElements()) {
				Logger.print("Binding Buffer: " + bufferId).setType("VBO").end();
				
				int target = targets.get(i).intValue();
				int usage = usages.get(i).intValue(); 
				
				glBindBuffer(target, bufferId);
				
				if(types.get(i).charValue() == 'f') {
					FloatBuffer buffer = ((FloatBuffer) buffers.get(i));
				    glBufferData(target, buffer, usage);	
				    
				} else if(types.get(i).charValue() == 'd') {
					DoubleBuffer buffer = ((DoubleBuffer) buffers.get(i));
				    glBufferData(target, buffer, usage);	
				    
				} else {
					IntBuffer buffer = ((IntBuffer) buffers.get(i));
				    glBufferData(target, buffer, usage);	
				    
				}	
			    
			    i ++;
			}
		}
		
		if(generatingBufferIds) {
			genBuffers();				
			generatingBufferIds = false;
		}
		
		for(Integer buffer : buffersToDelete.getElements()) {
			glDeleteBuffers(buffer.intValue());
		}

		clear();
	}
	
	private void clear() {
		prossessing = true;
				
		buffers.reset();
		targets.reset();
		bufferIds.reset();
		usages.reset();
		types.reset();	   
		
		buffersToDelete.reset();

		glBindBuffer(GL_ARRAY_BUFFER, 0);	
		prossessing = false;		
	}
	
	private void pushQueue() {
		prossessing = true;
		
		buffers.pushQueueAndClear();
		targets.pushQueueAndClear();
		bufferIds.pushQueueAndClear();
		usages.pushQueueAndClear();
		types.pushQueueAndClear();
		
		buffersToDelete.pushQueueAndClear();
		
		prossessing = false;
	}
	
	private void genBuffers() {
		IntBuffer buffer = BufferUtils.createIntBuffer(requestAmount);
		glGenBuffers(buffer); 
		generatedBufferIds = new int[buffer.capacity()];
				
		for(int i = 0; i < buffer.capacity(); i ++) {
			generatedBufferIds[i] = buffer.get(i);
		}
	}	

	public synchronized int[] genBufferIds(int requestAmount) {
		while(prossessing) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
	
		this.requestAmount = requestAmount; generatingBufferIds = true;
		while(generatingBufferIds) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}

		return generatedBufferIds;
	}
		
	public synchronized void addBufferBind(Buffer buffer, int target, int bufferId, int usage, char type) {
		while(prossessing) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		
		buffers.addToQueue(buffer);
		targets.addToQueue(new Integer(target));
		bufferIds.addToQueue(new Integer(bufferId));
		usages.addToQueue(new Integer(usage));
		types.addToQueue(new Character(type));
	}
	
	public synchronized void deleteBuffer(int buffer) {
		buffersToDelete.addToQueue(buffer);
	}
	
	private boolean sizeCheck(QueuedArray<?>... arrays) {
		for(int i = 0; i < arrays.length; i ++) {
			for(int j = i + 1; j < arrays.length; j ++) {
				if(arrays[i].elementsSize() != arrays[j].elementsSize()) {
					return false;
				}
			}
		}
		
		return true;
	}
}