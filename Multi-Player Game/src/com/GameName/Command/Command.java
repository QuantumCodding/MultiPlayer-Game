package com.GameName.Command;

import com.GameName.Engine.GameEngine;
import com.GameName.Engine.Registries.CommandRegistry;

public abstract class Command {
	protected final GameEngine ENGINE;
	private String name;
	
	public Command(GameEngine eng, String name) {
		this.name = name;
		ENGINE = eng;
	}
	
	public abstract boolean action(String... parm);

	public abstract String getInfo();
	public abstract String getDescription();
	public abstract String getParm();
	
	public static boolean runCommand(String commandString) {
		if(!commandString.startsWith("/")) return false;
		commandString = commandString.substring(1);
				
		String[] info = commandString.split(" ");
		Command command = CommandRegistry.accessByName(info[0]);
		
		if(command == null) return false;
		
		String[] parm = new String[info.length - 1];
		for(int i = 1; i < info.length; i ++) {
			parm[i - 1] = info[i];
		}
		
		return command.action(parm);
	}
	
	public String getName() {
		return name;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Command)) return false;
		
		Command other = (Command) obj;
		
		if (name == null) {
			if (other.name != null) {
				return false;
			}
			
		} else if (!name.equals(other.name)) {
			return false;
		}
		
		return true;
	}
}
