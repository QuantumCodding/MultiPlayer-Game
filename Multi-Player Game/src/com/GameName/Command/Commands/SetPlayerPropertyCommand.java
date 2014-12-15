package com.GameName.Command.Commands;

import com.GameName.Command.Command;
import com.GameName.Engine.GameEngine;

public class SetPlayerPropertyCommand extends Command {

	public SetPlayerPropertyCommand(GameEngine eng) {
		super(eng, "set");
	}

	public boolean action(String... parm) {
		if(parm.length < 2) return false;
		
		String affect = parm[0];
		int value = Integer.parseInt(parm[1]);

		switch(affect) {
			case "health": ENGINE.getPlayer().getAccess().setHealth(value); return true;
			case "mana": ENGINE.getPlayer().getAccess().setMana(value); return true;
			case "hunger": ENGINE.getPlayer().getAccess().setHunger(value); return true;
			
			case "maxHealth": ENGINE.getPlayer().getAccess().setMaxHealth(value); return true;
			case "maxMana": ENGINE.getPlayer().getAccess().setMaxMana(value); return true;
			case "maxHunger": ENGINE.getPlayer().getAccess().setMaxHunger(value); return true;
			
			case "reset": ENGINE.getPlayer().resetPlayer(); return true;
			
			default: return false;
		}
	}

	public String getInfo() {
		return "Sets a specified propery of the player to a specified value";
	}

	public String getDescription() {
		return getInfo();
	}

	public String getParm() {
		return  "Veriable: what property of the player will be set\n" + 
				"Value: what value it will be set to";
	}

}
