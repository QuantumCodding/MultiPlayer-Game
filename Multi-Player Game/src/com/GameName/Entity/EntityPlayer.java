package com.GameName.Entity;

import static org.lwjgl.openal.AL10.AL_ORIENTATION;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.alListener3f;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.GameName.Cube.Cube;
import com.GameName.Input.Control;
import com.GameName.Items.ItemStack;
import com.GameName.Main.GameName;
import com.GameName.Main.Debugging.Logger;
import com.GameName.Main.Threads.VBOUpdateThread;
import com.GameName.Networking.Client;
import com.GameName.Networking.Packets.PacketPlayerInventorySize;
import com.GameName.Networking.Packets.PacketPlayerInventorySlot;
import com.GameName.Networking.Packets.PacketPlayerLocation;
import com.GameName.Networking.Packets.PacketPlayerStats;
import com.GameName.Physics.PhysicsUtil;
import com.GameName.Util.Tag.DTGLoader;
import com.GameName.Util.Tag.TagGroup;
import com.GameName.Util.Vectors.Vector2f;
import com.GameName.Util.Vectors.Vector3f;

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
	
	protected float lookSpeed;
	protected float lookSpeedUp;
	protected float speed;
		
	protected int maxReach;
	protected Vector3f selectedCube;
	
	protected Vector2f pointerPos;
	protected boolean isPointerDown;
		
	protected PlayerMonitor monitor;
	
	protected void init() {
		access = new EntityPlayerAccess(this);
		monitor = new PlayerMonitor();
		
		resetCam();
		resetPlayer();
		
		alListener3f(AL_POSITION, pos.getX(), pos.getY(), pos.getZ());
	}
	
	public void resetCam() {//                                                       0.3f
		cam = new Camera(70, (float)Display.getWidth() / (float)Display.getHeight(), 1f, renderDistance);
		if(GameName.render != null) {
			GameName.render.setUpPerspectives();
		}
	}
	
	public void resetPlayer() {
		Logger.print("Player Reseting").setType("Setup").end();
		
		pointerPos = new Vector2f(0, 0);
		
		invSize = 10;
		inv = new ItemStack[invSize];
		
		pos = new Vector3f(20, 110, 20);		
		rot = new Vector3f(180, 0, 0);
		
		gravityOn = false;
		
		health = maxHealth;
		hunger = maxHunger;
		mana = maxMana;
		
		power = 0;
		money = 0.0;
		
		maxReach = 50;

		selectedCube = new Vector3f(0, 0, 0);
		
		playerMove(true);
		
		Client clt = GameName.client;			
		if(clt != null && clt.isOnServer()) {
			clt.sendPacket(new PacketPlayerStats(clt.getID(), hunger, health, mana, power, money));			
			clt.sendPacket(new PacketPlayerInventorySize(clt.getID(), invSize));
			for(int i = 0; i < invSize; i ++) clt.sendPacket(new PacketPlayerInventorySlot(clt.getID(), i, inv[i]));
		}
	}
	
	public void setRenderDistance(float distance) {
		renderDistance = distance;
	}
	
	public void updata() {
		if(gravityOn) {
			super.updata();
			
			selectedCube = PhysicsUtil.getLookPosition(pos, rot, currentWorld, maxReach); //rayTrace(pos.clone(), pos.add(getLook().multiply(maxReach)), currentWorld);//
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
		speed = Keyboard.isKeyDown(Keyboard.KEY_E) ? 0.5f : Keyboard.isKeyDown(Keyboard.KEY_R) ? 15 : 7; //15
		lookSpeed = Keyboard.isKeyDown(Keyboard.KEY_Y) ? 0.5f : Keyboard.isKeyDown(Keyboard.KEY_U) ? 5 : 1;
		lookSpeedUp = lookSpeed;
				
		Vector3f oldPos = pos.clone();
		
		for(Control ctr : controls) {
			if(ctr.isActive() != 0.0){
				
				switch(ctr.control) {
					case "forward": if(!GameName.lockMovement) if(PhysicsUtil.canMove(pos, PhysicsUtil.FORWARD, currentWorld)) 	access.moveZ((float) (ctr.isActive() / speed)); 	break;						
					case "back": 	if(!GameName.lockMovement) if(PhysicsUtil.canMove(pos, PhysicsUtil.BACKWARD, currentWorld)) access.moveZ((float) (ctr.isActive() / speed)); 	break;		
					case "left": 	if(!GameName.lockMovement) if(PhysicsUtil.canMove(pos, PhysicsUtil.LEFT, currentWorld)) 	access.moveX((float) (ctr.isActive() / speed)); 	break;
					case "right":   if(!GameName.lockMovement) if(PhysicsUtil.canMove(pos, PhysicsUtil.RIGHT, currentWorld)) 	access.moveX((float) (ctr.isActive() / speed));     break;
					case "up": 		if(!GameName.lockMovement) if(PhysicsUtil.canMove(pos, PhysicsUtil.UP, currentWorld)) 		access.moveY((float) (ctr.isActive() / speed)); 								break;		
					case "down": 	if(!GameName.lockMovement) if(PhysicsUtil.canMove(pos, PhysicsUtil.DOWN, currentWorld)) 	access.moveY((float) (ctr.isActive() / speed)); 								break;
					
					case "lookUp": 	if(!GameName.lockMovement)  /*if(entityPhisics(0))*/ access.rotateX((float) (ctr.isActive() / lookSpeedUp));  break;				
					case "lookDown": if(!GameName.lockMovement) /*if(entityPhisics(0))*/ access.rotateX((float) (ctr.isActive() / lookSpeedUp)); break;			
					case "lookLeft": if(!GameName.lockMovement) /*if(entityPhisics(0))*/ access.rotateY((float) (ctr.isActive() / lookSpeed));   break;
					case "lookRight": if(!GameName.lockMovement)/*if(entityPhisics(0))*/ access.rotateY((float) (ctr.isActive() / lookSpeed));	   break;
					
					case "jump": 					
						if(!GameName.lockMovement) if(jumpsLeft > 0) { 
							vel.add(new Vector3f(0, jumpHeight, 0));
							jumpsLeft --;
						}						
					break;
										
					case "resetPlayer": if(!GameName.lockMovement) resetPlayer(); break;
					case "gravity": 	if(!GameName.lockMovement) gravityOn = !gravityOn; break;
					
					case "pointerUp": 		pointerPos.addY((float) ctr.isActive()); break;
					case "pointerDown": 	pointerPos.addY((float) ctr.isActive()); break;
					case "pointerLeft": 	pointerPos.addX((float) ctr.isActive()); break;
					case "pointerRight": 	pointerPos.addX((float) ctr.isActive()); break;
					case "click": 			isPointerDown = true; break;
					
					case "toggle": if(!GameName.lockMovement) { GameName.guiManager.toggle("Test"); GameName.guiManager.toggle("Pause"); } break;
					
					case "placeTest": currentWorld.setCube(selectedCube.add(PhysicsUtil.getPosNextToFace(PhysicsUtil.getDirection(rot))), Cube.ColorfulTestCube.getId()); break;
					case "removeCube": currentWorld.setCube(selectedCube, Cube.Air.getId()); break;
					
					case "reloadChunks": ((VBOUpdateThread) GameName.threadManager.accessByName("VBO Thread")).forceUpdate();
					
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
		
		Client clt = GameName.client;			
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
