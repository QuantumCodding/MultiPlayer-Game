package com.GameName.Render;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetTexImage;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import com.GameName.Render.Effects.Texture;
import com.GameName.Render.Effects.Texture1D;
import com.GameName.Render.Effects.Texture2D;
import com.GameName.Render.Effects.Texture3D;
import com.GameName.Util.QueuedArray;
import com.GameName.Util.Vectors.Vector3f;

public class GLContextThread {

	private boolean prossessing;
	
	private QueuedArray<Buffer> buffers;
	private QueuedArray<Integer> targets;
	private QueuedArray<Integer> bufferIds;
	private QueuedArray<Integer> usages;
	private QueuedArray<Character> types;
	
	private BufferedImage image1D, image2D;
	private ByteBuffer image3D; private Vector3f size3D;
	private boolean useMipmap1D, useMipmap2D, useMipmap3D;
	private boolean generating1D, generating2D, generating3D;
	private Texture1D texture1D; private Texture2D texture2D;
	private Texture3D texture3D;
	
	private Texture textureToGetData;
	private ByteBuffer textureDataResult;
	private boolean gettingTextureData;
	
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
		
		if(generating1D) {
			texture1D = new Texture1D(image1D, useMipmap1D);			
			generating1D = false;
		}
		
		if(generating2D) {
			texture2D = new Texture2D(image2D, useMipmap2D);			
			generating2D = false;
		}
		
		if(generating3D) {
			texture3D = new Texture3D(image3D, size3D, useMipmap3D);
			generating3D = false;
		}
		
		if(gettingTextureData) {
			glEnable(textureToGetData.getType().glType()); textureToGetData.bind();
			glGetTexImage(textureToGetData.getType().glType(), 0, GL_RGBA, GL_UNSIGNED_BYTE, textureDataResult);
			glDisable(textureToGetData.getType().glType());
			
			gettingTextureData = false;
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
	
	public synchronized Texture1D genTexture1D(BufferedImage image, boolean useMipmap) {
		try {
			if(Display.isCurrent()) {
				return new Texture1D(image, useMipmap);
			}
		} catch (LWJGLException e) {}
		
		while(generating1D) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		while(prossessing) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		
		this.image1D = image; 
		this.useMipmap1D = useMipmap; 
		this.generating1D = true;
		
		while(generating1D) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		return texture1D;
	}
	
	public synchronized Texture2D genTexture2D(BufferedImage image, boolean useMipmap) {
		try {
			if(Display.isCurrent()) {
				return new Texture2D(image, useMipmap);
			}
		} catch (LWJGLException e) {}
		
		while(generating2D) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		while(prossessing) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		
		this.image2D = image; 
		this.useMipmap2D = useMipmap; 
		this.generating2D = true;
		
		while(generating2D) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		return texture2D;
	}
	
	public synchronized Texture3D genTexture3D(ByteBuffer image, Vector3f size, boolean useMipmap) {
		try {
			if(Display.isCurrent()) {
				return new Texture3D(image, size, useMipmap);
			}
		} catch (LWJGLException e) {}
		
		while(generating3D) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		while(prossessing) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		
		this.image3D = image; 
		this.size3D = size;
		this.useMipmap3D = useMipmap; 
		this.generating3D = true;
		
		while(generating3D) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		return texture3D;
	}
	
	public synchronized ByteBuffer getTextureData(Texture texture) {
		try {
			if(Display.isCurrent()) {
				ByteBuffer textureDataResult = BufferUtils.createByteBuffer(
						texture.getWidth() * texture.getHeight() * texture.getDepth() * 4);
				
				glEnable(texture.getType().glType()); texture.bind();
				glGetTexImage(texture.getType().glType(), 0, GL_RGBA, GL_UNSIGNED_BYTE, textureDataResult);
				glDisable(texture.getType().glType());
				
				return textureDataResult;
			}
		} catch (LWJGLException e) {}
		
		while(gettingTextureData) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		while(prossessing) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		
		textureDataResult = BufferUtils.createByteBuffer(
				texture.getWidth() * texture.getHeight() * texture.getDepth() * 4);
		
		this.textureToGetData = texture;
		this.gettingTextureData = true;
		
		while(gettingTextureData) {try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}}
		return textureDataResult;
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