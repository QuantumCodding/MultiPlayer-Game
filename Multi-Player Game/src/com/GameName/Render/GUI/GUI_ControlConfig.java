package com.GameName.Render.GUI;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import com.GameName.Entity.EntityPlayer;
import com.GameName.Input.Control;
import com.GameName.Main.GameName;

public class GUI_ControlConfig extends GUI {

	private final float HEIGHT = 500;
	private final float WIDTH = 700;	
	
	private final float END_X = 50;	
	private final float END_Y = 50;
	private final float START_X = 50;
	private final float START_Y = 50;
	
	private final float X_SPEED = 1f;
	private final float Y_SPEED = 1f;
	
	private float x, y;
	private float endX, endY;
	private float startX, startY;
	private float width, height;
	private float xSpeed, ySpeed;
	
	private boolean fOpen;	
	private Texture[] textureID;
	
	private Set<Control> controls;
	private List<GUI_Control> controlsGUI;
	private int selectedControl;
	private boolean getControlToAdd;
	
	public GUI_ControlConfig() {
		textureID = new Texture[1];
		
		comp = new GUIComponent[]{//X Y W H
				new GUIButton(0, "", 90, 67, 47, 45, true),	// Back			
				new GUIButton(1, "", 70, 461, 100, 39, true), // New
				new GUIButton(2, "", 192, 461, 148, 39, true), // Delete
				new GUIButton(3, "", 362, 461, 148, 39, true), // Refresh
				new GUIButton(4, "", 535, 461, 148, 39, true), // Cancel
				
				new GUISlideBar(5, 693, 488, 53, 19, 353, 1, 0, true),
			};
		
		controls = new HashSet<Control>();
		controlsGUI = new ArrayList<GUI_Control>();
		try {refreshControls();} catch(FileNotFoundException e) {e.printStackTrace();}
		
		genTextures();
		
		updateSize(Display.getWidth(), Display.getHeight());
		updateAll();
		
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
		
		for(GUIComponent component : comp) {
			component.setGui(this);
			component.updateSize(ratioX, ratioY);
		}
	}

	public void open() {
		x = START_X;
		y = START_Y;
		
		for(GUIComponent component : comp) component.reset();
		for(GUI gui : controlsGUI) gui.open();
		
		isOpen = true;
	}
	
	public void close() {
		super.close();
		
		for(GUI gui : controlsGUI) gui.close();
	}

	public void updateAll() {
		if(fOpen) {
			if(endX > startX ? x < endX : x > endX) { x += xSpeed; for(GUIComponent component : comp) {component.x += xSpeed; if(component instanceof GUISlideBar) {GUISlideBar s = (GUISlideBar) component; s.setUseX(s.x);}}}
			if(endY > startY ? y < endY : y > endY) { y += ySpeed; for(GUIComponent component : comp) {component.y += ySpeed; if(component instanceof GUISlideBar) {GUISlideBar s = (GUISlideBar) component; s.setUseY(s.y);}}}	
		}
						
		for(GUIComponent component : comp) {
			component.update();
		}
		
		int i = 0;
		for(GUI_Control gui : controlsGUI) {
			gui.updateAll();
			
			
			selectedControl = gui.isSelected() ? i : selectedControl;
			
			i ++;
		}
	}
	
	protected void actions(int id) {		
		if(GameName.guiManager.accessByName("Add Control").isOpen) {
			return;
		}
		
		if(getControlToAdd && !GameName.guiManager.accessByName("Add Control").isOpen) {
			Control toAdd = ((GUI_AddControl) GameName.guiManager.accessByName("Add Control")).getControl();
			controls.add(toAdd);
			
			float yAdj = (controls.size() * 80) - (80 * ((GUISlideBar) comp[5]).getValue());
			controlsGUI.add(new GUI_Control(x + 80, y + 91 + yAdj, toAdd));
			getControlToAdd = false;
		}
		
		try {
			switch(id) {	
				case 0: 
					GameName.player.getAccess().setControls(new ArrayList<Control>(controls)); 
					GameName.player.saveControls(new File("res/option/controls.dat"));
					
					this.close(); GameName.guiManager.accessByName("Pause").open();
				break;
				
				case 1: 
					GameName.guiManager.accessByName("Add Control").open();
					getControlToAdd = true;
				break;
				
				case 2: if(selectedControl != -1) {
							controls.remove(controls.toArray(new Control[controls.size()])[selectedControl]);
							controlsGUI.remove(selectedControl);
							
							selectedControl = -1;
						}				
					break;
				
				case 3: refreshControls(); break;
				case 4: this.close(); GameName.guiManager.accessByName("Pause").open(); break;
									
				default: break;
			}
		} catch(Exception e) {e.printStackTrace();}
	}
	
