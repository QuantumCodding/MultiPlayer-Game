package com.GameName.Render;

import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;

public class RenderModel {
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
			rotation[part] = new Vector3f();
		
		else if(axis == 0)
			rotation[part].x = rotation[part].x + amount;
		else if(axis == 1)
			rotation[part].y = rotation[part].y + amount;
		else if(axis == 2)
			rotation[part].z = rotation[part].z + amount;
	}
	
//	public boolean addPart() {
//		
//	}
	
	public void render() {
		for(int i = 0; i < partVBO.length; i++) {
			glPushMatrix();
				glTranslatef(translation[i].x, translation[i].y, translation[i].z);
		
				glRotatef(rotation[i].x, 1, 0, 0);
			glRotatef(rotation[i].y, 0, 1, 0);
			glRotatef(rotation[i].z, 0, 0, 1);
		
		
			glPopMatrix();
		}
	}
}

