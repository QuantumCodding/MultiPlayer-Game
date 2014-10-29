package com.GameName.Command.Commands;

import com.GameName.Command.Command;
import com.GameName.Main.GameName;
import com.GameName.World.WorldRegistry;

public class ForceVBOUpdateCommand extends Command {

	public ForceVBOUpdateCommand() {
		super("ForceVBOUpdate");
	}

	public boolean action(String... parm) {
		if(parm.length > 0) {		
			WorldRegistry.accessByName(parm[0]).forceChunkUpdate();
		} else {
			GameName.player.getAccess().getCurrentWorld().forceChunkUpdate();
		}
		
		return true;
	}

	public String getInfo() {
		return "Updates the VBO of a specifed world";
	}

	public String getDescription() {
		return getInfo();
	}

	public String getParm() {
		return "World: the world to force the VBOs to update";
	}
}
