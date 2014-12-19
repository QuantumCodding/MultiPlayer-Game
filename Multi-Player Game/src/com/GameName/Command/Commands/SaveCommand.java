package com.GameName.Command.Commands;

import com.GameName.Command.Command;
import com.GameName.Engine.GameEngine;
import com.GameName.Engine.Registries.WorldRegistry;
import com.GameName.World.World;

public class SaveCommand extends Command {

	public SaveCommand(GameEngine eng) {
		super(eng, "save");
	}

	public boolean action(String... parm) {
		if(parm.length == 0 || !parm[0].equals("chunk") || !parm[0].equals("all") || !parm[0].equals("world")) 
			return false;
		
		switch(parm[0]) {
			case "chunk": ENGINE.getWorld().getChunk(
				Integer.parseInt(parm[1]), Integer.parseInt(parm[2]), Integer.parseInt(parm[3]));
			return true;
				
			case "all": 
				for(World world : WorldRegistry.getWorlds()) {
					world.saveWorld();
				} 
			return true;
				
			case "world": 
				if(parm.length == 1) {
					ENGINE.getWorld().saveWorld();
				} else {
					try{ WorldRegistry.accessByName(parm[1]); } catch(Exception e) {return false;}
				}				
			return true;
			
			default: return false;
		}
	}

	public String getInfo() {
		return "Saves a specified Chunk or World";
	}

	public String getDescription() {
		return getInfo();
	}

	public String getParm() {
		return "Type chunk, world, or all followed by the correct parameters\n"
			 + "Chunk: \n"
			 + "     X: The x position of the chunk you want to save \n"
			 + "     Y: The y position of the chunk you want to save \n"
			 + "     Z: The z position of the chunk you want to save \n"
			 + "World: The name of the world you want to save (none for the current world) \n"
			 + "All: Save all worlds";
	}
}
