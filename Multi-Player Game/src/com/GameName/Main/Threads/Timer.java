package com.GameName.Main.Threads;

import com.GameName.Util.Time;

public abstract class Timer implements Runnable {
	private double tickTime;
	private boolean isRunning, isStopRequested, isPaused;
	private int TPS;
	
	private Thread thread;
	
	public Timer(int tickRate, String name) {
		tickTime = 1.0 / (double) tickRate;
		
		thread = new Thread(this, name);
	}

	public void start() {
		isRunning = true;
		thread.start();
	}
	
	public void pause() throws InterruptedException {
		isPaused = true;
		thread.wait();
	}
	
	public void resume() {
		isPaused = false;
		thread.notify();
	}
	
	private void stop() throws InterruptedException {
		isRunning = false;
		
		thread.interrupt();
		thread.join();
		
		thread = new Thread(this, thread.getName());
		isStopRequested = false;
	}
	
	public void run() {		
		init();

		double lastTime = Time.getTime();
		double unprocessedTime = 0;
		
		int frames = 0;
		double frameCounter = 0;
		
		while(isRunning) {
			if(isPaused) continue;
			
			boolean tick = false;

			double startTime = Time.getTime();
			double passedTime = startTime - lastTime;
			lastTime = startTime;
			
			unprocessedTime += passedTime;
			frameCounter += passedTime;
			
			while(unprocessedTime > tickTime) {
				tick = true;
				
				unprocessedTime -= tickTime;
				
				if(isStopRequested)	try { stop(); } catch (InterruptedException e) { e.printStackTrace(); }

				if(frameCounter >= 1.0) {
					TPS = frames;
					
					frames = 0;
					frameCounter = 0;
				}
			}
			
			if(tick) {
				tick();				
				frames ++;
				
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
}
