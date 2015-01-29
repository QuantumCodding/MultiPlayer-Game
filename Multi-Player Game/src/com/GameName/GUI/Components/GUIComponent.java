package com.GameName.GUI.Components;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER_BINDING;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.TexCoord2f;

import com.GameName.Engine.GameEngine;
import com.GameName.GUI.GUI;
import com.GameName.Render.Effects.Texture;
import com.GameName.Render.Types.Render2D;
import com.GameName.Util.BufferUtil;

public abstract class GUIComponent extends Render2D {
	private int id;		
	private GUI gui;
	
	private TexCoord2f texCoordsRest1, texCoordsRest2;
	private TexCoord2f texCoordsSelected1, texCoordsSelected2;
	private TexCoord2f texCoordsPressed1, texCoordsPressed2;
	
	private int[] textureBuffers;
	
	protected GUIComponent(GameEngine eng, int id, float x, float y, float width, float height) {
		super(eng, x, y, width, height);
		this.id = id;
	}
	
	protected void renderForground() {
						
		//Position
		glBindBuffer(GL_ARRAY_BUFFER, vertexVBO);
			glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);		
		
		//Texture
		glBindBuffer(GL_ARRAY_BUFFER, 
				textureBuffers[isSelected() ? ENGINE.getPlayer().isPointerDown() ? 2 : 1 : 0]);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		

		glDrawArrays(GL_QUADS, 0, 4);
	}
	
	protected void genVBO() {
		int[] ids = ENGINE.getGLContext().genBufferIds(4);
		
		vertexVBO = ids[0];
		
		textureBuffers[0] = ids[1];
		textureBuffers[1] = ids[2];
		textureBuffers[2] = ids[3];
	}
	
	public void updateVBOs() {
		List<Float> verties = new ArrayList<Float>();
		
		verties.add(getX());				verties.add(getY());
		verties.add(getX() + getWidth());	verties.add(getY());
		verties.add(getX() + getWidth());	verties.add(getY() + getHeight());
		verties.add(getX());				verties.add(getY() + getHeight());
		
		FloatBuffer vertexBuffer = BufferUtil.createFillipedFloatBuffer(verties);
		ENGINE.getGLContext().addBufferBind(vertexBuffer, GL_ARRAY_BUFFER_BINDING, vertexVBO, GL_STATIC_DRAW, 'f');
		
		TexCoord2f[] textureCoords = new TexCoord2f[] 
				{texCoordsRest1, texCoordsRest2, texCoordsSelected1, texCoordsSelected2, texCoordsPressed1, texCoordsPressed2};
		
		for(int i = 0; i < textureCoords.length; i += 2) {
			ArrayList<Float> texCoords = new ArrayList<Float>();
			
			texCoords.add(textureCoords[i + 1].x);	texCoords.add(textureCoords[i + 1].y);
			texCoords.add(textureCoords[i].x);		texCoords.add(textureCoords[i + 1].y);
			texCoords.add(textureCoords[i].x);		texCoords.add(textureCoords[i].y);
			texCoords.add(textureCoords[i + 1].x);	texCoords.add(textureCoords[i].y);
			
			FloatBuffer texCoordsBuffer = BufferUtil.createFillipedFloatBuffer(texCoords);
			ENGINE.getGLContext().addBufferBind(texCoordsBuffer, GL_ARRAY_BUFFER_BINDING, textureBuffers[i / 2], GL_STATIC_DRAW, 'f');
		}
	}
	
	public boolean isSelected() {
		if(ENGINE.getPlayer().getPointerPos().x > getX() && 
				ENGINE.getPlayer().getPointerPos().x < getX() + getWidth()) {
			
			if(ENGINE.getPlayer().getPointerPos().y > getY() && 
					ENGINE.getPlayer().getPointerPos().y < getY() + getHeight()) {
				
				return true;
			}
		}
		
		return false;
	}
	
	public void update() {
		if(isSelected() && ENGINE.getPlayer().isPointerDown()) {
			activate();
		}
	}
	
	public void activate() {
		defaultActivate();
	}
	
	public void defaultActivate() {
		if(gui != null) gui.action(id);
	}
	
	public void setTextureRest(Texture texture, TexCoord2f texCoordsRest1, TexCoord2f texCoordsRest2) {
		setTexture(texture);
		
		this.texCoordsRest1 = texCoordsRest1;
		this.texCoordsRest2 = texCoordsRest2;
	}
	
	public void setTextureSelected(Texture texture, TexCoord2f texCoordsSelected1, TexCoord2f texCoordsSelected2) {
		setTexture(texture);
		
		this.texCoordsSelected1 = texCoordsSelected1;
		this.texCoordsSelected2 = texCoordsSelected2;
	}
	
	public void setTexturePressed(Texture texture, TexCoord2f texCoordsPressed1, TexCoord2f texCoordsPressed2) {
		setTexture(texture);
		
		this.texCoordsPressed1 = texCoordsPressed1;
		this.texCoordsPressed2 = texCoordsPressed2;
	}
	
	protected void cleanUp_Renderable() {
		if(textureBuffers != null) {
			glDeleteBuffers(textureBuffers[0]);
			glDeleteBuffers(textureBuffers[1]);
			glDeleteBuffers(textureBuffers[2]);
		}
	}
}
