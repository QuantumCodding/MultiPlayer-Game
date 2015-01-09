package com.GameName.Entity;

import static org.lwjgl.openal.AL10.AL_ORIENTATION;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.alListener3f;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.vecmath.Vector2f;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.GameName.Engine.GameEngine;
import com.GameName.Input.Control;
import com.GameName.Items.ItemStack;
import com.GameName.Main.Debugging.Logger;
import com.GameName.Networking.Client;
import com.GameName.Networking.Packets.PacketPlayerInventorySize;
import com.GameName.Networking.Packets.PacketPlayerInventorySlot;
import com.GameName.Networking.Packets.PacketPlayerLocation;
import com.GameName.Networking.Packets.PacketPlayerStats;
import com.GameName.Util.Tag.DTGLoader;
import com.GameName.Util.Tag.TagGroup;
import com.GameName.Util.Vectors.MathVec3f;

public class EntityPlayer extends Entity {
	protected ItemStack[] inv;
	protected int invSize;
	
	protected int power;
	protected double money;
		
	protected Camera cam;
	protected float renderDistance = 10;
	
	protected List<Control> controls;		

	protected int maxJumps = 5;
	protected int jumpHeight = 1;
	protected int jumpsLeft = 0;
	
	protected boolean gravityOn;
	protected boolean lockMovement;
	
	protected float lookSpeed;
	protected float lookSpeedUp;
	protected float speed;
		
	protected int maxReach;
	protected MathVec3f selectedCube;
	
	protected Vector2f pointerPos;
	protected boolean isPointerDown;
		
	protected PlayerMonitor monitor;
	
	public EntityPlayer(GameEngine eng) {
		super(eng);
	}
	
	protected void init() {
		access = new EntityPlayerAccess(ENGINE, this);
		monitor = new PlayerMonitor();
		
		resetCam();
		resetPlayer();
		
		alListener3f(AL_POSITION, pos.getX(), pos.getY(), pos.getZ());
	}
	
	public void resetCam() {//                                                       0.3f
		cam = new Camera(70, (float)Display.getWidth() / (float)Display.getHeight(), 0.5f, renderDistance);
		if(ENGINE.getRender() != null) {
			ENGINE.getRender().setUpPerspectives();
		}
	}
	
	public void resetPlayer() {
		super.reset();
		Logger.print("Player Reseting").setType("Setup").end();
		
		pointerPos = new Vector2f(0, 0);
		
		invSize = 10;
		inv = new ItemStack[invSize];
		
		pos = new MathVec3f(20, 95, 20);		
		rot = new MathVec3f(180, 0, 0);
		
		gravityOn = false;
		
		health = maxHealth;
		hunger = maxHunger;
		mana = maxMana;
		
		power = 0;
		money = 0.0;
		
		maxReach = 50;

		selectedCube = new MathVec3f(0, 0, 0);
		
		playerMove(true);
		
		Client clt = ENGINE.getClient();			
		if(clt != null && clt.isOnServer()) {
			clt.sendPacket(new PacketPlayerStats(clt.getID(), hunger, health, mana, power, money));			
			clt.sendPacket(new PacketPlayerInventorySize(clt.getID(), invSize));
			for(int i = 0; i < invSize; i ++) clt.sendPacket(new PacketPlayerInventorySlot(clt.getID(), i, inv[i]));
		}
	}
	
	public void setRenderDistance(float distance) {
		renderDistance = distance;
	}
	
	public void update() {
		super.update();
		
		if(gravityOn) {
			
//			selectedCube = PhysicsUtil.getLookPosition(pos, rot, currentWorld, maxReach); //rayTrace(pos.clone(), pos.add(getLook().multiply(maxReach)), currentWorld);//
			if(selectedCube == null) selectedCube = pos.clone();
		}		

		renderPos = pos.multiply(adjust);
		
		checkControls();
		
		cam.x = renderPos.getX(); cam.rotX = rot.getX();
		cam.y = renderPos.getY(); cam.rotY = rot.getY();
		cam.z = renderPos.getZ(); cam.rotZ = rot.getZ();

		if(onGround) {
			jumpsLeft = maxJumps;	
		}		
		
		monitor.tick(this);
	}
	
