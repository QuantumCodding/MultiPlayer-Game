package com.GameName.Main.Threads;

import com.GameName.Util.Time;

public abstract class Timer implements Runnable {
	private TimerTracker tracker;
	
	private double tickTime;
	private boolean isRunning, isStopRequested, isPaused;
	private int TPS;
	
	private int tickRate;
	private String name;
	
	private Thread thread;
	
	public Timer(int tickRate, String name) {
		tickTime = 1.0 / (double) tickRate;	
		
		this.name = name;
		this.tickRate = tickRate;
		
		thread = new Thread(this, name);
		
		tracker = new TimerTracker(this);
	}

	public void start() {
		isRunning = true;
		thread.start();
	}
	
	public void pause() throws InterruptedException {
		isPaused = true;
//		thread.wait();
	}
	
	public void resume() {
		isPaused = false;
//		thread.notify();
	}
	
	private void stop() throws InterruptedException {
		isRunning = false;
		
		thread.interrupt();
		
		thread = new Thread(this, name);
		isStopRequested = false;
	}
	
	public void run() {		
		init();

		long lastTime = Time.getTime();
		double unprocessedTime = 0;
		
		int frames = 0;
		long frameCounter = 0;
		
		while(isRunning) {
			if(isPaused) continue;
			
			boolean tick = false;

			long startTime = Time.getTime();
			long passedTime = startTime - lastTime;
			lastTime = startTime;
			
			unprocessedTime += passedTime / (double) Time.getSECONDS();
			frameCounter += passedTime;
			
			while(unprocessedTime > tickTime) {
				tick = true;
				
				unprocessedTime -= tickTime;
				
				if(isStopRequested)	try { stop(); } catch (InterruptedException e) { e.printStackTrace(); }

				if(frameCounter >= Time.getSECONDS()) {
					TPS = frames;
					
					frames = 0;
					frameCounter = 0;
				}
			}
			
			if(tick) {
				tick();				
				frames ++;
				
				tracker.update();				
			} else {				
				try	{
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}						
		}
	}
	
	abstract void init();
	abstract void tick();

	public boolean isRunning() {
		return isRunning;
	}

	public boolean isPaused() {
		return isPaused;
	}

	public int getTPS() {
		return TPS;
	}

	public void requesteStop() {
		isStopRequested = true;
	}

	public String getName() {
		return name;
	}

	public int getTickRate() {
		return tickRate;
	}

	public TimerTracker getTracker() {
		return tracker;
	}
}
