package com.GameName.Command;

import java.util.ArrayList;

import com.GameName.Util.Registry;
import com.GameName.Util.RegistryStorage;

public class CommandRegistry extends Registry<Command> {
	private static Command[] commands;
	private static RegistryStorage<Command> regstries;
	private static ArrayList<Command> unregisteredCommands;
	
	static {
		regstries = new RegistryStorage<Command>();
		unregisteredCommands = new ArrayList<Command>();
	}
	
	public static Command[] getCommands() {
		return commands;
	}
	
	public void addCommand(Command command) {
		registerOBJ(command);
	}

	public static void register() {regstries.register();}
	
	public static void addRegistry(CommandRegistry reg) {
		regstries.addRegistry(reg);
	}
	
	protected void register(Command e) {
		unregisteredCommands.add(e);
	}
	
	protected void registrtionConcluded() {
		commands = unregisteredCommands.toArray(new Command[unregisteredCommands.size()]);
		
		unregisteredCommands.clear();
		unregisteredCommands = null;
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
