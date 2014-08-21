package com.GameName.Input;

import java.util.Arrays;

public class Combo {

	public Control[] combo;
	
	public Combo(Control[] combo) {
		this.combo = combo;
	}
	
	public boolean equals(Object check) {
		if(check == null) return false;
		if(check == this) return true;
		if(!(check instanceof Combo)) return false;
		
		Combo cmb = (Combo) check;
		
		return Arrays.equals(cmb.combo, combo);
	}
	
 
}
