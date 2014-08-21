package com.GameName.Entity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.lwjgl.openal.AL10.*;
import org.lwjgl.opengl.Display;

import com.GameName.Input.Control;
import com.GameName.Items.ItemStack;
import com.GameName.Main.GameName;
import com.GameName.Networking.Client;
import com.GameName.Networking.Packets.PacketPlayerInventorySize;
import com.GameName.Networking.Packets.PacketPlayerInventorySlot;
import com.GameName.Networking.Packets.PacketPlayerLocation;
import com.GameName.Networking.Packets.PacketPlayerStats;
import com.GameName.Physics.Physics;
import com.GameName.Util.Vector3f;

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
	
	protected int lookSpeed;
	protected int lookSpeedUp;
	protected int speed;
		
	protected int maxReach;
	protected Vector3f selectedCube;
		
	protected void init() {
		access = new EntityPlayerAccess(this);
		
		resetCam();
		resetPlayer();
		
		alListener3f(AL_POSITION, pos.getX(), pos.getY(), pos.getZ());
	}
	
	public void resetCam() {//                                                       0.3f
		cam = new Camera(70, (float)Display.getWidth() / (float)Display.getHeight(), 0.3f, renderDistance);
	}
	
	private void resetPlayer() {
		System.out.println("Player Reset");
		
		invSize = 10;
		inv = new ItemStack[invSize];
		
		pos = new Vector3f(20, 110, 20);
		
		rot = new Vector3f(180, 0, 0);
		
		health = maxHealth;
		hunger = maxHunger;
		mana = maxMana;
		
		power = 0;
		money = 0.0;
		
		maxReach = 5;

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
//			selectedCube = Physics.getLookPosition(worldPos.getX(), worldPos.getY(), worldPos.getZ(), rotX, rotY, rotZ, currentWorld, maxReach);
		}		

		renderPos = pos.multiply(adjust);
		
		checkControls();
		
		cam.x = renderPos.getX(); cam.rotX = rot.getX();
		cam.y = renderPos.getY(); cam.rotY = rot.getY();
		cam.z = renderPos.getZ(); cam.rotZ = rot.getZ();

		if(onGround) {
			jumpsLeft = maxJumps;	
		}		
	}
	
	private void checkControls() {
		speed = 7; //15
		lookSpeed = 1;
		lookSpeedUp = 1;
				
		Vector3f oldPos = pos.clone();
		
		for(Control ctr : controls) {
			if(ctr.isActive() != 0.0){
				
				switch(ctr.control) {
					case "forward": if(!GameName.lockMovement) if(Physics.canMove(pos, Physics.FORWARD, currentWorld)) 	access.moveZ((float) (ctr.isActive() / speed)); 	break;						
					case "back": 	if(!GameName.lockMovement) if(Physics.canMove(pos, Physics.BACKWARD, currentWorld)) access.moveZ((float) (ctr.isActive() / speed)); 	break;		
					case "left": 	if(!GameName.lockMovement) if(Physics.canMove(pos, Physics.LEFT, currentWorld)) 	access.moveX((float) (ctr.isActive() / speed)); 	break;
					case "right":   if(!GameName.lockMovement) if(Physics.canMove(pos, Physics.RIGHT, currentWorld)) 	access.moveX((float) (ctr.isActive() / speed));     break;
					case "up": 		if(!GameName.lockMovement) if(Physics.canMove(pos, Physics.UP, currentWorld)) 		access.moveY(0.1f); 								break;		
					case "down": 	if(!GameName.lockMovement) if(Physics.canMove(pos, Physics.DOWN, currentWorld)) 	access.moveY(-0.1f); 								break;
					
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
					
					case "pointerUp": 		GameName.pointer.y += ctr.isActive(); break;
					case "pointerDown": 	GameName.pointer.y += ctr.isActive(); break;
					case "pointerLeft": 	GameName.pointer.x += ctr.isActive(); break;
					case "pointerRight": 	GameName.pointer.x += ctr.isActive(); break;
					case "click": 			GameName.click = true; break;
					
					case "toggle": if(!GameName.lockMovement) { GameName.guiManager.toggle("Test"); GameName.guiManager.toggle("Pause"); } break;
					
					default: System.err.println("Default Called: Player is not using control " + ctr.control); break;					
				} 				
			} else {
				switch(ctr.control) {
					case "click":  GameName.click = false; break;
					
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
	
	public static List<Control> loadControls(File f) throws FileNotFoundException {
		List<Control> controlls = new ArrayList<Control>();
		Scanner reader = new Scanner(f);
		String in = "";
		
		while(in != null) {
			try{in = reader.nextLine();}catch(NoSuchElementException e){in = null; continue;}
			String[] line = in.split(",");
			Control c;
			
			if(line.length == 6)
				c = new Control(
					Integer.parseInt(line[0]) == Control.CONTROLLER ? Control.CONTROLLER :
					Integer.parseInt(line[0]) == Control.MOUSE ? Control.MOUSE : Control.KEYBOARD, 
							Integer.parseInt(line[1]), 
							line[2], 
							Boolean.parseBoolean(line[3]), 
							Boolean.parseBoolean(line[4]), 
							Double.parseDouble(line[5]), 
							GameName.c
					);
			else
				c = new Control(
						Integer.parseInt(line[0]) == Control.CONTROLLER ? Control.CONTROLLER :
						Integer.parseInt(line[0]) == Control.MOUSE ? Control.MOUSE : Control.KEYBOARD, 
								Integer.parseInt(line[1]), 
								line[2], 
								Boolean.parseBoolean(line[3]),
								Boolean.parseBoolean(line[4]),
								GameName.c
						);
				
			controlls.add(c);
		}
		
		reader.close();
		return controlls;
	}
	
	
	public File saveControls(File f) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(f));
		
		for(Control cont : controls) {
			writer.write(cont.toString());
			writer.newLine();
		}
		
		writer.close();
		return f;
	}
}
