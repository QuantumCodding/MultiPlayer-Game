package com.GameName.Render.Window;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.GameName.Engine.GameEngine;
import com.GameName.Render.Effects.Texture;

public class Window {
	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 600;
		
	private String title;
	private ByteBuffer[] icon;
	private Texture splash;
	
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
		return glGetString(GL_VERSION);
	}
	
	public static String getGLSLVersion() {
		return glGetString(GL_SHADING_LANGUAGE_VERSION);
	}
	
	public void setSplash(Texture splash) {
		this.splash = splash;
	}
	
	public void drawSplash() {
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
		splash.bind();	
		
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
