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

import com.GameName.Main.GameName;
import com.GameName.Render.Effects.ShaderRegistry;
import com.GameName.Util.BufferUtil;
import com.GameName.Util.Vectors.Vector2f;

public abstract class Render2D extends Renderable {
	private Vector2f pos;
	private float width, height;
	
	public Render2D(float x, float y, float width, float height) {
		this(new Vector2f(x, y), width, height);
	}
	
	public Render2D(Vector2f pos, float width, float height) {
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
		int[] ids = GameName.getGLContext().genBufferIds(3);
		
		vertexVBO = ids[0];
		textureVBO = ids[1];
		colorVBO = ids[2];
		
	}
	
	public void updateVBOs() {
		//Vertices
		List<Float> verties = new ArrayList<Float>();
		
		verties.add(pos.getX());			verties.add(pos.getY());
		verties.add(pos.getX() + width);	verties.add(pos.getY());
		verties.add(pos.getX() + width);	verties.add(pos.getY() + height);
		verties.add(pos.getX());			verties.add(pos.getY() + height);
		
		FloatBuffer vertexBuffer = BufferUtil.createFillipedFloatBuffer(verties);
		GameName.getGLContext().addBufferBind(vertexBuffer, GL_ARRAY_BUFFER_BINDING, vertexVBO, GL_STATIC_DRAW, 'f');
		
		//Texture
		List<Float> texCoords = new ArrayList<Float>();
		
		texCoords.add(texCoordsTop.getX());			texCoords.add(texCoordsTop.getY());
		texCoords.add(texCoordsBottom.getX());		texCoords.add(texCoordsTop.getY());
		texCoords.add(texCoordsBottom.getX());		texCoords.add(texCoordsBottom.getY());
		texCoords.add(texCoordsTop.getX());			texCoords.add(texCoordsBottom.getY());
		
		FloatBuffer texCoordsBuffer = BufferUtil.createFillipedFloatBuffer(texCoords);
		GameName.getGLContext().addBufferBind(texCoordsBuffer, GL_ARRAY_BUFFER_BINDING, textureVBO, GL_STATIC_DRAW, 'f');
	}
	
	protected float getX() {
		return pos.getX();
	}
	
	protected float getY() {
		return pos.getY();
	}
	
	protected float getHeight() {
		return height;
	}
	
	protected float getWidth() {
		return width;
	}	
	
	protected void setX(float x) {
		pos.setX(x);
	}
	
	protected void setY(float y) {
		pos.setY(y);
	}

	protected void setWidth(float width) {
		this.width = width;
		forceVBOUpdate();
	}

	protected void setHeight(float height) {
		this.height = height;
		forceVBOUpdate();
	}
	
	public void setTexCoordsTop(Vector2f texCoordsTop) {
		this.texCoordsTop = texCoordsTop;
		forceVBOUpdate();
	}

	public void setTexCoordsBottom(Vector2f texCoordsBottom) {
		this.texCoordsBottom = texCoordsBottom;
		forceVBOUpdate();
	}
}