	private void checkControls() {
		speed = Keyboard.isKeyDown(Keyboard.KEY_E) ? 1f/15f : Keyboard.isKeyDown(Keyboard.KEY_R) ? 1f/3f : 1f/7f; //15
		lookSpeed = Keyboard.isKeyDown(Keyboard.KEY_Y) ? 0.5f : Keyboard.isKeyDown(Keyboard.KEY_U) ? 5 : 1;
		lookSpeedUp = lookSpeed;
				
		MathVec3f oldPos = pos.clone();
		
		for(Control ctr : controls) {
			if(ctr.isActive() != 0.0){
				
				switch(ctr.control) {
					case "forward": if(!lockMovement) access.moveZ((float) /*(ctr.isActive() / speed)*/ 2); 	break;						
					case "back": 	if(!lockMovement) access.moveZ((float)-/*(ctr.isActive() / speed)*/ 2); 	break;		
					case "left": 	if(!lockMovement) access.moveX((float)-/*(ctr.isActive() / speed)*/ 2); 	break;
					case "right":   if(!lockMovement) access.moveX((float) /*(ctr.isActive() / speed)*/ 2);   	break;
					case "up": 		if(!lockMovement) access.moveY((float) /*(ctr.isActive() / speed)*/ 5); 	break;		
					case "down": 	if(!lockMovement) access.moveY((float)-/*(ctr.isActive() / speed)*/ 5); 	break;
					
					case "lookUp": 	if(!lockMovement)  /*if(entityPhisics(0))*/ access.rotateX((float) (ctr.isActive() / lookSpeedUp)); break;				
					case "lookDown": if(!lockMovement) /*if(entityPhisics(0))*/ access.rotateX((float) (ctr.isActive() / lookSpeedUp)); break;			
					case "lookLeft": if(!lockMovement) /*if(entityPhisics(0))*/ access.rotateY((float) (ctr.isActive() / lookSpeed));   break;
					case "lookRight": if(!lockMovement)/*if(entityPhisics(0))*/ access.rotateY((float) (ctr.isActive() / lookSpeed));	break;
					
					case "jump": 					
						if(!lockMovement) if(jumpsLeft > 0) { 
							vel.add(new MathVec3f(0, jumpHeight, 0));
							jumpsLeft --;
						}						
					break;
										
					case "resetPlayer": if(!lockMovement) resetPlayer(); break;
					case "gravity": 	if(!lockMovement) gravityOn = !gravityOn; break;
					
					case "pointerUp": 		pointerPos.y += ((float) ctr.isActive()); break;
					case "pointerDown": 	pointerPos.y += ((float) ctr.isActive()); break;
					case "pointerLeft": 	pointerPos.x += ((float) ctr.isActive()); break;
					case "pointerRight": 	pointerPos.x += ((float) ctr.isActive()); break;
					case "click": 			isPointerDown = true; break;
					
//					case "toggle": if(!lockMovement) { GameName.guiManager.toggle("Test"); GameName.guiManager.toggle("Pause"); } break;
					
//					case "placeTest": currentWorld.setCube(selectedCube.add(PhysicsUtil.getPosNextToFace(PhysicsUtil.getDirection(rot))), Cube.ColorfulTestCube.getId()); break;
//					case "removeCube": currentWorld.setCube(selectedCube, Cube.Air.getId()); break;
					
//					case "reloadChunks": Threads.VBOThread.forceUpdate();
					
					default: System.err.println("Default Called: Player is not using control " + ctr.control); break;					
				} 				
			} else {
				switch(ctr.control) {
					case "click": isPointerDown = false; break;
					
					default: break;
				}
			}
						
			if(!oldPos.equals(pos)) {
				playerMove(true);
			}
		}
	}
		
	public void playerMove(boolean send) {
		alListener3f(AL_POSITION, pos.getX(), pos.getY(), pos.getZ());
		alListener3f(AL_ORIENTATION, rot.getX(), rot.getY(), rot.getZ());
		
		if(!send) return;		
		Client clt = ENGINE.getClient();			
		if(clt != null && clt.isOnServer()) {
			clt.sendPacket(new PacketPlayerLocation(clt.getID(), pos));
		}	
	}
	
	public EntityPlayerAccess getAccess() {
		return (EntityPlayerAccess) access;
	}
	
	public static List<Control> loadControls(File f) {
		List<Control> controlls = new ArrayList<Control>();
		
		try {
			HashSet<TagGroup> groups = DTGLoader.readDTGFile(f);
			
			for(TagGroup group : groups) {
				controlls.add(Control.getControlFormTagGroup(group));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return controlls;
	}
	
	
	public File saveControls(File f) throws IOException {
		ArrayList<TagGroup> tagLines = new ArrayList<TagGroup>();
		
		for(Control control : controls) {
			tagLines.add(control.getTagGroup());
		}
		
		DTGLoader.saveDTGFile(f, tagLines);		
		return f;
	}
}
