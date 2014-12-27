package com.GameName.Engine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import com.GameName.Engine.Registries.CommandRegistry;
import com.GameName.Engine.Registries.CubeRegistry;
import com.GameName.Engine.Registries.EntityRegistry;
import com.GameName.Engine.Registries.ThreadRegistry;
import com.GameName.Engine.Registries.WorldRegistry;
import com.GameName.Engine.ResourceManager.Threads;
import com.GameName.Engine.ResourceManager.Worlds;
import com.GameName.Engine.Threads.GameThread;
import com.GameName.Entity.EntityPlayer;
import com.GameName.Input.Control;
import com.GameName.Render.Effects.ShaderRegistry;
import com.GameName.Render.Effects.Texture;
import com.GameName.Render.Window.Window;
import com.GameName.Util.Interfaces.ISetup;

public class GameName_New implements ISetup {
	private static final String VERSION = "In-Dev";
	private static ArrayList<ISetup> mods = new ArrayList<>();
	
	private GameEngine engine;
	private ResourceManager res;
	private Window window;
	
	private boolean isRunning;
	private int FPS;
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
	
	public void preInit() {
		res = new ResourceManager();		
		window = new Window();	
		
		try {
			window.setTitle("Multi-Player Game");		
			window.setIcon(ImageIO.read(new File("res/textures/icon.png"))); 
			
			window.initDisplay();		
			
			File[] files = new File("res/textures/Splash Screen").listFiles();
			File splashImage = files[(int) (Math.random() * 100) % files.length];
			
			window.setSplash(new Texture(ImageIO.read(splashImage), "Unspell + Names", false));
			window.drawSplash();
		} catch(IOException | LWJGLException e) { e.printStackTrace(); }		

		ResourceManager.addCubeRegistery(res);
		ResourceManager.addWorldRegistery(res);
		ResourceManager.addThreadRegistery(res);
		ResourceManager.addEntityRegistery(res);
		ResourceManager.addCommandRegistery(res);
		
		for(ISetup mod : mods) {
			mod.preInit();
		}
	}
	
	public void init() {
		engine = new GameEngine(this);
		ResourceManager.registerAll(engine);	
		
		CubeRegistry.conclude();
		WorldRegistry.conclude(engine);
		ThreadRegistry.conclude();
		EntityRegistry.conclude();
		CommandRegistry.conclude();
		
		engine.init(Worlds.MainWorld);	
		window.setupOpenGL(engine);
		window.drawSplash();
		
		engine.getRender().setUpShaders(); //TODO: Remove ShaderRegistry
		ShaderRegistry.register();
		
		engine.addThread(Threads.EntityThread);
		engine.addThread(Threads.PhysicsThread);
		engine.addThread(Threads.VBOThread);
		engine.addThread(Threads.WorldLoadThread);
		
		for(ISetup mod : mods) {
			mod.init();
		}
	}
	
	public void postInit() {
		engine.getThreads().setEngine(engine);
		
		engine.getPlayer().getAccess().setCurrentWorld(Worlds.MainWorld);
		engine.getPlayer().getAccess().setControls(EntityPlayer.loadControls(new File("res/option/controls.dtg")));
		engine.getPlayer().setRenderDistance(1000);
		engine.getPlayer().resetCam();
		
		Threads.EntityThread.addEntity(engine.getPlayer());
		Threads.PhysicsThread.setEngine(engine.getPhysics());
		engine.getPhysics().add(engine.getPlayer());
		
		Threads.WorldLoadThread.setWorld(Worlds.MainWorld);
		
		for(ISetup mod : mods) {
			mod.postInit();
		}
	}
	
	public void run() {
		isRunning = true;
		Worlds.MainWorld.getLoadedWorld().getAccess().getChunkLoaded().update();
		
		engine.getThreads().addAll(engine.getDebugWindow());
		engine.getThreads().startAll(); FPS_Thread.start();
		engine.getDebugWindow().start(engine.getDebugWindow());
		
		engine.getPlayer().reset();
		
		while(isRunning && !window.isCloseRequested()) {
			engine.getGLContext().tick();
			engine.getRender().step(1);
			
			Control.tick();
//			GameName.guiManager.update();
			engine.getDebugWindow().update();
			
			for(GameThread thread : engine.getThreads().getThreads()) {
				thread.getTracker().update();
			}
			
			FPSTicks ++;
			Display.update();
			Display.sync(60);
		}		

		FPS_Thread.interrupt();
		engine.getThreads().stopAll();
	}
	
	public void cleanUp() {
		engine.cleanUp();
		
		Worlds.MainWorld.cleanUp();
	}
	
	public static boolean addMod(ISetup mod) {return mods.add(mod);}
	
	public GameEngine getEngine() {return engine;}
	public ResourceManager getRes() {return res;}
	public Window getWindow() {return window;}
	
	public boolean isRunning() {return isRunning;}
	public void stop() {isRunning = false;}
	
	public int getFPS() {return FPS;}	
	
	public String getVersion() {
		return VERSION;
	}
}
