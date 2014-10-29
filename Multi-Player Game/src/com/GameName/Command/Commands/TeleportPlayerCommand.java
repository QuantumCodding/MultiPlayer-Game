package com.GameName.Command.Commands;

import com.GameName.Command.Command;
import com.GameName.Main.GameName;
import com.GameName.Util.Vectors.Vector3f;

public class TeleportPlayerCommand extends Command {

	public TeleportPlayerCommand() {
		super("tp");
	}

	public boolean action(String... parm) {
		if(parm.length < 3) return false;
		
		Vector3f worldSize = GameName.player.getAccess().getCurrentWorld().getSizeAsVector();
		Vector3f tpPos = new Vector3f(Integer.parseInt(parm[0]), Integer.parseInt(parm[1]), Integer.parseInt(parm[2]));
		
		if(tpPos.greaterThen(worldSize)) return false;
		if(tpPos.lessThen(0)) return false;
		
		GameName.player.getAccess().setPos(tpPos);
		return true;
	}

	public String getInfo() {
		return "Teleports the player to a specified location";
	}

	public String getDescription() {
		return getInfo();
	}

	public String getParm() {
		return  "X: the x position to teleport to \n" + 
				"Y: the y position to teleport to \n" +
				"Z: the z position to teleport to ";
	}

}
