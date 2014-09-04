package com.GameName.Main.Threads;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.Buffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import com.GameName.Main.Start;
import com.GameName.Util.QueuedArray;

public class GLContextThread extends GameThread {

	private boolean prossessing;
	
//	private QueuedArray<Boolean> render3D;
//	private QueuedArray<RenderEngine> renderEngines;
	
	private QueuedArray<Buffer> buffers;
	private QueuedArray<Integer> targets;
	private QueuedArray<Integer> bufferIds;
	private QueuedArray<Integer> usages;
	private QueuedArray<Character> types;
	
	private boolean generatingBufferIds;
	private int requestAmount;
	private int[] generatedBufferIds;
	
	public GLContextThread(int tickRate) {
		super(tickRate, "GLContext Thread");

//		render3D = new QueuedArray<Boolean>();
//		renderEngines = new QueuedArray<RenderEngine>();
		
		buffers = new QueuedArray<Buffer>();
		targets = new QueuedArray<Integer>();
		bufferIds = new QueuedArray<Integer>();
		usages = new QueuedArray<Integer>();
		types = new QueuedArray<Character>();
	}

	void init() {
		Start.initDisplay(Start.WIDTH, Start.HEIGHT);
	}

	public void tick() {
		pushQueue();
		
		int i = 0;
//		if(sizeCheck(render3D, renderEngines)) {
//			for(Boolean rend3D : render3D.getElements()) {
//				if(rend3D.booleanValue()) {
//					renderEngines.get(i).render3D();
//				} else {
//					renderEngines.get(i).render2D();				
//				}
//				
//				i ++;
//			}
//		}
		
		
		
		i = 0;		
		if(sizeCheck(bufferIds, targets, usages, types, buffers)) {
			for(Integer bufferId : bufferIds.getElements()) {
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

		clear();
	}
	
	private void clear() {
		prossessing = true;
		
//		render3D.reset();
//		renderEngines.reset();
		
		buffers.reset();
		targets.reset();
		bufferIds.reset();
		usages.reset();
		types.reset();	   			

		glBindBuffer(GL_ARRAY_BUFFER, 0);	
		prossessing = false;		
	}
	
	private void pushQueue() {
		prossessing = true;
		
//		render3D.pushQueueAndClear();
//		renderEngines.pushQueueAndClear();
		
		buffers.pushQueueAndClear();
		targets.pushQueueAndClear();
		bufferIds.pushQueueAndClear();
		usages.pushQueueAndClear();
		types.pushQueueAndClear();
		
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
	
//	public synchronized  void addRender(RenderEngine render, boolean render3D) {
//		while(prossessing) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
//		
//		renderEngines.addToQueue(render);
//		this.render3D.addToQueue(new Boolean(render3D));
//	}
	
	public synchronized void addBufferBind(Buffer buffer, int target, int bufferId, int usage, char type) {
		while(prossessing) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		
		buffers.addToQueue(buffer);
		targets.addToQueue(new Integer(target));
		bufferIds.addToQueue(new Integer(bufferId));
		usages.addToQueue(new Integer(usage));
		types.addToQueue(new Character(type));
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