package com.GameName.Input;

import net.java.games.input.ControllerEvent;
import net.java.games.input.ControllerListener;

import org.lwjgl.input.Controllers;

public class ControllerHandler implements ControllerListener {
	
	public void controllerAdded(ControllerEvent arg0) {
		Controllers.poll();
//		GameName.c = Controllers.getController(0);
	}

	public void controllerRemoved(ControllerEvent arg0) {
//		GameName.c = null;
	}

}
