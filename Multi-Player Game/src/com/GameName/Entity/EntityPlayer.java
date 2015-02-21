package com.GameName.Entity;

import static org.lwjgl.openal.AL10.AL_ORIENTATION;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.alListener3f;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;

import com.GameName.Console.Base.Logger;
import com.GameName.Engine.GameEngine;
import com.GameName.Engine.ResourceManager.Materials;
import com.GameName.Input.Control;
import com.GameName.Items.ItemStack;
import com.GameName.Networking.Client;
import com.GameName.Networking.Packets.PacketPlayerInventorySize;
import com.GameName.Networking.Packets.PacketPlayerInventorySlot;
import com.GameName.Networking.Packets.PacketPlayerLocation;
import com.GameName.Networking.Packets.PacketPlayerStats;
import com.GameName.Physics.Collision.BoxShape;
import com.GameName.Util.Tag.DTGLoader;
import com.GameName.Util.Tag.TagGroup;
import com.GameName.Util.Vectors.Vector2f;
import com.GameName.Util.Vectors.Vector3f;

public class EntityPlayer extends Entity {
	private ItemStack[] inv;
	private int invSize;
		
	private Camera cam;
	private float renderDistance;
	
	private List<Control> controls;	
	private boolean lockMovement;	

	private int maxJumps;
	private int jumpHeight;
	private int jumpsLeft;
	
	private int maxReach;
	private Vector3f selectedCube;
	
	private Vector2f pointerPos;
	private boolean isPointerDown;
	
	public EntityPlayer(GameEngine eng) {
		super(new BoxShape(new Vector3f(0, 0, 0), new Vector3f(0.5f, 0.5f, 0.5f)), Materials.Human, eng);
		setNoClip(true);
	}
	
	protected void init() {
		resetCam();
		resetPlayer();
		
		alListener3f(AL_POSITION, getPosition().x, getPosition().y, getPosition().z);
	}
	
	public void resetCam() {//                                                       0.3f
		cam = new Camera(70, (float)Display.getWidth() / (float)Display.getHeight(), 0.3f, renderDistance);
		if(ENGINE.getRender() != null) {
			ENGINE.getRender().setUpPerspectives();
		}
	}
	
	public void resetPlayer() {
		super.reset();
		Logger.addLine("Player Reseting", Logger.SETUP);
		
		pointerPos = new Vector2f(0, 0);
		
		invSize = 10;
		inv = new ItemStack[invSize];
		
		setPosition(new Vector3f(20.5f, 125, 20.5f));		
		setRotation(new Vector3f(180, 0, 0));
		jumpsLeft = maxJumps;		
		
		maxReach = 50;
		selectedCube = new Vector3f(0, 0, 0);
		
		playerMove(true);
		
		Client clt = ENGINE.getClient();			
		if(clt != null && clt.isOnServer()) {
			clt.sendPacket(new PacketPlayerStats(clt.getID(), hunger, health, mana));			
			clt.sendPacket(new PacketPlayerInventorySize(clt.getID(), invSize));
			for(int i = 0; i < invSize; i ++) clt.sendPacket(new PacketPlayerInventorySlot(clt.getID(), i, inv[i]));
		}
	}
	
	public void update() {
		super.update();	
		
		checkControls();
		
		cam.x = getRenderPos().x; cam.rotX = getRotation().x;
		cam.y = getRenderPos().y; cam.rotY = getRotation().y;
		cam.z = getRenderPos().z; cam.rotZ = getRotation().z;
	}
	
