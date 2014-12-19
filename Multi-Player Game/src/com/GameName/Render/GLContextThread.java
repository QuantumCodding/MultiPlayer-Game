package com.GameName.Render;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import com.GameName.Render.Effects.Texture;
import com.GameName.Util.QueuedArray;

public class GLContextThread {

	private boolean prossessing;
	
	private QueuedArray<Buffer> buffers;
	private QueuedArray<Integer> targets;
	private QueuedArray<Integer> bufferIds;
	private QueuedArray<Integer> usages;
	private QueuedArray<Character> types;
	
	private BufferedImage image;
	private String textureLocation;
	private String imageType;
	private Texture texture;
	private String textureName;
	private boolean useMipmap;
	private boolean generatingTexture;
	
	private boolean generatingBufferIds;
	private int requestAmount;
	private int[] generatedBufferIds;
	
	private QueuedArray<Integer> buffersToDelete;
	
	public GLContextThread() {		
		buffers = new QueuedArray<Buffer>();
		targets = new QueuedArray<Integer>();
		bufferIds = new QueuedArray<Integer>();
		usages = new QueuedArray<Integer>();
		types = new QueuedArray<Character>();
		
		buffersToDelete = new QueuedArray<Integer>();
	}

	public GLContextThread init() {
		return this;
	}

	public void tick() {
		pushQueue();
		
		int i = 0;	
		if(sizeCheck(bufferIds, targets, usages, types, buffers)) {
			for(Integer bufferId : bufferIds.getElements()) {
//				Logger.print("Binding Buffer: " + bufferId).setType("VBO").end();
				
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
		
		if(generatingTexture) {
			
			if(image != null) {
				texture = new Texture(image, textureName, useMipmap);
			} else {
				texture = new Texture(textureLocation, useMipmap, imageType);
			}
			
			generatingTexture = false;
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
		try {
			if(Display.isCurrent()) {
				this.requestAmount = requestAmount; 
				generatingBufferIds = true;
				
				genBuffers();
				return generatedBufferIds;
			}
		} catch (LWJGLException e) {}
		
		while(prossessing) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
	
		this.requestAmount = requestAmount; generatingBufferIds = true;
		while(generatingBufferIds) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}

		return generatedBufferIds;
	}
	
	public synchronized Texture genTexture(BufferedImage image, String textureName, boolean useMipmap) {
		try {
			if(Display.isCurrent()) {
				return new Texture(image, textureName, useMipmap);
			}
		} catch (LWJGLException e) {}
		
		while(generatingTexture) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		while(prossessing) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		
		this.image = image; this.textureName = textureName; 
		this.textureLocation = null; this.imageType = null;
		this.useMipmap = useMipmap; generatingTexture = true;
		
		while(generatingTexture) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		return texture;
	}
	
	public synchronized Texture genTexture(String textureLocation, String imageType, boolean useMipmap) {
		try {
			if(Display.isCurrent()) {
				return new Texture(textureLocation, useMipmap, imageType);
			}
		} catch (LWJGLException e) {}
		
		while(generatingTexture) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		while(prossessing) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		
		this.image = null; this.textureName = null;  
		this.textureLocation = textureLocation; this.imageType = imageType;
		this.useMipmap = useMipmap; generatingTexture = true;
		
		while(generatingTexture) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		return texture;
	}

	public synchronized void addBufferBind(Buffer buffer, int target, int bufferId, int usage, char type) {
		try {
			if(Display.isCurrent()) {
				glBindBuffer(target, bufferId);
				
				if(type == 'f') {
					FloatBuffer buffer2 = ((FloatBuffer) buffer);
				    glBufferData(target, buffer2, usage);	
				    
				} else if(type == 'd') {
					DoubleBuffer buffer2 = ((DoubleBuffer) buffer);
				    glBufferData(target, buffer2, usage);	
				    
				} else {
					IntBuffer buffer2 = ((IntBuffer) buffer);
				    glBufferData(target, buffer2, usage);					    
				}
				
				return;
			}
		} catch (LWJGLException e) {}
		
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