package com.GameName.Render.GUI;

import static org.lwjgl.opengl.GL11.*;

import com.GameName.Items.ItemStack;
import com.GameName.Render.GUI.GUIComponent;

public class GUISlot extends GUIComponent {
	private ItemStack item;
	
	private final float WIDTH = 35;
	private final float HEIGHT = 35;

	public GUISlot(int id, float x, float y) {
		super(id, null, x, y);	
		
		this.x = x;
		this.y = y;
	}
	
	public void updateSize(float ratioX, float ratioY) {
		width = WIDTH * ratioX;
		height = HEIGHT * ratioY;
	}

	public void render() { 		
		if(item != null) {	
			glEnable(GL_TEXTURE_2D);
			item.getItem().getTexture().bind();
			
			glBegin(GL_QUADS);
				glTexCoord2f(0, 0); glVertex2f(x, y);
				glTexCoord2f(1, 0); glVertex2f(x + width, y);
				glTexCoord2f(1, 1); glVertex2f(x + width, y + height);
				glTexCoord2f(0, 1); glVertex2f(x, y + height);
			glEnd();
			
			glDisable(GL_TEXTURE_2D);
		}
		
		if(isSelected()) {
			glColor4f(0f, 0f, 0f, 0.25f);
			glRectf(x, y, x + width, y + height);
		}			
		
	}

	public ItemStack getItem() {
		return item;
	}

	public void removeItemStack() {
		item = null;
	}
}