	private void checkControls() {				
		Vector3f oldPos = (Vector3f) getPosition().clone();
		
		for(Control ctr : controls) {
			if(ctr.isActive() != 0.0){
				
				switch(ctr.control) {
					case "forward": if(!lockMovement) moveZ((float) /*(ctr.isActive() / speed)*/ 15); break;						
					case "back": 	if(!lockMovement) moveZ((float)-/*(ctr.isActive() / speed)*/ 15); break;		
					case "left": 	if(!lockMovement) moveX((float)-/*(ctr.isActive() / speed)*/ 15); break;
					case "right":   if(!lockMovement) moveX((float) /*(ctr.isActive() / speed)*/ 15); break;
					case "up": 		if(!lockMovement) moveY((float) /*(ctr.isActive() / speed)*/ 15); break;		
					case "down": 	if(!lockMovement) moveY((float)-/*(ctr.isActive() / speed)*/ 15); break;
					
					case "lookUp": 	if(!lockMovement)  /*if(entityPhisics(0))*/ rotateX((float) (ctr.isActive() / 1f)); break;				
					case "lookDown": if(!lockMovement) /*if(entityPhisics(0))*/ rotateX((float) (ctr.isActive() / 1f)); break;			
					case "lookLeft": if(!lockMovement) /*if(entityPhisics(0))*/ rotateY((float) (-ctr.isActive() / 1f)); break;
					case "lookRight": if(!lockMovement)/*if(entityPhisics(0))*/ rotateY((float) (-ctr.isActive() / 1f));	break;
					
					case "jump": 					
						if(!lockMovement) if(jumpsLeft > 0) { 
							moveY(jumpHeight);
							jumpsLeft --;
						}						
					break;
										
					case "resetPlayer": if(!lockMovement) resetPlayer(); break;
					
					case "pointerUp": 		pointerPos.y += (float) ctr.isActive(); break;
					case "pointerDown": 	pointerPos.y += (float) ctr.isActive(); break;
					case "pointerLeft": 	pointerPos.x += (float) ctr.isActive(); break;
					case "pointerRight": 	pointerPos.x += (float) ctr.isActive(); break;
					case "click": 			isPointerDown = true; break;
					
					case "showLog": 
						ENGINE.getConsole().setVisible(true);
						ENGINE.getConsole().setTab(ENGINE.getConsole().getLog());
						ENGINE.getConsole().toFront(); ENGINE.getConsole().repaint();
						ENGINE.getConsole().getLog().focusOnInputField();
					break;
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
						
			if(!oldPos.equals(getPosition())) {
				playerMove(true);
			}
		}
	}
		
	public void playerMove(boolean send) {
		alListener3f(AL_POSITION, getPosition().x, getPosition().y, getPosition().z);
		alListener3f(AL_ORIENTATION, getRotation().x,getRotation().y, getRotation().z);
		
		if(!send) return;		
		Client clt = ENGINE.getClient();			
		if(clt != null && clt.isOnServer()) {
			clt.sendPacket(new PacketPlayerLocation(clt.getID(), getPosition()));
		}	
	}
	
	public Vector3f getSelectedCube() { return selectedCube; }
	public Vector2f getPointerPos() { return pointerPos; }	
	public boolean isPointerDown() { return isPointerDown; }
	public Camera getCamera() { return cam; }	
	public boolean canPlayerMove() { return !lockMovement; }	
	
	public void setMaxReach(int maxReach) { this.maxReach = maxReach; }
	public void setControls(List<Control> controls) { this.controls = controls; }
	public void setMaxJumps(int maxJumps) { this.maxJumps = maxJumps; }
	public void setJumpHeight(int jumpHeight) { this.jumpHeight = jumpHeight; }
	public void setJumpsLeft(int jumpsLeft) { this.jumpsLeft = jumpsLeft; }
	public void setCanMove(boolean canMove) { this.lockMovement = !canMove; }
	private void setInv(ItemStack[] inv) { this.inv = inv; }
	public void setRenderDistance(float renderDistance) {
		this.renderDistance = renderDistance;
		resetCam();
	}
		
	public void setInvSize(int invSize, boolean send) {
		this.invSize = invSize;
		ItemStack[] inv2 = new ItemStack[invSize];
		
		for(int j = 0; j < inv.length; j ++) {
			inv2[j] = inv[j];
		}
		
		setInv(inv2);
		
		if(!send) return;
		
		Client clt = ENGINE.getClient();		
		if(clt != null && clt.isOnServer()) {
			clt.sendPacket(new PacketPlayerInventorySize(clt.getID(), invSize));
		}
	}

	public void setInvSlot(ItemStack slot, int slotID, boolean send) {
		inv[slotID] = slot;
		
		if(!send) return;
		
		Client clt = ENGINE.getClient();	
		if(clt != null && clt.isOnServer()) {
			clt.sendPacket(new PacketPlayerInventorySlot(clt.getID(), slotID, slot));
		}
	}
	
	public void setHealth(int health, boolean send) {
		super.setHealth(health); if(send) sendStats();
	}

	public void setMana(int mana, boolean send) {
		super.setMana(mana); if(send) sendStats();	
	}

	public void setHunger(int hunger, boolean send) {
		super.setHunger(hunger); if(send) sendStats();		
	}
	
	private void sendStats() {
		Client clt = ENGINE.getClient();		
		if(clt != null && clt.isOnServer()) {
			clt.sendPacket(new PacketPlayerStats(clt.getID(), hunger, health, mana));
		}
	}
	
	public static List<Control> loadControls(File f) {
		List<Control> controlls = new ArrayList<Control>();
		
		try {
			ArrayList<TagGroup> groups = DTGLoader.readAll(DTGLoader.getInputStream(f));
			
			for(TagGroup group : groups) {
				controlls.add(Control.getControlFormTagGroup(group));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return controlls;
	}
	
	public File saveControls(File f) throws IOException {
		ArrayList<TagGroup> tagLines = new ArrayList<>();
		
		for(Control control : controls) {
			tagLines.add(control.getTagGroup());
		}
		
		DTGLoader.writeAll(DTGLoader.getOutputStream(f), tagLines);		
		return f;
	}
}
