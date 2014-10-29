package com.GameName.Main.Threads;

import com.GameName.Input.Control;
import com.GameName.Main.GameName;

public class ClassUpdataThread extends GameThread {

	public ClassUpdataThread(int tickRate) {
		super(tickRate, "Updata Thread");
	}

	void init() {
		
	}

	void tick() {
		Control.tick();
		GameName.guiManager.update();
		GameName.debugWindow.update();
	}

}
