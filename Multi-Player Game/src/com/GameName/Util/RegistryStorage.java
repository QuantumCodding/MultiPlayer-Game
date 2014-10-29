package com.GameName.Util;

import java.util.ArrayList;

public class RegistryStorage<E> {
	private ArrayList<Registry<E>> registries;
	private boolean isConcluded;
	
	public RegistryStorage() {
		registries = new ArrayList<Registry<E>>();
	}
	
	public void addRegistry(Registry<E> reg) {
		registries.add(reg);
	}
	
	public void register() {
		if(isConcluded) return;
		
		for(Registry<E> reg : registries) {
			for(E e : reg.toArray()) {
				reg.register(e);
			}
		}			
		
		if(!registries.isEmpty() && registries.get(0) != null) {
			registries.get(0).registrtionConcluded();
		}
		
		registries.clear();
		registries = null;
		
		isConcluded = true;
	}
}
