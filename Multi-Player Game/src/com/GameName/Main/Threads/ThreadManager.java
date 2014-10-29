package com.GameName.Main.Threads;

import com.GameName.Main.Debugging.DebugWindow;

public class ThreadManager {
	public static final int DEFAULT_TICK_RATE = 60;
	public static final int UNCAPED_TICK_RATE = 10000;
		
	public ThreadManager() {
		
	}
	
	public void startAll() {
		for(GameThread thread : ThreadRegistry.getThreads()) {
			thread.start();
		}
	}
	
	public void stopAll() {
		for(GameThread thread : ThreadRegistry.getThreads()) {
			thread.requesteStop();
		}
	}
	
	public void addAll(DebugWindow window) {
		for(GameThread thread : ThreadRegistry.getThreads()) {
			window.addPanel(thread.getTracker());
		}
	}
	
	public GameThread accessByName(String name) {
		for(GameThread thread : ThreadRegistry.getThreads()) {
			if(thread.getName().equals(name)) {
				return thread;
			}
		}
		
		return null;
	}
}
