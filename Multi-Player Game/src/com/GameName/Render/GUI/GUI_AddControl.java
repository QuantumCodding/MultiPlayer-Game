package com.GameName.Render.GUI;

import java.awt.Color;

import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import com.GameName.Input.Control;
import com.GameName.Input.ControlRecorder;
import com.GameName.Input.MultiControl;
import com.GameName.Main.GameName;

import static org.lwjgl.opengl.GL11.*;

public class GUI_AddControl extends GUI {
	private final float WIDTH = 625;
	private final float HEIGHT = 298;		
	
	private final float END_X = 90;	
	private final float END_Y = 80;
	private final float START_X = 90;
	private final float START_Y = 80;
	
	private final float X_SPEED = 1f;
	private final float Y_SPEED = 1f;
	
	private float x, y;
	private float endX, endY;
	private float startX, startY;
	private float width, height;
	private float xSpeed, ySpeed;
	
//	private float ratioX = 1;
//	private float ratioY = 1;
	
	private Texture[] textureID;
	
	private Control control, tempControl;	
	private boolean checkingIndavidual, indevidualFail, indevidualCheck;
	private boolean checkingFull, fullFail, fullCheck;
	private boolean polling;
	
	private String controlAction;
	
	private int controlID, type;
	private boolean onlyOnce, forward, isAxis;
	private double deadzone;
	
	private Control[] unfinshedControl;	
	private Controller controller;
	
	private int selectedControl, selectedInput, totalAdded;
	
	public GUI_AddControl() {
		textureID = new Texture[1];
		
		unfinshedControl = new Control[8];
		for(int i = 0; i < unfinshedControl.length; i ++) {
			unfinshedControl[i] = null;
		}
		
		comp = new GUIComponent[]{
				new GUIButton(0, "", 417, 92, 73, 69, true), // Set Control
				
				new GUIButton(1, "", 370, 163, 18, 18, true), // Next Input
				new GUIButton(2, "", 114, 163, 18, 18, true), // Last Input
				new GUIButton(3, "", 505, 138, 162, 23, true), // Set Deadzone
				new GUIText(4, "", 430, 125, new Color(102, 102, 102), 0, 30), // Control Input ID
				new GUIButton(5, "", 603, 183, 103, 39, true), // Test Individual
				new GUIButton(6, "", 499, 332, 103, 39, true), // Test Full
				new GUIButton(7, "", 603, 332, 103, 39, true), // Done Full
				
				new GUIRadioButton(8, "", 505, 92, 18, 18), // Only Once
				new GUIRadioButton(9, "", 505, 115, 18, 18), // Is Positive
				new GUIRadioButton(10, "", 480, 194, 18, 18), // Is Axis
				
				new GUITextField(11, "", 111, 100, 294, 51, 15, 5, 10), // Control control 
				new GUIText(12, "", 140, 160, Color.WHITE, 0, 17), // Control ID				
				
				new GUIButton(13, "", 103, 332, 68, 29, true), // Save
				new GUIButton(14, "", 173, 332, 87, 29, true), // Delete
				new GUIButton(15, "", 262, 332, 64, 29, true), // Edit
				new GUIButton(16, "", 327, 332, 69, 29, true), // New
				
			};
			
		genTextures();
		updateSize(Display.getWidth(), Display.getHeight());
		updateAll();
		
		sleepCount.setName("GUI_AddControl Sleep Counter");
		forward = true;
		selectedControl = -1;
		
		isOpen = false;
	}
	
	public void updateSize(int orgX, int orgY) {
		float ratioX = (float)(Display.getWidth() / orgX);
		float ratioY = (float)(Display.getHeight() / orgY);
		
		endX = END_X * ratioX;
		endY = END_Y * ratioY;
		
		startX = START_X * ratioX;
		startY = START_Y * ratioY;
		
		width = WIDTH * ratioX;
		height = HEIGHT * ratioY;
		
		xSpeed = X_SPEED * ratioX;
		ySpeed = Y_SPEED * ratioY;
		
//		this.ratioX = ratioX;
//		this.ratioY = ratioY;
		
		for(GUIComponent component : comp) {
			component.updateSize(ratioX, ratioY);
			component.setGui(this);
		}
	}

	public void open() {
		x = START_X;
		y = START_Y;
		
		GameName.lockMovement = true;
		isOpen = true;
	}

