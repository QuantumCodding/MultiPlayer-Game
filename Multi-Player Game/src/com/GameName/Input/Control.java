package com.GameName.Input;

import org.lwjgl.input.Controller;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Control {
	public static final int CONTROLLER = 0;
	public static final int MOUSE = 1;
	public static final int KEYBOARD = 2;
	
	public static final int MOUSE_X_AXIS = 0;
	public static final int MOUSE_Y_AXIS = 1;
	
	public static float oldX, oldY;
	
	private int type;
	private int id;
	public String control;
	
	private boolean onlyOnce;
	private boolean down;
	
	private boolean isAxis;
	private boolean forward;
	private double deadZone;
	
	private Controller c;
	
	protected Control() {}
	
	public Control(int type, int id, String controle, boolean isAxis, boolean forward, double deadZone, Controller c) {
		this.type = type;
		this.id = id;
		this.control = controle;
		this.isAxis = isAxis;
		this.deadZone = deadZone;
		this.forward = forward;
		
		this.c = c;
	}
	
	public Control(int type, int id, String controle, boolean onlyOnce, boolean forward, Controller c) {
		this.type = type;
		this.id = id;
		this.control = controle;
		this.isAxis = false;
		this.deadZone = 0.0;
		this.forward = forward;
		this.onlyOnce = onlyOnce;
		
		this.c = c;
	}
	
	public double isActive() {
		switch(type) {
			case CONTROLLER: {
				if(isAxis) {
					double temp = c.getAxisValue(id);
					
					if(temp > deadZone && !forward)
						return temp;
					else if(temp < -deadZone && forward)
						return temp;
					else 
						return 0;
				} else {
					if(!onlyOnce || !c.isButtonPressed(id))
						down = false;
										
					if(c.isButtonPressed(id) && !down) {
						down = true;
						
						if(forward)
							return 1;
						else
							return -1;
					} else 
						return 0;
				}
			}
			
			case MOUSE: {
				if(isAxis) {
					switch(id) {
						case MOUSE_X_AXIS: {							
							if((Mouse.getX() - oldX) > deadZone && forward)
								return (Mouse.getX() - oldX);
							else if((Mouse.getX() - oldX) < -deadZone && !forward)
								return (Mouse.getX() - oldX);
							else
								return 0;
						}
						
						case MOUSE_Y_AXIS: {							
							if(((Display.getHeight() - Mouse.getY()) - oldY) > deadZone && forward)
								return ((Display.getHeight() - Mouse.getY()) - oldY);
							else if(((Display.getHeight() - Mouse.getY()) - oldY) < -deadZone && !forward)
								return ((Display.getHeight() - Mouse.getY()) - oldY);
							else
								return 0;
						}
					}
				} else {
					if(!onlyOnce || !Mouse.isButtonDown(id))
						down = false;
										
					if(Mouse.isButtonDown(id) && !down) {
						down = true;
						
						if(forward)
							return 1;
						else
							return -1;
					} else 
						return 0;
				} 
			} 
						
			case KEYBOARD: {
				if(!onlyOnce || !Keyboard.isKeyDown(id))
					down = false;
									
				if(Keyboard.isKeyDown(id) && !down) {
					down = true;
					
					if(forward)
						return 1;
					else
						return -1;
				} else 
					return 0;
			}
			
			default: return 0;
		}
	}

	public String toString() {
		if(isAxis)
			return 
				type + "," + 
				id + "," +
				control + "," +
				isAxis + "," +
				forward + "," +
				deadZone;
		else 
			return 
				type + "," + 
				id + "," +
				control + "," +
				forward + "," +
				onlyOnce;
		
	}
	
	public static void tick() {			
		oldX = Mouse.getX();
		oldY = Display.getHeight() - Mouse.getY();
	}

	public int getType() {
		return type;
	}

	public int getId() {
		return id;
	}

	public String getControl() {
		return control;
	}

	public boolean isOnlyOnce() {
		return onlyOnce;
	}

	public boolean isDown() {
		return down;
	}

	public boolean isAxis() {
		return isAxis;
	}

	public boolean isForward() {
		return forward;
	}

	public double getDeadZone() {
		return deadZone;
	}

	public Controller getC() {
		return c;
	}

}
