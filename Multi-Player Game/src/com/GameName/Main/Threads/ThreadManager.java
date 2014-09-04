package com.GameName.Main.Threads;

import com.GameName.Main.GameName;
import com.GameName.Main.Debugging.DebugWindow;

public class ThreadManager {
	public static final int DEFAULT_TICK_RATE = 60;
	public static final int UNCAPED_TICK_RATE = 10000;
	
	private GameThread[] threads;
	
	public ThreadManager() {
		threads = new GameThread[] {		
			new PhysicsThread	(DEFAULT_TICK_RATE, GameName.physics),                             
			new WorldLoadThread	(DEFAULT_TICK_RATE, GameName.player.getAccess().getCurrentWorld()),
			new VBOUpdateThread	(DEFAULT_TICK_RATE, GameName.player.getAccess().getCurrentWorld()),	
//			new RenderThread	(DEFAULT_TICK_RATE, GameName.render), 
			
			new EntityThread	(DEFAULT_TICK_RATE),                                       
			new PlayerThread	(DEFAULT_TICK_RATE, GameName.player),  
			
			new ClassUpdataThread(DEFAULT_TICK_RATE),
		};
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
			window.add(thread.getTracker());
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
}
