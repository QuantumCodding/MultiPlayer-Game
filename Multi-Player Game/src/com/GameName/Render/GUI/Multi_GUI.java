package com.GameName.Render.GUI;

public abstract class Multi_GUI extends GUI {
	protected GUI[] guis;
	
	public Multi_GUI() {
		fillGUIs();
	}
	
	public void open() {
		isOpen = true;
		
		for(GUI gui : guis) {
			gui.open();
		}
	}
	
	public void close() {
		isOpen = false;
		
		for(GUI gui : guis) {
			gui.close();
		}
	}
	
	public void render() {
		for(GUI gui : guis) {
			gui.render();
		}
	}

	public void updateSize(int orgX, int orgY) {}

	public void updateAll() {
		for(GUI gui : guis) {
			gui.updateAll();
		}
	}

	protected void actions(int id) {}
	
	abstract void fillGUIs();
}
