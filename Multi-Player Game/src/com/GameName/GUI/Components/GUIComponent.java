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

import com.GameName.GUI.GUI;
import com.GameName.Main.GameName;
import com.GameName.Render.Effects.Texture;
import com.GameName.Render.Types.Render2D;
import com.GameName.Util.Util;
import com.GameName.Util.Vectors.Vector2f;

public abstract class GUIComponent extends Render2D {
	private int id;		
	private GUI gui;
	
	private Vector2f texCoordsRest1, texCoordsRest2;
	private Vector2f texCoordsSelected1, texCoordsSelected2;
	private Vector2f texCoordsPressed1, texCoordsPressed2;
	
	private int[] textureBuffers;
	
	protected GUIComponent(int id, float x, float y, float width, float height) {
		super(x, y, width, height);
		this.id = id;
	}
	
	protected void renderForground() {
						
		//Position
		glBindBuffer(GL_ARRAY_BUFFER, vertexVBO);
			glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);		
		
		//Texture
		glBindBuffer(GL_ARRAY_BUFFER, 
				textureBuffers[isSelected() ? GameName.player.getAccess().isPointerDown() ? 2 : 1 : 0]);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		

		glDrawArrays(GL_QUADS, 0, 4);
	}
	
	protected void genVBO() {
		int[] ids = GameName.getGLContext().genBufferIds(4);
		
		vertexVBO = ids[0];
		
		textureBuffers[0] = ids[1];
		textureBuffers[1] = ids[2];
		textureBuffers[2] = ids[3];
	}
	
	protected void updateVBOs() {
		List<Float> verties = new ArrayList<Float>();
		
		verties.add(getX());				verties.add(getY());
		verties.add(getX() + getWidth());	verties.add(getY());
		verties.add(getX() + getWidth());	verties.add(getY() + getHeight());
		verties.add(getX());				verties.add(getY() + getHeight());
		
		FloatBuffer vertexBuffer = Util.createFillipedFloatBuffer(verties);
		GameName.getGLContext().addBufferBind(vertexBuffer, GL_ARRAY_BUFFER_BINDING, vertexVBO, GL_STATIC_DRAW, 'f');
		
		Vector2f[] textureCoords = new Vector2f[] 
				{texCoordsRest1, texCoordsRest2, texCoordsSelected1, texCoordsSelected2, texCoordsPressed1, texCoordsPressed2};
		
		for(int i = 0; i < textureCoords.length; i += 2) {
			ArrayList<Float> texCoords = new ArrayList<Float>();
			
			texCoords.add(textureCoords[i + 1].getX());	texCoords.add(textureCoords[i + 1].getY());
			texCoords.add(textureCoords[i].getX());		texCoords.add(textureCoords[i + 1].getY());
			texCoords.add(textureCoords[i].getX());		texCoords.add(textureCoords[i].getY());
			texCoords.add(textureCoords[i + 1].getX());	texCoords.add(textureCoords[i].getY());
			
			FloatBuffer texCoordsBuffer = Util.createFillipedFloatBuffer(texCoords);
			GameName.getGLContext().addBufferBind(texCoordsBuffer, GL_ARRAY_BUFFER_BINDING, textureBuffers[i / 2], GL_STATIC_DRAW, 'f');
		}
	}
	
	public boolean isSelected() {
		if(GameName.player.getAccess().getPointer().getX() > getX() && 
				GameName.player.getAccess().getPointer().getX() < getX() + getWidth()) {
			
			if(GameName.player.getAccess().getPointer().getY() > getY() && 
					GameName.player.getAccess().getPointer().getY() < getY() + getHeight()) {
				
				return true;
			}
		}
		
		return false;
	}
	
	public void update() {
		if(isSelected() && GameName.player.getAccess().isPointerDown()) {
			activate();
		}
	}
	
	public void activate() {
		defaultActivate();
	}
	
	public void defaultActivate() {
		if(gui != null) gui.action(id);
	}
	
	public void setTextureRest(Texture texture, Vector2f texCoordsRest1, Vector2f texCoordsRest2) {
		setTexture(texture);
		
		this.texCoordsRest1 = texCoordsRest1;
		this.texCoordsRest2 = texCoordsRest2;
	}
	
	public void setTextureSelected(Texture texture, Vector2f texCoordsSelected1, Vector2f texCoordsSelected2) {
		setTexture(texture);
		
		this.texCoordsSelected1 = texCoordsSelected1;
		this.texCoordsSelected2 = texCoordsSelected2;
	}
	
	public void setTexturePressed(Texture texture, Vector2f texCoordsPressed1, Vector2f texCoordsPressed2) {
		setTexture(texture);
		
		this.texCoordsPressed1 = texCoordsPressed1;
		this.texCoordsPressed2 = texCoordsPressed2;
	}
	
	protected void cleanUp_() {
		if(textureBuffers != null) {
			glDeleteBuffers(textureBuffers[0]);
			glDeleteBuffers(textureBuffers[1]);
			glDeleteBuffers(textureBuffers[2]);
		}
	}
}
