package com.GameName.Render.Types;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import com.GameName.Engine.GameEngine;
import com.GameName.Render.Effects.ShaderRegistry;

public abstract class Render3D extends Renderable {
	protected int normalVBO;
	protected int vertexCount;
	
	public Render3D(GameEngine eng) {
		super(eng);
		
		normalVBO = -1;
		setShader(ShaderRegistry.accessByName("BasicRender3DShader"));
	}
	
	public void draw() {
				
		glBindBuffer(GL_ARRAY_BUFFER, vertexVBO);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);		
			
		glBindBuffer(GL_ARRAY_BUFFER, textureVBO);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);		
			
		glBindBuffer(GL_ARRAY_BUFFER, colorVBO);
			glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);		
			
		glBindBuffer(GL_ARRAY_BUFFER, normalVBO);
			glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0);
			
		glDrawArrays(GL_QUADS, 0, vertexCount);	
	}
	
	protected void cleanUp_Renderable() {
		cleanUp_Render3D();
		
		if(normalVBO != -1) {
			ENGINE.getGLContext().deleteBuffer(normalVBO);
		}
	}
	
	protected abstract void cleanUp_Render3D();
}
