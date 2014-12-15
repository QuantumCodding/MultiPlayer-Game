package com.GameName.Engine.Threads;

import java.util.ArrayList;

import com.GameName.Engine.GameEngine;
import com.GameName.Main.Debugging.DebugWindow;

public class ThreadGroup {
	public static final int DEFAULT_TICK_RATE = 60;
	public static final int UNCAPED_TICK_RATE = 10000;
	
	private ArrayList<GameThread> threads;
	
	public ThreadGroup() {
		threads = new ArrayList<>();
	}
	
	public ThreadGroup addThread(GameThread thread) {
		threads.add(thread); return this;
	}
	
	public void setEngine(GameEngine eng) {
		for(GameThread thread : threads) {
			thread.setEngine(eng);
		}
	}
	
	public void startAll() {
		for(GameThread thread : threads) {
			thread.start();
		}
	}
	
	public void stopAll() {
		for(GameThread thread : threads) {
			thread.requesteStop();
		}
	}
	
	public void addAll(DebugWindow window) {
		for(GameThread thread : threads) {
			window.addPanel(thread.getTracker());
		}
	}
	
	public GameThread accessByName(String name) {
		for(GameThread thread : threads) {
			if(thread.getName().equals(name)) {
				return thread;
			}
		}
		
		return null;
	}
	
	public ArrayList<GameThread> getThreads() {
		return threads;
	}
}
