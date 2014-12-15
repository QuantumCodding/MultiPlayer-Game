package com.GameName.Engine.Registries;

import java.util.ArrayList;

import com.GameName.Engine.Threads.GameThread;

public class ThreadRegistry {
	private static GameThread[] threads;
	private static ArrayList<GameThread> unregisteredGameThreads;
	
	static {
		unregisteredGameThreads = new ArrayList<GameThread>();
	}
	
	public static void registerThread(GameThread reg) {
		unregisteredGameThreads.add(reg);
	}
	
	public static void conclude() {
		int index = 0;
		for(GameThread thread : unregisteredGameThreads) {
			thread.setId(index ++);
		}
		
		threads = unregisteredGameThreads.toArray(new GameThread[unregisteredGameThreads.size()]);
		
		unregisteredGameThreads.clear();
		unregisteredGameThreads = null;
	}
	
	public static GameThread[] getThreads() {
		return threads;
	}
}
