package com.GameName.Render.Types;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER_BINDING;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector2f;

import com.GameName.Engine.GameEngine;
import com.GameName.Render.Effects.ShaderRegistry;
import com.GameName.Util.BufferUtil;

public abstract class Render2D extends Renderable {
	private Vector2f pos;
	private float width, height;
	
	public Render2D(GameEngine eng, float x, float y, float width, float height) {
		this(eng, new Vector2f(x, y), width, height);
	}
	
	public Render2D(GameEngine eng, Vector2f pos, float width, float height) {
		super(eng);
		
		this.pos = pos;
		this.width = width;
		this.height = height;
		
		setShader(ShaderRegistry.accessByName("BasicRender2DShader"));
	}
	
	public void draw() {		
		renderBackground();
		renderForground();
	}
	
	protected abstract void renderForground();
	
	private void renderBackground() {		
		if(getTexture() == null && getColor() == null) return;
		
		glPushMatrix();
		
			//Position
			glBindBuffer(GL_ARRAY_BUFFER, vertexVBO);
				glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);		
			
			//Texture
			glBindBuffer(GL_ARRAY_BUFFER, textureVBO);
				glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
						
			glDrawArrays(GL_QUADS, 0, 4);
		
		glPopMatrix();
	}

	public void genVBOids() {
		int[] ids = ENGINE.getGLContext().genBufferIds(3);
		
		vertexVBO = ids[0];
		textureVBO = ids[1];
		colorVBO = ids[2];
		
	}
	
	public void updateVBOs() {
		//Vertices
		List<Float> verties = new ArrayList<Float>();
		
		verties.add(pos.x);			verties.add(pos.y);
		verties.add(pos.x + width);	verties.add(pos.y);
		verties.add(pos.x + width);	verties.add(pos.y + height);
		verties.add(pos.x);			verties.add(pos.y + height);
		
		FloatBuffer vertexBuffer = BufferUtil.createFillipedFloatBuffer(verties);
		ENGINE.getGLContext().addBufferBind(vertexBuffer, GL_ARRAY_BUFFER_BINDING, vertexVBO, GL_STATIC_DRAW, 'f');
		
		//Texture
		List<Float> texCoords = new ArrayList<Float>();
		
		texCoords.add(texCoordsTop.x);			texCoords.add(texCoordsTop.y);
		texCoords.add(texCoordsBottom.x);		texCoords.add(texCoordsTop.y);
		texCoords.add(texCoordsBottom.x);		texCoords.add(texCoordsBottom.y);
		texCoords.add(texCoordsTop.x);			texCoords.add(texCoordsBottom.y);
		
		FloatBuffer texCoordsBuffer = BufferUtil.createFillipedFloatBuffer(texCoords);
		ENGINE.getGLContext().addBufferBind(texCoordsBuffer, GL_ARRAY_BUFFER_BINDING, textureVBO, GL_STATIC_DRAW, 'f');
	}
	
	protected float getX() {
		return pos.x;
	}
	
	protected float getY() {
		return pos.y;
	}
	
	protected float getHeight() {
		return height;
	}
	
	protected float getWidth() {
		return width;
	}	
	
	protected void setX(float x) {
		pos.x = x;
	}
	
	protected void setY(float y) {
		pos.y = y;
	}

	protected void setWidth(float width) {
		this.width = width;
		forceVBOUpdate();
	}

	protected void setHeight(float height) {
		this.height = height;
		forceVBOUpdate();
	}
	
	public void setTexCoordsTop(TexCoord2f texCoordsTop) {
		this.texCoordsTop = texCoordsTop;
		forceVBOUpdate();
	}

	public void setTexCoordsBottom(TexCoord2f texCoordsBottom) {
		this.texCoordsBottom = texCoordsBottom;
		forceVBOUpdate();
	}
}
