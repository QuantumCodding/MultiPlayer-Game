package com.GameName.Main;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.management.InstanceAlreadyExistsException;

import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.opengl.Display;

import com.GameName.Audio.SoundEngin;
import com.GameName.Entity.EntityPlayer;
import com.GameName.Input.Control;
import com.GameName.Networking.Client;
import com.GameName.Networking.Server;
import com.GameName.Render.RenderEngin;
import com.GameName.Render.GUI.GUIManager;
import com.GameName.World.World;
import com.GameName.World.Cube.Cube;

public class GameName {	
	public static boolean isRunning;
	
	public static EntityPlayer player;
	
	public static RenderEngin render;
	public static GUIManager guiManager;
	public static SoundEngin sound;
	
	public static Server server;
	public static Client client;
	
	public static List<World> worlds;
	
	public static Controller c;
	public static Point pointer;
	public static boolean click;
	public static boolean lockMovement;
	
	private static int FPS;
	private int FPSTicks;
	
	private int oldW, oldH;
	
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
			Cube.regesterCubes();
			Cube.concludeInit();
			
			worlds = new ArrayList<World>();
			worlds.add(new World(10, 10, 10, worlds.size(), "Main World"));
			
			sound = new SoundEngin();
	//		sound.playRandom();
			
			player = new EntityPlayer();		
			player.setRenderDistance(1000);
			player.resetCam();
			
			pointer = new Point(0, 0);
			click = false;
			guiManager = new GUIManager();
			
			render = new RenderEngin();		
			player.getAccess().setCurrentWorld(worlds.get(0));
				
			guiManager.accessByName("Main HUD").open();
			guiManager.accessByName("Music").open();
//			guiManager.accessByName("Pause").open();
			
			player.getAccess().setControls(EntityPlayer.loadControls(new File("res/option/controls.dat")));
			
			FPS_Thread.setName("FPS Thread");
			
			for(World w : worlds.toArray(new World[worlds.size()])) {
				w.checkChunks();
			}
			
			isRunning  = true;
			
		} catch(FileNotFoundException | InstanceAlreadyExistsException e) {
			e.printStackTrace();
			
			Start.cleanUp();
		}
	}
	
	public void gameLoop() {
		c = checkControllers();		
		FPS_Thread.start();
		
		while(isRunning && !Display.isCloseRequested()) {
			player.updata();
			
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
			
			FPSTicks ++;
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
}