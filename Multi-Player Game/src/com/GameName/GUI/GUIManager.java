package com.GameName.GUI;

import com.GameName.GUI.GUI;

public class GUIManager {
	public GUIManager() {
		
	}
	
	public void render() {
		for(GUI gui : GuiRegistry.getGuis()) {
			if(gui.isOpen()) {
				gui.render();
				
			}
		}
	}
	
	public void update() {
		for(GUI gui : GuiRegistry.getGuis()) {
			if(gui.isOpen()) {
				gui.updateAll();
			}
		}
	}
	
	public void toggle(String name) {
		GUI gui = accessByName(name);
		
		if(gui.isOpen()) {
			gui.close();
		} else {
			gui.open();
		}
	}
	
	public GUI accessByName(String name) {
		for(GUI gui : GuiRegistry.getGuis()) {
			if(gui != null && gui.getName().equals(name)) {
				return gui;
			}
		}
		
		return null;
	}
}