	public void updateAll() {
		if(endX > startX ? x < endX : x > endX) { x += xSpeed; for(GUIComponent component : comp) component.x += xSpeed; }
		if(endY > startY ? y < endY : y > endY) { y += ySpeed; for(GUIComponent component : comp) component.y += ySpeed; } 		
		
		if(checkingIndavidual && tempControl != null && tempControl.isActive() != 0.0) {
			indevidualFail = false;
			indevidualCheck = true;
			checkingIndavidual = false;
		}
		
		if(checkingFull && control != null && control.isActive() != 0.0) {
			fullFail = false;
			fullCheck = true;
			checkingFull = false;
		}
		
		controlAction = comp[11].text;
		
		if(GameName.click) {
			if(GameName.pointer.y > 247 && GameName.pointer.y < 320) {
				if(GameName.pointer.x > 107 && GameName.pointer.x < 697) {
					selectedControl = (GameName.pointer.x - 107) / 67;
				}
			}
		}
		
		for(GUIComponent component : comp) {
			component.update();
		}
	}
	
	private Thread sleepCount = new Thread() {
		public void run() {
			try {
				sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(checkingIndavidual) {
				indevidualFail = true;
				indevidualCheck = false;
				checkingIndavidual = false;
			}
			
			if(checkingFull) {
				fullFail = true;
				fullCheck = false;
				checkingFull = false;
			}
		}
	};
	
	protected void actions(int id) {
		switch(id) {
			case 0: 
				if(polling) break;
				if(type == Control.CONTROLLER && controller == null) break;
				if(type == Control.KEYBOARD && !Keyboard.isCreated()) break;
				if(type == Control.MOUSE && !Mouse.isCreated()) break;
				
				comp[4].text = ". . .";
				
				polling = true;
				new Thread() {public void run() {
					double[] data = ControlRecorder.pullData(type, forward, controller);
					
					((GUIRadioButton) comp[10]).setState((int) data[0] == ControlRecorder.AXIS);
					controlID = (int) data[1];
					deadzone = data[2] - (forward ? 0.000001 : -0.000001);
					
					comp[4].text = controlID + "";					
				}}.start(); 		
				
				polling = false;
			break;
			
			case 1: 				
				selectedInput ++; if(selectedInput > Controllers.getControllerCount() + 2) selectedInput = 0; 
				
				if(selectedInput < Controllers.getControllerCount()) {
					type = Control.CONTROLLER;
					controller = Controllers.getController(selectedInput);
				} else {
					type = (selectedInput - Controllers.getControllerCount()) == 0 ? Control.KEYBOARD : Control.MOUSE;
					controller = null;
				}
				
				setInputDisplay();
			break;
			
			case 2: 				
				selectedInput --; if(selectedInput < 0) selectedInput = Controllers.getControllerCount() + 2; 
				
				if(selectedInput < Controllers.getControllerCount()) {
					type = Control.CONTROLLER;
					controller = Controllers.getController(selectedInput);
				} else {
					type = (selectedInput - Controllers.getControllerCount()) == 0 ? Control.KEYBOARD : Control.MOUSE;
					controller = null;
				}
			
				setInputDisplay();
			break;
			
			case 3: deadzone = ControlRecorder.pullData(type, forward, controller)[3] - (forward ? 0.000001 : -0.000001); break;
			
			case 5: tempControl = getControlFromCurrent(); checkingIndavidual = true; new Thread(sleepCount).start(); break;
			case 6: control = new MultiControl(unfinshedControl, controlAction); checkingFull = true; new Thread(sleepCount).start(); break;
			case 7: control = new MultiControl(unfinshedControl, controlAction); close(); break;
			
			case 8: onlyOnce = ((GUIRadioButton) comp[8]).getState(); break;
			case 9: forward = !((GUIRadioButton) comp[9]).getState(); break;
			case 10: isAxis = ((GUIRadioButton) comp[10]).getState(); break;
			
			case 13: unfinshedControl[selectedControl] = getControlFromCurrent(); break;
			case 14: unfinshedControl[selectedControl] = null; totalAdded --; break;
			case 15: 
				tempControl = unfinshedControl[selectedControl] == null ? getControlFromCurrent() : unfinshedControl[selectedControl]; 
				
				controller = tempControl.getC();
				
				type = tempControl.getType(); controlID = tempControl.getId();
				deadzone = tempControl.getDeadZone();
				onlyOnce = tempControl.isOnlyOnce(); forward = tempControl.isForward(); isAxis = tempControl.isAxis();
		
				adjustWithCurrent();
			break;
			
			case 16: if(totalAdded >= 8) break;
				selectedControl = getUnusedSpot();
				
				type = 0; controlID = 0; deadzone = 0.0;
				onlyOnce = false; forward = true; isAxis = false;
				
				totalAdded ++;
				
				adjustWithCurrent();
			break;
			
			default: break;
		}
	}
	
	private void setInputDisplay() {
		if(type == Control.CONTROLLER) {
			comp[12].text = controller.getName();
		} else if(type == Control.KEYBOARD) {
			comp[12].text = "Keyboard";
		} else {
			comp[12].text = "Mouse";
		}
	}
	
	private void adjustWithCurrent() {
		((GUIRadioButton) comp[8]).setState(onlyOnce);
		((GUIRadioButton) comp[9]).setState(!forward);
		((GUIRadioButton) comp[10]).setState(isAxis);
		
		comp[4].text = controlID + "";
		
		String typeDisplay = "";
		
		if(type == Control.KEYBOARD && Keyboard.isCreated()) typeDisplay = "Keyboard";
		if(type == Control.MOUSE && Mouse.isCreated()) typeDisplay = "Mouse";
		if(type == Control.CONTROLLER && controller != null) typeDisplay = controller.getName();
		
		typeDisplay.substring(0, typeDisplay.length() > 25 ? 25 : typeDisplay.length());
		
		comp[12].text = typeDisplay.trim();
	}
	
	private int getUnusedSpot() {
		for(int i = 0; i < unfinshedControl.length; i ++) {
			System.out.println(unfinshedControl[i]);
			if(unfinshedControl[i] == null) {
				return i;
			}
		}
		
		return -1;
	}
	
	private Control getControlFromCurrent() {		
		if(isAxis) {
			return new Control(type, controlID, controlAction, isAxis, forward, deadzone, controller);
		} else {
			return new Control(type, controlID, controlAction, onlyOnce, forward, controller);
		}
	}
	
	public void render() {		
		glPushMatrix();		
			glColor4f(0, 0, 0, 0.75f);
			glRectf(0, 0, Display.getWidth(), Display.getHeight());
			
			glEnable(GL_TEXTURE_2D);
			textureID[0].bind();
			float r = 1f / (float)textureID[0].getImageHeight();
			
			glBegin(GL_QUADS);
				glColor4f(1, 1, 1, 1);
				
				//BackGround
				glTexCoord2f(0, 0);       		glVertex2f(x, y);
				glTexCoord2f(r * 625, 0); 		glVertex2f(x + width, y);
				glTexCoord2f(r * 625, r * 298); glVertex2f(x + width, y + height);
				glTexCoord2f(0, r * 298);       glVertex2f(x, y + height);
			glEnd();
			
			glBegin(GL_QUADS);
				if(checkingIndavidual) {
					glTexCoord2f(r * 19, r * 447);  glVertex2f(x + 582, 	 y + 12);
					glTexCoord2f(r * 37, r * 447);  glVertex2f(x + 582 + 18, y + 12);
					glTexCoord2f(r * 37, r * 465);  glVertex2f(x + 582 + 18, y + 12 + 18);
					glTexCoord2f(r * 19, r * 465);  glVertex2f(x + 582, 	 y + 12 + 18);
				} else if(indevidualCheck) {
					glTexCoord2f(r * 19, r * 576);   glVertex2f(x + 582, 	 y + 12);
					glTexCoord2f(r * 37, r * 576);  glVertex2f(x + 582 + 18, y + 12);
					glTexCoord2f(r * 37, r * 594);  glVertex2f(x + 582 + 18, y + 12 + 18);
					glTexCoord2f(r * 19, r * 594);   glVertex2f(x + 582, 	 y + 12 + 18);
				} else if(indevidualFail) {
					glTexCoord2f(r * 0, r * 576);   glVertex2f(x + 582, 	 y + 12);
					glTexCoord2f(r * 18, r * 576);  glVertex2f(x + 582 + 18, y + 12);
					glTexCoord2f(r * 18, r * 594);  glVertex2f(x + 582 + 18, y + 12 + 18);
					glTexCoord2f(r * 0, r * 594);   glVertex2f(x + 582, 	 y + 12 + 18);
				}
			glEnd();
			
			glBegin(GL_QUADS);
				if(checkingFull) {
					glTexCoord2f(r * 19, r * 447);  glVertex2f(x + 343, 	 y + 248);
					glTexCoord2f(r * 37, r * 447);  glVertex2f(x + 343 + 18, y + 248);
					glTexCoord2f(r * 37, r * 465);  glVertex2f(x + 343 + 18, y + 248 + 18);
					glTexCoord2f(r * 19, r * 465);  glVertex2f(x + 343, 	 y + 248 + 18);
				} else if(fullCheck) {
					glTexCoord2f(r * 19, r * 576);   glVertex2f(x + 343, 	 y + 248);
					glTexCoord2f(r * 37, r * 576);  glVertex2f(x + 343 + 18, y + 248);
					glTexCoord2f(r * 37, r * 594);  glVertex2f(x + 343 + 18, y + 248 + 18);
					glTexCoord2f(r * 19, r * 594);   glVertex2f(x + 343, 	 y + 248 + 18);
				} else if(fullFail) {
					glTexCoord2f(r * 0, r * 576);   glVertex2f(x + 343, 	 y + 248);
					glTexCoord2f(r * 18, r * 576);  glVertex2f(x + 343 + 18, y + 248);
					glTexCoord2f(r * 18, r * 594);  glVertex2f(x + 343 + 18, y + 248 + 18);
					glTexCoord2f(r * 0, r * 594);   glVertex2f(x + 343, 	 y + 248 + 18);
				}
			glEnd();
			
			float value = 0.0f;
			
			if(type == Control.KEYBOARD && Keyboard.isCreated()) {
				if(Keyboard.isKeyDown(controlID)) {
					value = forward ? 1 : -1;
				} else {
					value = 0;
				}
				
			} else if(type == Control.MOUSE && Mouse.isCreated()) {
				if(controlID == Control.MOUSE_X_AXIS) {
					value = forward ? Mouse.getX() - Control.oldX : -(Mouse.getX() - Control.oldX);
				} else if(controlID == Control.MOUSE_Y_AXIS) {
					value = forward ? Mouse.getY() - Control.oldY : -(Mouse.getY() - Control.oldY);
				} else if(Mouse.isButtonDown(controlID)) {
					value = forward ? 1 : -1;
				} else {
					value = 0;
				}
				
			} else if(type == Control.CONTROLLER && controller != null) {
				if(isAxis && controlID < controller.getAxisCount()) {
					value = forward ? controller.getAxisValue(controlID) : -controller.getAxisValue(controlID);
					
				} else if(controlID < controller.getButtonCount()) {
					if(controller.isButtonPressed(controlID)) {
						value = forward ? 1 : -1;
					} else {
						value = 0;
					}
				}
			}
			
			value = forward ? Math.max(value, 1) : Math.min(value, -1);
			value *= (289.0 / 2.0); value += (289.0 / 2.0);
			float cen = (289.0f / 2.0f); float min = Math.min(value, cen), max = Math.max(value, cen);	
			
			glBegin(GL_QUADS);
				glColor3f(1, 1, 1);
				
				//Powerbar
				glTexCoord2f(r * cen,  r * 299);    glVertex2f(min + 152,	197);
				glTexCoord2f(r * value, r * 299); 	glVertex2f(max + 152, 	197);
				glTexCoord2f(r * value, r * 314);	glVertex2f(max + 152, 	197 + 15);
				glTexCoord2f(r * cen,  r * 314);    glVertex2f(min + 152, 	197 + 15);
			glEnd();
			
			if(selectedControl > -1 && selectedControl < 8) {
				glBegin(GL_QUADS);
					glColor3f(1, 1, 1);
					
					//Selected Control
					glTexCoord2f(r * 0,  r * 795);     glVertex2f(111 + (selectedControl * 67),		 251);     
					glTexCoord2f(r * 67, r * 795); 	   glVertex2f(67 + 111 + (selectedControl * 67), 251);     
					glTexCoord2f(r * 67, r * 863);	   glVertex2f(67 + 111 + (selectedControl * 67), 251 + 67);
					glTexCoord2f(r * 0,  r * 863);     glVertex2f(111 + (selectedControl * 67), 	 251 + 67);
				glEnd();
			}
			
			for(int i = 0; i < unfinshedControl.length; i ++) {
				if(unfinshedControl[i] != null) {
					glBegin(GL_QUADS);
						glColor3f(1, 1, 1);
						
						//controls
						glTexCoord2f(r * 0, 	r * 595);       glVertex2f(116 + (i * 67),		256);
						glTexCoord2f(r * 57, r * 595); 			glVertex2f(57 + 116 + (i * 67), 256);
						glTexCoord2f(r * 57, r * 652);			glVertex2f(57 + 116 + (i * 67), 256 + 57);
						glTexCoord2f(r * 0, 	r * 652);      	glVertex2f(116 + (i * 67), 		256 + 57);
					glEnd();	
				} else {
//					System.out.println(i + " == null");
				}
			}
			
			glDisable(GL_TEXTURE_2D);

			super.render();
		glPopMatrix();
	}

	public Control getControl() {
		return control;
	}
	
	public void setControl(Control control) {
		this.control = control;
		
		if(control instanceof MultiControl) {
			unfinshedControl = ((MultiControl) control).getControls();
		} else {
			unfinshedControl = new Control[] {control};
		}
	}
	
	private void genTextures() {
		textureID[0] = getTexture("GUI/Add_Control_Parts");		
		Texture tex0 = textureID[0];
		
		int[] arr = {0, 0,315,	73,315,	73,384, 0,384};
		((GUIButton) comp[0]).setUpImages(tex0, tex0, tex0, arr, arr, arr); // Set Control
		
		arr = new int[]{0, 0,385,	18,385,	18,403, 0,403};
		((GUIButton) comp[1]).setUpImages(tex0, tex0, tex0, arr, arr, arr); // Next Input
		
		arr = new int[]{0, 0,404,	18,404,	18,422, 0,422};
		((GUIButton) comp[2]).setUpImages(tex0, tex0, tex0, arr, arr, arr); // Last Input
		
		arr = new int[]{0, 0,423, 162,423, 162,446, 0,446};
		((GUIButton) comp[3]).setUpImages(tex0, tex0, tex0, arr, arr, arr); // Set Deadzone
		
		arr = new int[]{0, 0,496, 103,496, 103,535, 0,535};
		((GUIButton) comp[5]).setUpImages(tex0, tex0, tex0, arr, arr, arr); // Test Individual
		((GUIButton) comp[6]).setUpImages(tex0, tex0, tex0, arr, arr, arr); // Test Full
		
		arr = new int[]{0, 0,536, 103,536, 103,575, 0,575};
		((GUIButton) comp[7]).setUpImages(tex0, tex0, tex0, arr, arr, arr); // Done
		
		arr = new int[]{0, 0,466, 68,466, 68,495, 0,495};
		((GUIButton) comp[13]).setUpImages(tex0, tex0, tex0, arr, arr, arr);// Save
		
		arr = new int[]{0, 0,653, 87,653, 87,682, 0,682};
		((GUIButton) comp[14]).setUpImages(tex0, tex0, tex0, arr, arr, arr);// Delete
		
		arr = new int[]{0, 0,683, 64,683, 64,712, 0,712};
		((GUIButton) comp[15]).setUpImages(tex0, tex0, tex0, arr, arr, arr);//Edit
		
		arr = new int[]{0, 0,713, 69,713, 69,742, 0,742};
		((GUIButton) comp[16]).setUpImages(tex0, tex0, tex0, arr, arr, arr);//New
		
		int[] arr2;
		
		arr = new int[] {0, 0,447, 18,447, 18,465, 0,465};
		arr2 = new int[]{0, 19,447, 37,447, 37,465, 19,465};
		
		((GUIRadioButton) comp[8]).setUpImages(tex0, tex0, arr, arr2); // Only Once
		((GUIRadioButton) comp[9]).setUpImages(tex0, tex0, arr, arr2); // Forward
		((GUIRadioButton) comp[10]).setUpImages(tex0, tex0, arr, arr2);// Is Axis
		
		arr = new int[] {0, 0,743, 294,743, 294,794, 0,794};
		((GUITextField) comp[11]).setUpImages(tex0, arr);
		((GUITextField) comp[11]).setUpFont(25, 0, Color.WHITE);
	}
}
