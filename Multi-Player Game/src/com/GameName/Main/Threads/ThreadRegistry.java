package com.GameName.Main.Threads;

import java.util.ArrayList;

import com.GameName.Util.Registry;
import com.GameName.Util.RegistryStorage;

public class ThreadRegistry extends Registry<GameThread> {
	private static GameThread[] threads;
	private static RegistryStorage<GameThread> regstries;
	private static ArrayList<GameThread> unregisteredGameThreads;
	
	static {
		regstries = new RegistryStorage<GameThread>();
		unregisteredGameThreads = new ArrayList<GameThread>();
	}
	
	public static GameThread[] getThreads() {
		return threads;
	}
	
	public void addThread(GameThread thread) {
		registerOBJ(thread);
	}

	public static void register() {regstries.register();}
	
	public static void addRegistry(ThreadRegistry reg) {
		regstries.addRegistry(reg);
	}
	
	protected void register(GameThread e) {
		unregisteredGameThreads.add(e);
	}
	
	protected void registrtionConcluded() {
		threads = unregisteredGameThreads.toArray(new GameThread[unregisteredGameThreads.size()]);
		
		unregisteredGameThreads.clear();
		unregisteredGameThreads = null;
	}
}