	private void refreshControls() throws FileNotFoundException {
		GameName.player.getAccess().setControls(EntityPlayer.loadControls(new File("res/option/controls.dat")));
		controlsGUI = new ArrayList<GUI_Control>();
		
		int i = 0;
		for(Control c : GameName.player.getAccess().getControls()) {
			controls.add(c);
			
			float yAdj = (i * 80);
			controlsGUI.add(new GUI_Control(x + 80, y + 91 + yAdj, c));
			
			i ++;
		}
		
		((GUISlideBar) comp[5]).setMax(controls.size());
		((GUISlideBar) comp[5]).setValue(controls.size());
	}
		
	public void render() {		
		glPushMatrix();		
			glEnable(GL_TEXTURE_2D);
			textureID[0].bind();
			float r = 1f / (float)textureID[0].getImageHeight();
			
			glBegin(GL_QUADS);
				glColor3f(1, 1, 1);
				
				//ControlsBackGround
				glTexCoord2f(0, r * 572);      		glVertex2f(x + 20,  y + 91);
				glTexCoord2f(r * 613, r * 572); 	glVertex2f(x + 633, y + 91);
				glTexCoord2f(r * 613, r * 882); 	glVertex2f(x + 633, y + 401);
				glTexCoord2f(0, r * 882);     		glVertex2f(x + 20,  y + 401);
				
			glEnd();
			
			int i = -1;
			for(GUI_Control gui : controlsGUI) {
				i ++; float yAdj = (i * 80) - (80 * (controls.size() - ((GUISlideBar) comp[5]).getValue()));
				gui.setY(151 + yAdj);
				
				if(gui.getY() > 61 && gui.getY() < 451) { gui.render();}//480								
			}
			
			textureID[0].bind();
			
			glBegin(GL_QUADS);				
				glColor3f(1, 1, 1);
				
				//BackGround
				glTexCoord2f(0, 0);      		glVertex2f(x, y);
				glTexCoord2f(r * 700, 0); 		glVertex2f(x + width, y);
				glTexCoord2f(r * 700, r * 500); glVertex2f(x + width, y + height);
				glTexCoord2f(0, r * 500);     	glVertex2f(x, y + height);
				
			glEnd();
			
			glDisable(GL_TEXTURE_2D);		
			
			super.render();
		glPopMatrix();
	}

	private void genTextures() {
		textureID[0] = getTexture("GUI/Controls_Menu_Parts");
		
		GUIButton b = (GUIButton) comp[0];
		int[] arr = {0, 149,923, 223,923, 223,968, 149,968}; 
		b.setUpImages(textureID[0], textureID[0], textureID[0], arr, arr, arr);
		
		b = (GUIButton) comp[1];
		arr = new int[] {0, 0,883, 100,883, 100,922, 0,922}; 
		b.setUpImages(textureID[0], textureID[0], textureID[0], arr, arr, arr);
		
		b = (GUIButton) comp[2];
		arr = new int[] {0, 0,923, 148,923, 148,962, 0,962}; 
		b.setUpImages(textureID[0], textureID[0], textureID[0], arr, arr, arr);
		
		b = (GUIButton) comp[3];
		arr = new int[] {0, 0,963, 148,963, 148,1002, 0,1002}; 
		b.setUpImages(textureID[0], textureID[0], textureID[0], arr, arr, arr);
		
		b = (GUIButton) comp[4];
		arr = new int[] {0, 101,883, 249,883, 249,922, 101,922}; 
		b.setUpImages(textureID[0], textureID[0], textureID[0], arr, arr, arr);
		
		GUISlideBar s = (GUISlideBar) comp[5];
		arr = new int[] {0, 0,1003, 52,1003, 52,1022, 0,1022}; 
		s.setUpImages(textureID[0], arr);
		
	}	
}
