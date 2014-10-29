package com.GameName.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Controller;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.GameName.Main.GameName;

public class ControlRecorder {
	
	public static int AXIS = 0;
	public static int BUTTON = 1;
	
	public static Control[] recordCombo(int type, boolean forward, Control exit, Controller c) {
		List<Control> combo = new ArrayList<Control>();
		
		while(exit.isActive() != 0D) {
			combo.add(record(type, "UsedInCombo", forward, true, c));
		}
		
		return (Control[]) combo.toArray();
	}
	
	public static Control record(int type, String name, boolean forward, boolean onlyOnce, Controller c) {
		Control contr = null;
		double[] data = pullData(type, forward, c);
		
		if((int) data[0] == BUTTON) 
			contr = new Control(type, (int) data[1], name, onlyOnce, forward, c); 
		else 
			contr = new Control(type, (int) data[1], name, true, true, 0.0, c);
		
		return contr;
	}
	
	public static double[] pullData(int type, boolean forward, Controller c) {
		double[] lookingAt;
		double[] newLookingAt;
		
		switch(type) {
			case Control.CONTROLLER: lookingAt = new double[c.getAxisCount() + c.getButtonCount()]; break;
			case Control.MOUSE: lookingAt = new double[Mouse.getButtonCount() + 2]; break;
			case Control.KEYBOARD: lookingAt = new double[Keyboard.getKeyCount()]; break;		
			default: lookingAt = null; break;
		}
		
		while(GameName.player.getAccess().isPointerDown()) {try{Thread.sleep(10);}catch(InterruptedException e){}}
		
		switch(type) {
			case Control.CONTROLLER: 
				for(int i = 0; i < c.getAxisCount(); i ++) lookingAt[i] = c.getAxisValue(i);
				for(int i = 0; i < c.getButtonCount(); i ++) lookingAt[i + c.getAxisCount()] = c.isButtonPressed(i) ? 1 : 0;
			break;
			
			case Control.MOUSE: 
				lookingAt[0] = Mouse.getX(); lookingAt[1] = Mouse.getY();
				for(int i = 0; i < Mouse.getButtonCount(); i ++) lookingAt[i + 2] = Mouse.isButtonDown(i) ? 1 : 0;
			break;
			
			case Control.KEYBOARD:
				for(int i = 0; i < Keyboard.getKeyCount(); i ++) lookingAt[i] = Keyboard.isKeyDown(i) ? 1 : 0;
			break;	
			
			default: break;
		}
		
		do {
			newLookingAt = lookingAt.clone();
			Display.processMessages();
			
			switch(type) {
				case Control.CONTROLLER: 
					for(int i = 0; i < c.getAxisCount(); i ++) lookingAt[i] = c.getAxisValue(i);
					for(int i = 0; i < c.getButtonCount(); i ++) lookingAt[i + c.getAxisCount()] = c.isButtonPressed(i) ? 1 : 0;
				break;
				
				case Control.MOUSE: 
					lookingAt[0] = Mouse.getX(); lookingAt[1] = Mouse.getY();
					for(int i = 0; i < Mouse.getButtonCount(); i ++) lookingAt[i + 2] = Mouse.isButtonDown(i) ? 1 : 0;
				break;
				
				case Control.KEYBOARD:
					for(int i = 0; i < Keyboard.getKeyCount(); i ++) lookingAt[i] = Keyboard.isKeyDown(i) ? 1 : 0;
				break;	
				
				default: break;
			}
			
		} while(Arrays.equals(lookingAt, newLookingAt)); //Arrays.equals(lookingAt, newLookingAt)
				
		int i = 0;
		for(i = 0; i < lookingAt.length; i ++) if(lookingAt[i] != newLookingAt[i]) break;
		
		double value = lookingAt[i] - newLookingAt[i];
		
		switch(type) {
			case Control.CONTROLLER: return new double[]{i >= c.getAxisCount() ? BUTTON : AXIS, i >= c.getAxisCount() ? i - c.getAxisCount() : i, value};
			case Control.MOUSE: return new double[]{i >= 2 ? BUTTON : AXIS, i >= 2 ? i - 2: i, value};
			case Control.KEYBOARD: return new double[]{BUTTON, i, forward ? value : -value};
			default: return new double[]{0, 0};
		}
	}
}
