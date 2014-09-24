package com.GameName.Render.Types;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;

import com.GameName.Util.Vectors.Vector3f;

public class RenderModel extends Renderable {
	private int[] partVBO;
	private Vector3f[] translation;
	private Vector3f[] rotation;
	
	public RenderModel(int numParts) {
		partVBO = new int[numParts];
		translation = new Vector3f[numParts];
		rotation = new Vector3f[numParts];
	}
	
	public void rotate(int part, int axis, float amount) {
		if(rotation[part] == null)
			rotation[part] = new Vector3f(0, 0, 0);
		
		else if(axis == 0)
			rotation[part].setX( rotation[part].getX() + amount );
		else if(axis == 1)
			rotation[part].setY( rotation[part].getY() + amount );
		else if(axis == 2)
			rotation[part].setZ( rotation[part].getZ() + amount );
	}
	
	public void addPart(int newPart) {
		int[] newPartVBO = new int[partVBO.length + 1];
		System.arraycopy(partVBO, 0, newPartVBO, 0, partVBO.length);
		newPartVBO[partVBO.length] = newPart;
		
		partVBO = newPartVBO;
	}
	
	public void draw() {
		for(int i = 0; i < partVBO.length; i++) {
			glPushMatrix();
				glTranslatef(translation[i].getX(), translation[i].getY(), translation[i].getZ());
		
				glRotatef(rotation[i].getX(), 1, 0, 0);
				glRotatef(rotation[i].getY(), 0, 1, 0);
				glRotatef(rotation[i].getZ(), 0, 0, 1);
		
				
			glPopMatrix();
		}
	}

	@Deprecated
	public void updateVBOs() {}
	
	@Deprecated
	public void genVBOids() {}

	protected void cleanUp_() {
		for(int i : partVBO) {
			glDeleteBuffers(i);
		}
	}
}

