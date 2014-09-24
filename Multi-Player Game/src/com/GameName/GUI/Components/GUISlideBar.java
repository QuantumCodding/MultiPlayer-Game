package com.GameName.GUI.Components;

import com.GameName.Main.GameName;

public class GUISlideBar extends GUIComponent {
	private float run;
	private int max, min;
	private boolean vertical;
	private float value;
	
	private int last = 0;
	private boolean hasFocus;
	
	/**
	 * A slider that travel across either the X or Y axis and returns a vaue baced on its position
	 * 
	 * @param ID 	 The id in the component array
	 * @param X 	 The starting x position for drawing
	 * @param Y 	 The starting y position for drawing
	 * @param Width  The width of the slider 
	 * @param Height The height of the slider
	 * @param Run 	 The distance that the slider travels across the axis in pixels
	 * @param Max	 The maximum value the slider can have
	 * @param Min	 The minimum value the slider can have
	 * @param Vertical Is the slider vertical (true) or horizontal (false)
	 */
	public GUISlideBar(int id, float x, float y, float width, float height, float run, int max, int min, boolean vertical) {
		super(id, x, y, width, height);
		
		this.max = max;
		this.min = min;
		
		this.vertical = vertical;
	}
	
	public void update() {		
		if(isSelected() && GameName.player.getAccess().isPointerDown()) { 
			if(!hasFocus) last = (int) GameName.player.getAccess().getPointer().getY(); hasFocus = true; 
		}
		
		if(hasFocus && !GameName.player.getAccess().isPointerDown()) hasFocus = false;
		
		if(hasFocus) {
//			System.out.println(getId() + ": " + last + ", " + GameName.pointer.y + " / " + value);
			if(vertical) {
				if(last == (int) GameName.player.getAccess().getPointer().getY()) {
					return;
				} else {
					int change = (last - (int) GameName.player.getAccess().getPointer().getY());

					setValue(getValue() + ((((float)(max - min)) / run) * (float) change));
				}
				
				last = (int) GameName.player.getAccess().getPointer().getY();
				
			} else {
				if(last == (int) GameName.player.getAccess().getPointer().getX()) {
					return;
				} else {
					int change = (int) GameName.player.getAccess().getPointer().getX() - last;
					setValue((float)(run / (float)(max - min)) * (float)change);
				}
				
				last = (int) GameName.player.getAccess().getPointer().getX();
			}
			
			activate();
		} else {		
			setValue(getValue());
		}
	}
	
	public float getValue() {
		return value;
	}
	
	public float getMax() {
		return max;
	}
	
	public float getMin() {
		return min;
	}
	
	public void setValue(float v) {
		value = v < max ? v > min ? v : min : max;
		
		if(vertical) {
			setY(getY() - ((float)(run / (float)(max - min)) * value));
			setX(getX());
		} else {
			setX(getX() + ((run / (float)(max - min)) * value));
			setY(getY());
		}
	}

	public void setMax(int max) {
		this.max = max;
	}

	public void setMin(int min) {
		this.min = min;
	}
}
