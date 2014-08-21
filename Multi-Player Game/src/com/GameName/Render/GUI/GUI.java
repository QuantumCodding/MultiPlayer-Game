package com.GameName.Render.GUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public abstract class GUI {
	protected GUIComponent[] comp;	
	protected boolean isOpen;
	
	public abstract void updateSize(int orgX, int orgY);
	
	public void render() {
		for(GUIComponent c : comp) {
			c.render();
		}
	}
	
	public void open() {
		isOpen = true;
	}
	
	public void close() {
		isOpen = false;
	}
	
	public boolean isOpen() {
		return isOpen;
	}
	
	public GUIComponent[] getComponents() {return comp;}
	public abstract void updateAll();
	
	protected abstract void actions(int id);
	
	protected Texture getTexture(String key) {
		try {
			return TextureLoader.getTexture("png", new FileInputStream(new File("res/textures/" + key + ".png")));
		} catch (IOException e) {
			System.out.println("The texture " + key + " was not found or successfully loaded");
			e.printStackTrace();
		}
		
		return null;
	}
}
