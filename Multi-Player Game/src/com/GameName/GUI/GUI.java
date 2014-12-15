package com.GameName.GUI;

import com.GameName.Engine.GameEngine;
import com.GameName.GUI.Components.GUIComponent;
import com.GameName.Render.Effects.Texture;
import com.GameName.Render.Types.Render2D;

public abstract class GUI extends Render2D {
	private final String GUI_TEXTURE_ROOT_DIR = "res/textures/gui/";
		
	protected Texture[] textures;
	private GUIComponent[] components;
	private boolean isOpen;
	private String name;
	
	protected GUI(GameEngine eng, String name, int textureCount, float x, float y, float width, float height, String textureType) {
		super(eng, x, y, width, height);
		
		this.name = name;
		
		textures = new Texture[textureCount];
		loadTextures(textureType);
	}	

	public void open() {
		ENGINE.getRender().add(this);
		isOpen = true;
	}
	
	public void close() {
		ENGINE.getRender().remove(this);
		isOpen = false;
	}
	
	public abstract void render();
	
	public void renderForground() {
		render();
		
		for(GUIComponent c : components) {
			c.render();
		}
	}
	
	public abstract void update();
	
	public void updateAll() {
		update();
		
		for(GUIComponent comp : components) {
			comp.update();
		}
	}
	
	public abstract void action(int componentId);
	
	private void loadTextures(String type) {
		for(int i = 0; i < textures.length; i ++) {
			textures[i] = new Texture(GUI_TEXTURE_ROOT_DIR + name, false, type);
		}
	}
	
	public String getGUI_TEXTURE_ROOT_DIR() {
		return GUI_TEXTURE_ROOT_DIR;
	}

	public GUIComponent[] getComponents() {
		return components;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public String getName() {
		return name;
	}
}
