package com.GameName.Command.Commands;

import com.GameName.Command.Command;
import com.GameName.Engine.GameEngine;
import com.GameName.Util.Vectors.Vector3f;

public class TeleportPlayerCommand extends Command {

	public TeleportPlayerCommand(GameEngine eng) {
		super(eng, "tp");
	}

	public boolean action(String... parm) {
		if(parm.length < 3) return false;
		
		Vector3f playerPosition = ENGINE.getPlayer().getPosition();
		for(int i = 0; i < parm.length; i ++) {
			if(parm[i].startsWith("x")) parm[i] = playerPosition.getX() + parm[i].substring(1);
			if(parm[i].startsWith("y")) parm[i] = playerPosition.getY() + parm[i].substring(1);
			if(parm[i].startsWith("z")) parm[i] = playerPosition.getZ() + parm[i].substring(1);
		}
		
		Vector3f worldSize = ENGINE.getPlayer().getCurrentWorld().getSizeAsVector();
		Vector3f tpPos = new Vector3f(Float.parseFloat(parm[0]), Float.parseFloat(parm[1]), Float.parseFloat(parm[2]));
		
		if(tpPos.greaterThen(worldSize)) return false;
		if(tpPos.lessThen(0)) return false;
		
		ENGINE.getPlayer().setPosition(tpPos);
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
