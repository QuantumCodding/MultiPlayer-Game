package com.GameName.Render.GUI;

public class MultiGUI_HUD extends Multi_GUI {

	void fillGUIs() {
		guis = new GUI[] { 
			new GUI_UtilityBar(),
			new GUI_StatusBar(),
			new GUI_PowerBar(),
			new GUI_Money(),
		};
	}

}
