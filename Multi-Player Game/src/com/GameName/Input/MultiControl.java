package com.GameName.Input;

public class MultiControl extends Control {
	Control[] cont;
	
	public MultiControl(Control[] cont, String controle) {
		this.cont = cont;
		this.control = controle;
	}
	
	public double isActive() {
		double toRep = 0;
		
		for(int i = 0; i < cont.length; i ++) {
			if(cont[i] == null) continue;
				
			double toAdd = cont[i].isActive();
			if(toAdd == 0.0) return 0.0;
			
			toRep += toAdd;
		}
		
		toRep /= cont.length;
		
		return toRep;
	}

	public Control[] getControls() {
		return cont;
	}
}
