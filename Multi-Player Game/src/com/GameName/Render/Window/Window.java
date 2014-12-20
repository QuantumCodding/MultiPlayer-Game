package com.GameName.Render.Window;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.GameName.Engine.GameEngine;
import com.GameName.Render.Effects.Texture;

public class Window {
	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 600;
		
	private String title;
	private ByteBuffer[] icon;
	
	public Window() {}
	
	public void initDisplay() throws LWJGLException {initDisplay(DEFAULT_WIDTH, DEFAULT_HEIGHT);}
	public void initDisplay(int width, int height) throws LWJGLException {
		if(Display.isCreated()) { destroy(); }
		
		Display.setDisplayMode(new DisplayMode(width, height));
		Display.setTitle(title);
		Display.setIcon(icon);
		
		Display.create();
		AL.create();
		Mouse.create();
		Keyboard.create();
		Controllers.create();
	}
	
	public void setupOpenGL(GameEngine ENGINE) {
		ENGINE.getRender().setUpOpenGL();
	}
	
	public void destroy() {
		Display.destroy();
		AL.destroy();
		Mouse.destroy();
		Keyboard.destroy();
		Controllers.destroy();
	}

	public void setFullscreen(boolean fullscreen) throws LWJGLException {Display.setFullscreen(fullscreen);}
	public void setTitle(String title) {this.title = title;}
	public void setIcon(BufferedImage image) {
		icon = IconLoader.load(image);
	}
	
	public boolean isCloseRequested() {return Display.isCloseRequested();}
	public boolean isFullscreen() {return Display.isFullscreen();}	
	public int getWidth() {return Display.getWidth();}
	public int getHeight() {return Display.getHeight();}
	
	public static String getOpenGLVersion() {
		return GL11.glGetString(GL11.GL_VERSION);
	}
	
	public void drawImage(Texture texture) {
		glMatrixMode(GL_PROJECTION);
	        glLoadIdentity();
			glOrtho(0, getWidth(), getHeight(), 0, 1, -1);	 
		glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();	
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);
        glDisable(GL_RESCALE_NORMAL);
			
		glEnable(GL_TEXTURE_2D); 
		texture.bind();	
		
		glBegin(GL_QUADS);		
			glTexCoord2f(0f, 0f);
			glVertex3f(0, 0, -1);
			
			glTexCoord2f(1f, 0f);
			glVertex3f(getWidth(), 0, -1);
			
			glTexCoord2f(1f, 1f);
			glVertex3f(getWidth(), getHeight(), -1);
			
			glTexCoord2f(0f, 1f);
			glVertex3f(0, getHeight(), -1);			
		glEnd();		

		Texture.unbind();
		glDisable(GL_TEXTURE_2D); 
        glEnable(GL_RESCALE_NORMAL);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
		
		Display.update();
	}
}
