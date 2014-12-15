package com.GameName.Engine;

import com.GameName.Audio.SoundEngine;
import com.GameName.Audio.SoundEvent;
import com.GameName.Engine.Threads.GameThread;
import com.GameName.Engine.Threads.ThreadGroup;
import com.GameName.Entity.EntityPlayer;
import com.GameName.Main.Debugging.DebugWindow;
import com.GameName.Networking.Client;
import com.GameName.Networking.Server;
import com.GameName.Physics.PhysicsEngine;
import com.GameName.Physics.Object.PhysicsObject;
import com.GameName.Render.GLContextThread;
import com.GameName.Render.RenderEngine;
import com.GameName.Render.Types.Renderable;
import com.GameName.World.World;

public class GameEngine {
	private GameName_New gameName;
	
	private DebugWindow debugWindow;
	private GLContextThread glContextThread;
	private ThreadGroup threads;
	
	private PhysicsEngine physics;
	private RenderEngine render;
	private SoundEngine sound;
	
	private World currentWorld;
	private EntityPlayer player;
	
	private Server server;
	private Client client;
	
	public GameEngine(GameName_New gameName) {
		this.gameName = gameName;
	}
	
	public GameEngine init(World world) {
		debugWindow = new DebugWindow(this);
		glContextThread = new GLContextThread().init();
		threads = new ThreadGroup();		

		currentWorld = world;
		player = new EntityPlayer(this);
		
		physics = new PhysicsEngine(world);
		render = new RenderEngine(this);
		sound = new SoundEngine(this);
		
		server = new Server(this);
		client = new Client(this);
		
		return this;
	}
	
	public DebugWindow getDebugWindow() {return debugWindow;}
	public GLContextThread getGLContext() {return glContextThread;}	
	public ThreadGroup getThreads() {return threads;}
	public void addThread(GameThread thread) {threads.addThread(thread);}
	
	public void add(PhysicsObject obj) {physics.add(obj);}
	public void add(Renderable obj) {render.add(obj);}
	public void add(SoundEvent obj) {sound.add(obj);}

	public PhysicsEngine getPhysics() {return physics;}	
	public RenderEngine getRender() {return render;}	
	public SoundEngine getSound() {return sound;}

	public EntityPlayer getPlayer() {return player;}
	public World getWorld() {return currentWorld;}
	
	public Server getServer() {return server;}
	public Client getClient() {return client;}
	
	public boolean isRunning() {return true;}
	public GameName_New getGameName() {return gameName;}
	
	public void cleanUp() {
		threads.stopAll();
		
		physics.cleanUp();
		render.cleanUp();
		sound.cleanUp();
	}
}
