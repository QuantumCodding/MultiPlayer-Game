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
import com.GameName.Input.Control;
import com.GameName.Main.Debugging.DebugWindow;
import com.GameName.Main.Threads.EntityThread;
import com.GameName.Main.Threads.PhysicsThread;
import com.GameName.Main.Threads.PlayerThread;
import com.GameName.Main.Threads.WorldLoadThread;
import com.GameName.Networking.Client;
import com.GameName.Networking.Server;
import com.GameName.Physics.PhysicsEngine;
import com.GameName.Render.RenderEngine;
import com.GameName.Render.GUI.GUIManager;
import com.GameName.World.World;
import com.GameName.World.Cube.Cube;

public class GameName {	
	public static boolean isRunning;
	private DebugWindow debugWindow;
	
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
	private int tickRate;
	
	private int oldW, oldH;
	
	private PhysicsThread   physicsThread;
	private WorldLoadThread worldLoadThread;
	private EntityThread    entityThread;
	private PlayerThread    playerThread;
	
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
		tickRate = 60;
			
		try {
			Cube.regesterCubes();
			Cube.concludeInit();
			
			worlds = new ArrayList<World>();
			worlds.add(new World(10, 10, 10, worlds.size(), "Main World"));
			
			sound = new SoundEngine();
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
			
			physicsThread   = new PhysicsThread(tickRate, physics);
			worldLoadThread = new WorldLoadThread(tickRate, player.getAccess().getCurrentWorld()); 
			entityThread    = new EntityThread(tickRate);
			playerThread    = new PlayerThread(tickRate, player);
			
			for(Entity entity : player.getAccess().getCurrentWorld().getEntityList()) {
				physicsThread.add(entity);
				entityThread.add(entity);
			}

			debugWindow = new DebugWindow();
			isRunning  = true;
			
		} catch(InstanceAlreadyExistsException | IOException e) {
			e.printStackTrace();
			
			Start.cleanUp();
		}
	}
	
	public void gameLoop() {
		debugWindow.add(physicsThread.getTracker());
		debugWindow.add(worldLoadThread.getTracker());
		debugWindow.add(entityThread.getTracker());
		debugWindow.add(playerThread.getTracker());
		
		debugWindow.reload();
		
		c = checkControllers();		
		FPS_Thread.start();
		
		physicsThread  .start();
		worldLoadThread.start(); 
		entityThread   .start(); 
		playerThread   .start(); 		
		
		while(isRunning && !Display.isCloseRequested()) {
						
			Control.tick();
			guiManager.update();
			
			render.render3D();
			render.render2D();			
			
			Display.update();
			Display.sync(60);
			
			if(oldW != Display.getWidth() || oldH != Display.getHeight()){
	//			Start.initDisplay(Display.getWidth(), Display.getHeight());
				
				player.resetCam();
				render.setUpPerspectives();
				guiManager.resize(Start.WIDTH, Start.HEIGHT);
				
				oldW = Display.getWidth();
				oldH = Display.getHeight();
			}
			
			debugWindow.tick();			
			FPSTicks ++;
		}
		
		FPS_Thread.interrupt();
		
		physicsThread  .requesteStop();
		worldLoadThread.requesteStop();
		entityThread   .requesteStop();
		playerThread   .requesteStop();
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
}