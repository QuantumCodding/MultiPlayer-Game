package com.GameName.Engine;

import java.util.ArrayList;

import com.GameName.Command.Commands.HelpCommand;
import com.GameName.Command.Commands.SaveCommand;
import com.GameName.Command.Commands.SetPlayerPropertyCommand;
import com.GameName.Command.Commands.TeleportPlayerCommand;
import com.GameName.Cube.Cube;
import com.GameName.Cube.Cubes.AirCube;
import com.GameName.Cube.Cubes.ColorfulTestCube;
import com.GameName.Cube.Cubes.CopperCube;
import com.GameName.Cube.Cubes.GoldCube;
import com.GameName.Cube.Cubes.StoneCube;
import com.GameName.Cube.Cubes.TestCube;
import com.GameName.Engine.Registries.CommandRegistry;
import com.GameName.Engine.Registries.CubeRegistry;
import com.GameName.Engine.Registries.ThreadRegistry;
import com.GameName.Engine.Registries.WorldRegistry;
import com.GameName.Engine.Threads.EntityThread;
import com.GameName.Engine.Threads.PhysicsThread;
import com.GameName.Engine.Threads.ThreadGroup;
import com.GameName.Engine.Threads.VBOThread;
import com.GameName.Engine.Threads.WorldLoadThread;
import com.GameName.Physics.Object.Material;
import com.GameName.Physics.Object.Material.Phase;
import com.GameName.Util.Interfaces.ICommandRegister;
import com.GameName.Util.Interfaces.ICubeRegister;
import com.GameName.Util.Interfaces.IEntityRegister;
import com.GameName.Util.Interfaces.IThreadRegister;
import com.GameName.Util.Interfaces.IWorldRegister;
import com.GameName.World.World;

public class ResourceManager implements ICubeRegister, ICommandRegister, IEntityRegister, IWorldRegister, IThreadRegister {

	public void registerCubes() {
		CubeRegistry.registerCube(Cubes.Air);
		CubeRegistry.registerCube(Cubes.TestCube);
		CubeRegistry.registerCube(Cubes.ColorfulTestCube);
		CubeRegistry.registerCube(Cubes.StoneCube);
		CubeRegistry.registerCube(Cubes.GoldCube);
		CubeRegistry.registerCube(Cubes.CopperCube);
	}	
	
	public void registerWorlds() {
		WorldRegistry.registryWorld(Worlds.MainWorld);
	}

	public void registerThreads() {
		ThreadRegistry.registerThread(Threads.WorldLoadThread);
		ThreadRegistry.registerThread(Threads.VBOThread);
		ThreadRegistry.registerThread(Threads.EntityThread);
		ThreadRegistry.registerThread(Threads.PhysicsThread);
	}

	public void registerEntities() {

	}

	public void registerCommands(GameEngine eng) {
		CommandRegistry.registerCommand(new HelpCommand(eng));
		CommandRegistry.registerCommand(new TeleportPlayerCommand(eng));
		CommandRegistry.registerCommand(new SetPlayerPropertyCommand(eng));
		CommandRegistry.registerCommand(new SaveCommand(eng));
	}
	
	public static final class Cubes {
		public static final Cube Air = new AirCube();		
		public static final Cube TestCube = new TestCube();
		public static final Cube ColorfulTestCube = new ColorfulTestCube();		
		public static final Cube StoneCube = new StoneCube();		
		public static final Cube GoldCube = new GoldCube();
		public static final Cube CopperCube = new CopperCube();		
	}
	
	public static final class Materials {
		public static final Material Stone = new Material(2.3f, 0.72f, Phase.Solid, "Stone");
		public static final Material Air = new Material(1.225f/100, 0.0f, Phase.Gas, "Air");
		public static final Material Human = new Material(1.17f, 1.0f, Phase.Solid, "Human");
	}
	
	public static final class Worlds {
		public static final World MainWorld = new World(5, 5, 5, "MainWorld");
	}
	
	public static final class Threads {
		public static final WorldLoadThread WorldLoadThread = new WorldLoadThread(ThreadGroup.DEFAULT_TICK_RATE);
		public static final VBOThread VBOThread = new VBOThread(ThreadGroup.DEFAULT_TICK_RATE);
		public static final EntityThread EntityThread = new EntityThread(ThreadGroup.DEFAULT_TICK_RATE);
		public static final PhysicsThread PhysicsThread = new PhysicsThread(ThreadGroup.DEFAULT_TICK_RATE);
	}
	
	private static ArrayList<ICubeRegister> cubeRegs = new ArrayList<>();
	private static ArrayList<IWorldRegister> worldRegs = new ArrayList<>();
	private static ArrayList<IThreadRegister> threadRegs = new ArrayList<>();
	private static ArrayList<IEntityRegister> entityRegs = new ArrayList<>();
	private static ArrayList<ICommandRegister> commandRegs = new ArrayList<>();
	
	public static boolean addCubeRegistery(ICubeRegister e) {return cubeRegs.add(e);}
	public static boolean addWorldRegistery(IWorldRegister e) {return worldRegs.add(e);}
	public static boolean addThreadRegistery(IThreadRegister e) {return threadRegs.add(e);}
	public static boolean addEntityRegistery(IEntityRegister e) {return entityRegs.add(e);}
	public static boolean addCommandRegistery(ICommandRegister e) {return commandRegs.add(e);}
	
	public static void registerAll(GameEngine eng) {
		for(ICubeRegister reg : cubeRegs) reg.registerCubes();
		for(IWorldRegister reg : worldRegs) reg.registerWorlds();
		for(IThreadRegister reg : threadRegs) reg.registerThreads();
		for(IEntityRegister reg : entityRegs) reg.registerEntities();
		for(ICommandRegister reg : commandRegs) reg.registerCommands(eng);
	}
}
