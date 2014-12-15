package com.GameName.Render.Model;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.GameName.Engine.GameEngine;
import com.GameName.Render.Types.Renderable;
import com.GameName.Util.BufferUtil;
import com.GameName.Util.Vectors.Vector2f;
import com.GameName.Util.Vectors.Vector3f;

public class ModelRender extends Renderable {
	private String name;
	
	private ModelData modelData;
	private ArrayList<Animation> animations;
	private ArrayList<Animation> animationsPlaying;
	
	private ArrayList<Vector3f[]> effects;

	private int[] vertexVBOs;
	private int[] textureVBOs;
	private int[] normalVBOs;
	
	public ModelRender(GameEngine eng, ModelData modelData, String name) {
		super(eng);
		
		this.modelData = modelData;
		animations = new ArrayList<Animation>();
		this.name = name;
	}
	
	public void step() {
		for(int i = 0; i < animationsPlaying.size(); i ++) {
			Animation animation = animationsPlaying.get(i);
			
			if(animation.step()) {
				updatePartEffects(animation.getAffectedFaces(), animation.getEffects());
			} else {
				animationsPlaying.remove(i);
			}
		}
	}

	public void draw() {
		for(int i = 0; i < vertexVBOs.length; i ++) {
			Vector3f[] effect = effects.get(i);
			
			glPushMatrix();
				glTranslatef(effect[0].getX(), effect[0].getY(), effect[0].getZ());
				
				glRotatef(effect[1].getX(), 1, 0, 0);
				glRotatef(effect[1].getY(), 0, 1, 0);
				glRotatef(effect[1].getZ(), 0, 0, 1);
				
				glScalef(effect[2].getX(), effect[2].getY(), effect[2].getZ());
				
				glBindBuffer(GL_ARRAY_BUFFER, vertexVBOs[i]);
        			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);		
        		
	        	glBindBuffer(GL_ARRAY_BUFFER, textureVBOs[i]);
	        		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
	        		
	        	glBindBuffer(GL_ARRAY_BUFFER, normalVBOs[i]);
	        		glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0);
	
	        	glDrawArrays(GL_QUADS, 0, 3);
				
			glPopMatrix();
		}
	}

	public void updateVBOs() {
		for(int i = 0; i < vertexVBOs.length; i ++) {
			updatePartVBO(i);
		}
	}
	
	public void updatePartsVBO(int... is) {
		for(int i : is) {
			updatePartVBO(i);
		}
	}

	public void updatePartVBO(int i) {
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Float> textureCoords = new ArrayList<Float>();
		ArrayList<Float> normals = new ArrayList<Float>();
		
		Vector3f vertex, normal;
		Vector2f textCoord;
		
		for(Face face : modelData.getFaces()) {
			for(int index : face.getVertexIndices()) {
				vertex = modelData.getVertices().get(index);
				
				vertices.add(vertex.getX());
				vertices.add(vertex.getY());
				vertices.add(vertex.getZ());
			}
			
			for(int index : face.getTextureCoordinateIndices()) {
				textCoord = modelData.getTextureCoordinates().get(index);
				
				textureCoords.add(textCoord.getX());
				textureCoords.add(textCoord.getY());
			}	
			
			for(int index : face.getNormalIndices()) {
				normal = modelData.getNormals().get(index);
				
				normals.add(normal.getX());
				normals.add(normal.getY());
				normals.add(normal.getZ());
			}	
			
			for(int j = 0; j < face.getVertexIndices().length; j ++) {
				effects.add(new Vector3f[] {new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 0)});
			}
		}
	
		FloatBuffer vertexBuffer = BufferUtil.createFillipedFloatBuffer(vertices);
		FloatBuffer textureBuffer = BufferUtil.createFillipedFloatBuffer(textureCoords);
		FloatBuffer normalBuffer = BufferUtil.createFillipedFloatBuffer(normals);
		
		ENGINE.getGLContext()
			.addBufferBind(vertexBuffer, GL_ARRAY_BUFFER, vertexVBOs[i], GL_DYNAMIC_DRAW, 'f');
		
		ENGINE.getGLContext()
			.addBufferBind(textureBuffer, GL_ARRAY_BUFFER, textureVBOs[i], GL_DYNAMIC_DRAW, 'f');
		
		ENGINE.getGLContext()
			.addBufferBind(normalBuffer, GL_ARRAY_BUFFER, normalVBOs[i], GL_DYNAMIC_DRAW, 'f');
	}
	
	public void updatePartEffects(int[] is, Vector3f[] effects) {
		for(int i : is) {
			updatePartEffect(i, effects);
		}
	}
	
	public void updatePartEffect(int i, Vector3f[] effect) {
		Vector3f[] currentEffects = effects.get(i);
		effects.set(i, new Vector3f[] {
				currentEffects[0].add(effect[0]),
				currentEffects[1].add(effect[1]),
				currentEffects[2].add(effect[2]),
		});
	}
	
	public void addAnimation(Animation animation) {
		animations.add(animation);
	}
	
	public void playAnimation(String name) {
		for(Animation animation : animationsPlaying) {
			if(animation.getName().equals(name)) {
				System.err.println("Model " + this.name + " is already playing Animation \"" +  name + "\"");
				return;
			}
		}
		
		for(Animation animation : animations) {
			if(animation.getName().equals(name)) {
				animationsPlaying.add(animation);
				
				return;
			}
		}
	}
	
	public void genVBOids() {
		vertexVBOs = ENGINE.getGLContext().genBufferIds(modelData.getFaces().size());
		textureVBOs = ENGINE.getGLContext().genBufferIds(modelData.getFaces().size());
		normalVBOs = ENGINE.getGLContext().genBufferIds(modelData.getFaces().size());
	}
	
	public String getName() {
		return name;
	}

	protected void cleanUp_Renderable() {
		for(int i = 0; i < modelData.getFaces().size(); i ++) {
			glDeleteBuffers(vertexVBOs[i]);
			glDeleteBuffers(textureVBOs[i]);
			glDeleteBuffers(normalVBOs[i]);
		}
		
		for(Material material : modelData.getMaterials().values()) {
			if(material.getTexture() != null) {
				material.getTexture().cleanUp();
			}
		}
	}	
}
