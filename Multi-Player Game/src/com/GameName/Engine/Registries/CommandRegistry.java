package com.GameName.Engine.Registries;

import java.util.ArrayList;

import com.GameName.Command.Command;

public class CommandRegistry {
	private static Command[] commands;
	private static ArrayList<Command> unregisteredCommands;
	
	static {
		unregisteredCommands = new ArrayList<Command>();
	}
	
	public static void registerCommand(Command command) {
		unregisteredCommands.add(command);
	}	
	
	public static void conclude() {
		commands = unregisteredCommands.toArray(new Command[unregisteredCommands.size()]);
		
		unregisteredCommands.clear();
		unregisteredCommands = null;
	}	
	
	public static Command[] getCommands() {
		return commands;
	}
	
	public static Command accessByName(String name) {
		for(Command command : getCommands()) {
			if(command.getName().equals(name)) {
				return command;
			}
		}
		
		return null;
	}
	
	public static Command getCommand(int index) {
		return getCommands()[index];
	}
}
