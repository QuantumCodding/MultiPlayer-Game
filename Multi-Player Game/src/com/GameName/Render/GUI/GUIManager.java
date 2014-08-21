package com.GameName.Render.GUI;

public class GUIManager {
	public GUI[] guiList;
	
	public GUIManager() {
		guiList = new GUI[]{
			new GUI_Test(),
			new GUI_Music(),
			new GUI_Pause(),
			new MultiGUI_HUD(),
			new GUI_ControlConfig(),
			new GUI_AddControl(),
		};		
	}
	
	public void render() {
		for(GUI gui : guiList) {
			if(gui.isOpen()) {
				gui.render();
				
			}
		}
	}
	
	public void resize(int orgW, int orgH) {
		for(GUI gui : guiList) {
			gui.updateSize(orgW, orgH);
		}
	}
	
	public void update() {
		for(GUI gui : guiList) {
			if(gui.isOpen()) {
				gui.updateAll();
			}
		}
	}
	
	public void toggle(String name) {
		GUI gui = accessByName(name);
		
		if(gui.isOpen) {
			gui.close();
		} else {
			gui.open();
		}
	}
	
	public GUI accessByName(String name) {
		switch(name) {
			case "Test": return guiList[0];
			case "Music": return guiList[1];
			case "Pause": return guiList[2];
			case "Main HUD": return guiList[3];
			case "Controls Config": return guiList[4];
			case "Add Control": return guiList[5];
			default: return null;
		}
	}
}
