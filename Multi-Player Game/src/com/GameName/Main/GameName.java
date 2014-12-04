package com.GameName.Main;

import java.io.File;

import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.opengl.Display;

import com.GameName.Audio.Sound;
import com.GameName.Audio.SoundEngine;
import com.GameName.Audio.SoundRegistry;
import com.GameName.Command.CommandRegistry;
import com.GameName.Command.Commands.ForceVBOUpdateCommand;
import com.GameName.Command.Commands.HelpCommand;
import com.GameName.Command.Commands.SetPlayerPropertyCommand;
import com.GameName.Command.Commands.TeleportPlayerCommand;
import com.GameName.Cube.Cube;
import com.GameName.Cube.CubeRegistry;
import com.GameName.Entity.Entity;
import com.GameName.Entity.EntityPlayer;
import com.GameName.GUI.GUIManager;
import com.GameName.GUI.GuiRegistry;
import com.GameName.Main.Debugging.DebugWindow;
import com.GameName.Main.Threads.ClassUpdataThread;
import com.GameName.Main.Threads.EntityThread;
import com.GameName.Main.Threads.GLContextThread;
import com.GameName.Main.Threads.GameThread;
import com.GameName.Main.Threads.PhysicsThread;
import com.GameName.Main.Threads.PlayerThread;
import com.GameName.Main.Threads.ThreadManager;
import com.GameName.Main.Threads.ThreadRegistry;
import com.GameName.Main.Threads.VBOUpdateThread;
import com.GameName.Networking.Client;
import com.GameName.Networking.Server;
import com.GameName.Physics.PhysicsEngine;
import com.GameName.Render.RenderEngine;
import com.GameName.Render.Effects.ShaderRegistry;
import com.GameName.Render.Effects.TextureRegistry;
import com.GameName.World.World;
import com.GameName.World.WorldRegistry;
import com.GameName.World.Object.WorldObject;

public class GameName implements ISetup {	
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
	
//	public static List<World> worlds;
	
	public static Controller c;
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
	
	public void preInit()  {
		Cube.regesterCubes();
		
	}
	
	public void init() {
		setupWorlds();
		testCode();
	}
	
	public void postInit() {
		//Cubes
		CubeRegistry.register();
		Cube.concludeInit();
	
		//Textures
		TextureRegistry.register();
		
		//Shaders
		ShaderRegistry.register();
		
		//Sound
		SoundRegistry.register();
		
		//Worlds
		WorldRegistry.register();
		for(World w : WorldRegistry.getWorlds()) {
			w.checkChunks();
		}
		
		//Player
		debugWindow.addPanel(player.getAccess().getMonitor());
		player.getAccess().setCurrentWorld(WorldRegistry.accessByName("MainWorld"));
		player.getAccess().setControls(EntityPlayer.loadControls(new File("res/option/controls.dtg")));
		player.setRenderDistance(1000);
		player.resetCam();
		
		//Threads
		ThreadRegistry.register();
		
		for(WorldObject object : player.getAccess().getCurrentWorld().getWorldObjects()) {
			physics.add(object);
			
			if(object instanceof Entity)
				((EntityThread) threadManager.accessByName("Entity Thread")).add((Entity) object);
		}
		
		((VBOUpdateThread) threadManager.accessByName("VBO Thread")).setWorld(player.getAccess().getCurrentWorld());		
//		((WorldLoadThread) threadManager.accessByName("World Thread")).setWorld(player.getAccess().getCurrentWorld());
		
		//GUI
		GuiRegistry.register();
		
		//Commands
		CommandRegistry.register();
		
		isRunning  = true;
	}
	
	private void testCode() { //TODO: Remove TestCode
		sound = new SoundEngine();
		physics = new PhysicsEngine();
		player = new EntityPlayer();
		guiManager = new GUIManager();
		render = new RenderEngine();
		threadManager = new ThreadManager();
		debugWindow = new DebugWindow();
		glContextThread = new GLContextThread(ThreadManager.UNCAPED_TICK_RATE);
		
		FPS_Thread.setName("FPS Thread");
		
		setupGuis();
//		setupSounds();
		setupThreads();
		setupWorlds();
		setupCommands();
	}
	
	private void setupWorlds() {
		WorldRegistry reg = new WorldRegistry();
		
		reg.addWorld(new World(10, 10, 10, "MainWorld"));
		
		WorldRegistry.addRegistry(reg);
	}
	
	private void setupSounds() {
		SoundRegistry reg = new SoundRegistry();
		
		reg.addSound(new Sound("01 In The End", "wav"));		
		reg.addSound(new Sound("Awesome Music", "wav"));	
		reg.addSound(new Sound("Can\'t Hold Us", "wav"));	
		reg.addSound(new Sound("Come And Get It", "wav"));
				
		SoundRegistry.addRegistry(reg);
	}

	private void setupThreads() {
		ThreadRegistry reg = new ThreadRegistry();		
		
		reg.addThread(new PhysicsThread	 (ThreadManager.DEFAULT_TICK_RATE, GameName.physics));                             
//		reg.addThread(new WorldLoadThread(ThreadManager.DEFAULT_TICK_RATE, GameName.player.getAccess().getCurrentWorld())); 
		reg.addThread(new VBOUpdateThread(ThreadManager.DEFAULT_TICK_RATE, GameName.player.getAccess().getCurrentWorld())); 
			
		reg.addThread(new EntityThread	 (ThreadManager.DEFAULT_TICK_RATE));                     
		reg.addThread(new PlayerThread	 (ThreadManager.DEFAULT_TICK_RATE, GameName.player));   
			
		reg.addThread(new ClassUpdataThread(ThreadManager.DEFAULT_TICK_RATE)); 
		
		ThreadRegistry.addRegistry(reg);
	}
	
	private void setupGuis() {
		GuiRegistry reg = new GuiRegistry();
		
		GuiRegistry.addRegistry(reg);
	}
	
	private void setupCommands() {
		CommandRegistry reg = new CommandRegistry();
		
		reg.addCommand(new HelpCommand());
		reg.addCommand(new ForceVBOUpdateCommand());
		reg.addCommand(new TeleportPlayerCommand());
		reg.addCommand(new SetPlayerPropertyCommand());
		
		CommandRegistry.addRegistry(reg);
	}
	
	public static void cleanUp() {
		threadManager.stopAll();
		
		Cube.cleanUp();	
		
		render.cleanUp();
		sound.cleanUp();
		physics.cleanUp();
		
		for(World w : WorldRegistry.getWorlds()) {
			w.cleanUp();
		}
	}

	public GameName() {			
		preInit();						
		init();			
		postInit();
	}
	
	public void gameLoop() {
		threadManager.addAll(debugWindow);		
		debugWindow.start(debugWindow);
		
		c = checkControllers();		
		FPS_Thread.start();
		
		threadManager.startAll();
		WorldRegistry.accessByName("MainWorld").getLoadedWorld().getAccess().getChunkLoaded().update();
		
		while(isRunning && !Display.isCloseRequested()) {
			getGLContext().tick();
			render.step(1);
			
			for(GameThread thread : ThreadRegistry.getThreads()) {
				thread.getTracker().update();
			}
			
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
	
	public String getVersion() {
		return "0.00.4";
	}
}