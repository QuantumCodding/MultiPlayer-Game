package com.GameName.Console;

import com.GameName.Command.Command;
import com.GameName.Console.Base.BasicLog;
import com.GameName.Engine.GameEngine;

public class GameNameLogTab extends BasicLog {
	private static final long serialVersionUID = -1430686549960754649L;
	private final GameEngine ENGINE;
	
	public GameNameLogTab(GameEngine eng) {
		super("Log", createImageIcon("res/textures/Console/LogsIcon.png"));
		ENGINE = eng;
	}

	public boolean evaluateInput(String input) {
		if(!input.startsWith("/")) {
			String[] parm = input.split(" ");
	
			switch(parm[0].toLowerCase()) {
				case "clear": 		
					clearLog();		
				return true;
				
				case "toggletimestamp": setUseTimeStamp(!usingTimeStamp()); return true;
				case "ping": addLine("Pong!"); return true;
				case "exit": case "quit": case "stop": ENGINE.getGameName().stop(); return true;
					
				default: return false;
			}
			
		}
		
		return Command.runCommand(input);
	}
}
