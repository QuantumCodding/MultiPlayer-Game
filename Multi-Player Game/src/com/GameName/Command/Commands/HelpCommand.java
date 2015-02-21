package com.GameName.Command.Commands;

import com.GameName.Command.Command;
import com.GameName.Console.Base.Logger;
import com.GameName.Engine.GameEngine;
import com.GameName.Engine.Registries.CommandRegistry;

public class HelpCommand extends Command {

	public HelpCommand(GameEngine eng) {
		super(eng, "help");
	}

	public boolean action(String... parm) {
		if(parm.length == 0) {
			
			boolean swap = false;
			for(Command command : CommandRegistry.getCommands()) {
				if(swap)
					Logger.addLine(command.getName() + " " + command.getInfo());
				else
					Logger.addLine(command.getName() + " " +  command.getInfo());
				swap = !swap;
			}
			
			Logger.addLine("");
			return true;
		} else {
			for(Command command : CommandRegistry.getCommands()) {
				if(command.getName().equals(parm[0])) {
					Logger.addLine("<b>"+command.getName()+":</b>" +
							command.getDescription() + "\n"
						);
					
					Logger.addLine("\n" + command.getParm());;
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
