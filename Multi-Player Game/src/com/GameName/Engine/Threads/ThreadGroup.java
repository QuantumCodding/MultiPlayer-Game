package com.GameName.Engine.Threads;

import java.util.ArrayList;

import com.GameName.Console.ThreadStatusTab;
import com.GameName.Engine.GameEngine;

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
	
	public void setEngine(GameEngine engine) {
		for(GameThread thread : threads) {
			thread.setENGINE(engine);
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
	
	public void addAll(ThreadStatusTab tab) {
		for(GameThread thread : threads) {
			tab.addThread(thread.getTracker());
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
