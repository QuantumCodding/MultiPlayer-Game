package com.GameName.Command.Commands;

import com.GameName.Command.Command;
import com.GameName.Engine.GameEngine;
import com.GameName.Engine.Registries.CommandRegistry;
import com.GameName.Main.Debugging.Logger;

public class HelpCommand extends Command {

	public HelpCommand(GameEngine eng) {
		super(eng, "help");
	}

	public boolean action(String... parm) {
		if(parm.length == 0) {
			
			boolean swap = false;
			for(Command command : CommandRegistry.getCommands()) {
				if(swap)
					Logger.print(command.getName() + " " + command.getInfo()).setType("").end();
				else
					Logger.print(command.getName() + " " +  command.getInfo()).setType("").setBold().end();
				swap = !swap;
			}
			
			Logger.print("").setType("").end();
			return true;
		} else {
			for(Command command : CommandRegistry.getCommands()) {
				if(command.getName().equals(parm[0])) {
					Logger.print("<b>"+command.getName()+":</b>" +
							command.getDescription() + "\n"
						).setType("").end();
					
					Logger.print("\n" + command.getParm()).setType("Parm").end();
					return true;
				}
			}
			
			return false;
		}
		
	}

	public String getInfo() {
		return "Gives a discription about a command";
	}

	public String getDescription() {
		return "Lists all commands or gives specific information about a command";
	}

	public String getParm() {
		return "Command: The command to give information about";
	}

}
