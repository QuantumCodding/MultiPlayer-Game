package com.GameName.Render.Types;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import com.GameName.Engine.GameEngine;
import com.GameName.Render.Effects.Texture;
import com.GameName.Util.Vectors.Vector2f;

public class RenderImage extends Render2D {
	private Texture image;
	
	public RenderImage(GameEngine eng, float x, float y, float width, float height, String location, String imageType) {
		this(eng, x, y, width, height, 
				eng.getGLContext().genTexture(location, imageType, false)
			);
	}
	
	public RenderImage(GameEngine eng, float x, float y, float width, float height, Texture image) {
		super(eng, x, y, width, height);
		this.image = image;
		
		setColor(null);
		setTexture(null);
		
		setTexCoordsTop(new Vector2f(0, 0));
		setTexCoordsBottom(new Vector2f(1, 1));
	}

	protected void renderForground() {
		glPushMatrix();	
			image.bind();
		
			glColor3f(1, 1, 1);
			glTranslatef(getX(), getY(), 0);
		
			glBegin(GL_QUADS);			
				glTexCoord2f(texCoordsTop.x, texCoordsTop.y);
				glVertex2f(0, 0);
				
				glTexCoord2f(texCoordsBottom.x, texCoordsTop.y);
				glVertex2f(getWidth(), 0);
				
				glTexCoord2f(texCoordsBottom.x, texCoordsBottom.y);
				glVertex2f(getWidth(), getHeight());
				
				glTexCoord2f(texCoordsTop.x, texCoordsBottom.y);
				glVertex2f(0, getHeight());			
			glEnd();
			
		glPopMatrix();
	}

	protected void cleanUp_Renderable() {
		image.cleanUp();
	}
}
