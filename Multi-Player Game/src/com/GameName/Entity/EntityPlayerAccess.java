package com.GameName.Entity;

import java.util.List;

import com.GameName.Input.Control;
import com.GameName.Items.ItemStack;
import com.GameName.Main.GameName;
import com.GameName.Networking.Client;
import com.GameName.Networking.Packets.PacketPlayerInventorySize;
import com.GameName.Networking.Packets.PacketPlayerInventorySlot;
import com.GameName.Networking.Packets.PacketPlayerStats;
import com.GameName.Util.Vector3f;

public class EntityPlayerAccess extends EntityAccess {
	
	EntityPlayer player;
	
	protected EntityPlayerAccess(Entity entity) {
		super(entity);
		
		if(!(entity instanceof EntityPlayer)) 
			throw new IllegalArgumentException("EntityPlayerAccess must be instantiated with a instance of EntityPlayer");
		
		player = (EntityPlayer) entity;
	}
	
	public int getPower() {
		return player.power;
	}

	public double getMoney() {
		return player.money;
	}
	
	public ItemStack getInvItemStack(int i) {
		return player.inv[i];
	}
	
	public float getRenderDistance() {
		return player.renderDistance;
	}
	
	public Camera getCamera() {
		return player.cam;
	}
	
	public int getMaxJumps() {
		return player.maxJumps;
	}

	public int getJumpHeight() {
		return player.jumpHeight;
	}

	public int getJumpsLeft() {
		return player.jumpsLeft;
	}

	public boolean isGravityOn() {
		return player.gravityOn;
	}

	public int getLookSpeed() {
		return player.lookSpeed;
	}

	public int getLookSpeedUp() {
		return player.lookSpeedUp;
	}

	public int getSpeed() {
		return player.speed;
	}

	public List<Control> getControls() {
		return player.controls;
	}

	public int getMaxReach() {
		return player.maxReach;
	}

	public Vector3f getSelectedCube() {
		return player.selectedCube;
	}

	public void setMaxReach(int maxReach) {
		player.maxReach = maxReach;
	}

	public void setControls(List<Control> controls) {
		player.controls = controls;
	}

	public void setMaxJumps(int maxJumps) {
		player.maxJumps = maxJumps;
	}

	public void setJumpHeight(int jumpHeight) {
		player.jumpHeight = jumpHeight;
	}

	public void setJumpsLeft(int jumpsLeft) {
		player.jumpsLeft = jumpsLeft;
	}

	public void setGravityOn(boolean gravityOn) {
		player.gravityOn = gravityOn;
	}

	public void setLookSpeed(int lookSpeed) {
		player.lookSpeed = lookSpeed;
	}

	public void setLookSpeedUp(int lookSpeedUp) {
		player.lookSpeedUp = lookSpeedUp;
	}

	public void setSpeed(int speed) {
		player.speed = speed;
	}

	public void setRenderDistance(float renderDistance) {
		player.renderDistance = renderDistance;
	}
	
	private void setInv(ItemStack[] inv) {
		player.inv = inv;
	}
	
	public void setInvSize(int invSize, boolean send) {
		player.invSize = invSize;
		ItemStack[] inv2 = new ItemStack[invSize];
		
		for(int j = 0; j < player.inv.length; j ++) {
			inv2[j] = player.inv[j];
		}
		
		setInv(inv2);
		
		if(!send) return;
		
		Client clt = GameName.client;		
		if(clt != null && clt.isOnServer()) {
			clt.sendPacket(new PacketPlayerInventorySize(clt.getID(), invSize));
		}
	}

	public void setInvSlot(ItemStack slot, int slotID, boolean send) {
		player.inv[slotID] = slot;
		
		if(!send) return;
		
		Client clt = GameName.client;		
		if(clt != null && clt.isOnServer()) {
			clt.sendPacket(new PacketPlayerInventorySlot(clt.getID(), slotID, slot));
		}
	}
	
	public void setHealth(int health, boolean send) {
		entity.health = health;
		
		if(!send) return;
		
		Client clt = GameName.client;		
		if(clt != null && clt.isOnServer()) {
			clt.sendPacket(new PacketPlayerStats(clt.getID(), getHunger(), getHealth(), getMana(), getPower(), getMoney()));
		}
	}

	public void setMana(int mana, boolean send) {
		entity.mana = mana;
		
		if(!send) return;
		
		Client clt = GameName.client;		
		if(clt != null && clt.isOnServer()) {
			clt.sendPacket(new PacketPlayerStats(clt.getID(), getHunger(), getHealth(), getMana(), getPower(), getMoney()));
		}
	}

	public void setHunger(int hunger, boolean send) {
		entity.hunger = hunger;
		
		if(!send) return;
		
		Client clt = GameName.client;		
		if(clt != null && clt.isOnServer()) {
			clt.sendPacket(new PacketPlayerStats(clt.getID(), getHunger(), getHealth(), getMana(), getPower(), getMoney()));
		}
	}
	
	public void setPower(int power, boolean send) {
		player.power = power;
		
		if(!send) return;
		
		Client clt = GameName.client;		
		if(clt != null && clt.isOnServer()) {
			clt.sendPacket(new PacketPlayerStats(clt.getID(), getHunger(), getHealth(), getMana(), getPower(), getMoney()));
		}
	}

	public void setMoney(double money, boolean send) {
		player.money = money;
		
		if(!send) return;
		
		Client clt = GameName.client;		
		if(clt != null && clt.isOnServer()) {
			clt.sendPacket(new PacketPlayerStats(clt.getID(), getHunger(), getHealth(), getMana(), getPower(), getMoney()));
		}
	}
	
	public void setX(float x, boolean send) {
		setX(x);		
		player.playerMove(send);	
	}
	
	public void setY(float y, boolean send) {
		setY(y);	
		player.playerMove(send);
	}
	
	public void setZ(float z, boolean send) {
		setZ(z);		
		player.playerMove(send);	
	}
	
	public void moveX(float amount) {
		super.moveX(amount);
		
		player.playerMove(true);	
	}
	
	public void moveY(float amount) {
		super.moveY(amount);
		
		player.playerMove(true);	
	}
	
	public void moveZ(float amount) {
		super.moveZ(amount);
		
		player.playerMove(true);	
	}		
}
