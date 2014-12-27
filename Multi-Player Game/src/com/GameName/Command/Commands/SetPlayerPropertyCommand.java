package com.GameName.Command.Commands;

import com.GameName.Command.Command;
import com.GameName.Engine.GameEngine;

public class SetPlayerPropertyCommand extends Command {

	public SetPlayerPropertyCommand(GameEngine eng) {
		super(eng, "set");
	}

	public boolean action(String... parm) {
		if(parm.length < 2 && !parm[0].equals("reset")) return false;		
		String affect = parm[0];

		switch(affect) {
			case "health": ENGINE.getPlayer().getAccess().setHealth(Integer.parseInt(parm[1])); return true;
			case "mana": ENGINE.getPlayer().getAccess().setMana(Integer.parseInt(parm[1])); return true;
			case "hunger": ENGINE.getPlayer().getAccess().setHunger(Integer.parseInt(parm[1])); return true;
			
			case "maxHealth": ENGINE.getPlayer().getAccess().setMaxHealth(Integer.parseInt(parm[1])); return true;
			case "maxMana": ENGINE.getPlayer().getAccess().setMaxMana(Integer.parseInt(parm[1])); return true;
			case "maxHunger": ENGINE.getPlayer().getAccess().setMaxHunger(Integer.parseInt(parm[1])); return true;
			
			case "reset": ENGINE.getPlayer().resetPlayer(); return true;
			case "noClip": ENGINE.getPlayer().getAccess().setNoClip(Boolean.parseBoolean(parm[1])); return true;
			
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
