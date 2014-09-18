package com.GameName.Render.GUI;

import java.util.ArrayList;

import com.GameName.Audio.Sound;
import com.GameName.Util.Registry;

public class GuiRegistry extends Registry<GUI> {

	private static GUI[] guis;
	
	public static void register() {
		ArrayList<GUI> unregisteredGUIs = new ArrayList<GUI>();
		
		for(Registry<?> reg : getRegistries()) {
			for(GUI gui : (GUI[]) reg.toArray()) {
				unregisteredGUIs.add(gui);
			}
		}
		
		getRegistries().clear();
		guis = unregisteredGUIs.toArray(new GUI[unregisteredGUIs.size()]);
				
		isConcluded = true;
	}
	
	public static GUI accessByName(String name) {
		for(GUI gui : getGuis()) {
			if(gui.getName().equals(name)) {
				return gui;
			}
		}
		
		return null;
	}
	
	public static GUI getSound(int index) {
		return getGuis()[index];
	}
	
	public static GUI[] getGuis() {
		return guis;
	}
	
	public void addGUI(GUI gui) {
		register(gui);
	}	
}
