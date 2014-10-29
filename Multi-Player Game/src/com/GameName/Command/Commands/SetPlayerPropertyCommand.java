package com.GameName.Command.Commands;

import com.GameName.Command.Command;
import com.GameName.Main.GameName;

public class SetPlayerPropertyCommand extends Command {

	public SetPlayerPropertyCommand() {
		super("set");
	}

	public boolean action(String... parm) {
		if(parm.length < 2) return false;
		
		String affect = parm[0];
		int value = Integer.parseInt(parm[1]);

		switch(affect) {
			case "health": GameName.player.getAccess().setHealth(value); return true;
			case "mana": GameName.player.getAccess().setMana(value); return true;
			case "hunger": GameName.player.getAccess().setHunger(value); return true;
			
			case "maxHealth": GameName.player.getAccess().setMaxHealth(value); return true;
			case "maxMana": GameName.player.getAccess().setMaxMana(value); return true;
			case "maxHunger": GameName.player.getAccess().setMaxHunger(value); return true;
			
			case "reset": GameName.player.resetPlayer(); return true;
			
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
