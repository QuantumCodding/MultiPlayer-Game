package com.GameName.Render.Types_2;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import com.GameName.Engine.GameEngine;
import com.GameName.Render.Effects.RenderProperties.RenderPropertiesBuilder;

public abstract class Render2D extends Render {
	public Render2D(GameEngine eng) {
		super(eng); 
		
		setRenderProperties(new RenderPropertiesBuilder().disableDepthTest().disableCullFace().build());
	}

	protected void preformRender() {
		// Vertices
		glBindBuffer(GL_ARRAY_BUFFER, bufferIds[0]);
			glVertexPointer(2, GL_FLOAT, 0, 0);
			//glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);		
		
		// Texture Coordinates
		glBindBuffer(GL_ARRAY_BUFFER, bufferIds[1]);
			glTexCoordPointer(getTexture().getType().getCoordCount(), GL_FLOAT, 0, 0);
			//glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);		
			
		// Color
		glBindBuffer(GL_ARRAY_BUFFER, bufferIds[2]);
			glColorPointer(4, GL_FLOAT, 0, 0);
			//glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);		
			
		// Render
		glDrawArrays(GL_QUADS, 0, bufferIds[3]);	
	}
	
	public void generateBufferIds() {
//		Vertices, TexCoords, Colors, RenderCount
		bufferIds = new int[4];
		
		int[] tempIds = ENGINE.getGLContext().genBufferIds(3);
		
		bufferIds[0] = tempIds[0]; 
		bufferIds[1] = tempIds[1];
		bufferIds[2] = tempIds[2]; 
	}
	
	public void cleanUp() {
		if(bufferIds != null) {
			ENGINE.getGLContext().deleteBuffer(bufferIds[0]);
			ENGINE.getGLContext().deleteBuffer(bufferIds[1]);
			ENGINE.getGLContext().deleteBuffer(bufferIds[2]);
		}
	}
}
