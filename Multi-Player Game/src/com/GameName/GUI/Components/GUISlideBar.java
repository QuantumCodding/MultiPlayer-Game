package com.GameName.GUI.Components;

import com.GameName.Engine.GameEngine;

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
	public GUISlideBar(GameEngine eng, int id, float x, float y, float width, float height, float run, int max, int min, boolean vertical) {
		super(eng, id, x, y, width, height);
		
		this.max = max;
		this.min = min;
		
		this.vertical = vertical;
	}
	
	public void update() {		
		if(isSelected() && ENGINE.getPlayer().isPointerDown()) { 
			if(!hasFocus) last = (int) ENGINE.getPlayer().getPointerPos().y; hasFocus = true; 
		}
		
		if(hasFocus && !ENGINE.getPlayer().isPointerDown()) hasFocus = false;
		
		if(hasFocus) {
//			System.out.println(getId() + ": " + last + ", " + GameName.pointer.y + " / " + value);
			if(vertical) {
				if(last == (int) ENGINE.getPlayer().getPointerPos().y) {
					return;
				} else {
					int change = (last - (int) ENGINE.getPlayer().getPointerPos().y);

					setValue(getValue() + ((((float)(max - min)) / run) * (float) change));
				}
				
				last = (int) ENGINE.getPlayer().getPointerPos().y;
				
			} else {
				if(last == (int) ENGINE.getPlayer().getPointerPos().x) {
					return;
				} else {
					int change = (int) ENGINE.getPlayer().getPointerPos().x - last;
					setValue((float)(run / (float)(max - min)) * (float)change);
				}
				
				last = (int) ENGINE.getPlayer().getPointerPos().x;
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
