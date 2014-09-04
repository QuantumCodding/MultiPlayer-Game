package com.GameName.Main;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.management.InstanceAlreadyExistsException;

import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.opengl.Display;

import com.GameName.Audio.SoundEngine;
import com.GameName.Entity.Entity;
import com.GameName.Entity.EntityPlayer;
import com.GameName.Main.Debugging.DebugWindow;
import com.GameName.Main.Threads.EntityThread;
import com.GameName.Main.Threads.GLContextThread;
import com.GameName.Main.Threads.PhysicsThread;
import com.GameName.Main.Threads.ThreadManager;
import com.GameName.Networking.Client;
import com.GameName.Networking.Server;
import com.GameName.Physics.PhysicsEngine;
import com.GameName.Render.RenderEngine;
import com.GameName.Render.GUI.GUIManager;
import com.GameName.World.World;
import com.GameName.World.Cube.Cube;

public class GameName {	
	public static boolean isRunning;
	public static DebugWindow debugWindow;

	public static ThreadManager threadManager;
	public static GLContextThread glContextThread;
	
	public static EntityPlayer player;
	
	public static PhysicsEngine physics;
	public static RenderEngine render;
	public static GUIManager guiManager;
	public static SoundEngine sound;
	
	public static Server server;
	public static Client client;
	
	public static List<World> worlds;
	
	public static Controller c;
	public static Point pointer;
	public static boolean click;
	public static boolean lockMovement;
	
	private static int FPS;
	private int FPSTicks;
	
	private Thread FPS_Thread = new Thread() {
		public void run() {
			while(true) {
				FPSTicks = 0;
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
				
				FPS = FPSTicks;
			}
		}
	};

	public GameName() {			
		try {			
			glContextThread = new GLContextThread(ThreadManager.UNCAPED_TICK_RATE);
			
			Cube.regesterCubes();
			Cube.concludeInit();
			
			worlds = new ArrayList<World>();
			worlds.add(new World(10, 10, 10, worlds.size(), "Main World"));
			
			sound = new SoundEngine();
			sound.registerSounds();
	//		sound.playRandom();
			
			physics = new PhysicsEngine();
			
			player = new EntityPlayer();		
			player.setRenderDistance(1000);
			player.resetCam();
			
			pointer = new Point(0, 0);
			click = false;
			guiManager = new GUIManager();
			
			render = new RenderEngine();		
			player.getAccess().setCurrentWorld(worlds.get(0));
				
			guiManager.accessByName("Main HUD").open();
			guiManager.accessByName("Music").open();
//			guiManager.accessByName("Pause").open();
			
			player.getAccess().setControls(EntityPlayer.loadControls(new File("res/option/controls.dat")));
			
			FPS_Thread.setName("FPS Thread");
			
			for(World w : worlds.toArray(new World[worlds.size()])) {
				w.checkChunks();
			}
			
			threadManager = new ThreadManager();
			
			for(Entity entity : player.getAccess().getCurrentWorld().getEntityList()) {
				((PhysicsThread) threadManager.accessByName("Physics Thread")).add(entity);
				((EntityThread) threadManager.accessByName("Entity Thread")).add(entity);
			}

			debugWindow = new DebugWindow();
			isRunning  = true;
			
		} catch(InstanceAlreadyExistsException | IOException e) {
			e.printStackTrace();
			
			Start.cleanUp();
		}
	}
	
	public void gameLoop() {
		threadManager.addAll(debugWindow);		
		debugWindow.reload();
		
		c = checkControllers();		
		FPS_Thread.start();
		
		threadManager.startAll();
		
		while(isRunning && !Display.isCloseRequested()) {
			getGLContext().tick();
			
			render.render3D();
			render.render2D();
			
			FPSTicks ++;
			Display.update();
			Display.sync(60);
		}		

		FPS_Thread.interrupt();
	}
	
	
	private Controller checkControllers() {
		Controllers.poll();
		
		try {
			return Controllers.getController(0);
		} catch(IndexOutOfBoundsException e) {
			return null;
		}
	}

	public static int getFPS() {
		return FPS;
	}	
	
	public static GLContextThread getGLContext() {
		return glContextThread;
	}
	
	public static void cleanUp() {
		threadManager.stopAll();
		
		Cube.cleanUp();		
		render.cleanUp();
		sound.cleanUp();
		
		for(World w : worlds) {
			w.cleanUp();
		}
	}
}