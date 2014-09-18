package com.GameName.Main.Threads;

import java.util.ArrayList;

import com.GameName.Util.Registry;

public class ThreadRegistry extends Registry<GameThread> {
	private static GameThread[] threads;
	
	public static void register() {
		ArrayList<GameThread> unregisteredThreads = new ArrayList<GameThread>();
		
		for(Registry<?> reg : getRegistries()) {
			for(GameThread thread : (GameThread[]) reg.toArray()) {
				unregisteredThreads.add(thread);
			}
		}
		
		getRegistries().clear();
		threads = unregisteredThreads.toArray(new GameThread[unregisteredThreads.size()]);
				
		isConcluded = true;
	}
	
	public static GameThread[] getSounds() {
		return threads;
	}
	
	public void addThread(GameThread thread) {
		register(thread);
	}
}
