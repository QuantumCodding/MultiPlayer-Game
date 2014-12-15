package com.GameName.Render.Window;

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
	
	public String getOpenGLVersion() {
		return GL11.glGetString(GL11.GL_VERSION);
	}
}
