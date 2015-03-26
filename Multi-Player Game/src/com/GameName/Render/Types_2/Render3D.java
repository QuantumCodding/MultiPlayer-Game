package com.GameName.Render.Types_2;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_CCW;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glNormalPointer;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import com.GameName.Engine.GameEngine;
import com.GameName.Render.Effects.RenderProperties.RenderPropertiesBuilder;

public abstract class Render3D extends Render {
	public Render3D(GameEngine eng) { 
		super(eng); 
		
		setRenderProperties(new RenderPropertiesBuilder().enableCullFace(GL_CCW, GL_BACK).enableDepthTest().build());
	}

	public void preformRender() {
		// Vertices
		glBindBuffer(GL_ARRAY_BUFFER, bufferIds[0]);
			glVertexPointer(3, GL_FLOAT, 0, 0); 
//			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);		
		
		// Texture Coordinates
		glBindBuffer(GL_ARRAY_BUFFER, bufferIds[1]);
			glTexCoordPointer(getTexture().getType().getCoordCount(), GL_FLOAT, 0, 0);  
//			glVertexAttribPointer(1, getTexture().getType().getCordCount(), GL_FLOAT, false, 0, 0);		
			
		// Color
		glBindBuffer(GL_ARRAY_BUFFER, bufferIds[2]);
			glColorPointer(4, GL_FLOAT, 0, 0);		
//			glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);		
			
		// Normals
		glBindBuffer(GL_ARRAY_BUFFER, bufferIds[3]);
			glNormalPointer(GL_FLOAT, 0, 0);
//			glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0);
			
		// Render
		glDrawArrays(GL_QUADS, 0, bufferIds[4]);
	}
	
	public void generateBufferIds() {
//		Vertices, TexCoords, Colors, Normals, RenderCount
		bufferIds = new int[5];
		
		int[] tempIds = ENGINE.getGLContext().genBufferIds(4);
		
		bufferIds[0] = tempIds[0]; bufferIds[1] = tempIds[1];
		bufferIds[2] = tempIds[2]; bufferIds[3] = tempIds[3];
	}
	
	public void cleanUp() {
		System.out.println("Cleaning up -> " + this);
		
		ENGINE.getRender().remove(this);
		
		if(bufferIds != null) {
			ENGINE.getGLContext().deleteBuffer(bufferIds[0]);
			ENGINE.getGLContext().deleteBuffer(bufferIds[1]);
			ENGINE.getGLContext().deleteBuffer(bufferIds[2]);
			ENGINE.getGLContext().deleteBuffer(bufferIds[3]);
		}
	}
}
