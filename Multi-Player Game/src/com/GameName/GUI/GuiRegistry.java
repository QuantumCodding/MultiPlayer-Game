package com.GameName.GUI;

import java.util.ArrayList;

import com.GameName.Util.Registry;
import com.GameName.Util.RegistryStorage;

public class GuiRegistry extends Registry<GUI> {

	private static GUI[] guis;
	private static RegistryStorage<GUI> regstries;
	private static ArrayList<GUI> unregisteredGUIs;
	
	static {
		regstries = new RegistryStorage<GUI>();
		unregisteredGUIs = new ArrayList<GUI>();
	}
	
	public static GUI[] getGuis() {
		return guis;
	}
	
	public void addGui(GUI gui) {
		registerOBJ(gui);
	}

	public static void register() {regstries.register();}
	
	public static void addRegistry(GuiRegistry reg) {
		regstries.addRegistry(reg);
	}
	
	protected void register(GUI e) {
		unregisteredGUIs.add(e);
	}
	
	protected void registrtionConcluded() {
		guis = unregisteredGUIs.toArray(new GUI[unregisteredGUIs.size()]);
		
		unregisteredGUIs.clear();
		unregisteredGUIs = null;
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
}
