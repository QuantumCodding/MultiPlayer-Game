package com.GameName.Util;

import java.util.ArrayList;

public abstract class Registry<E> {
	private ArrayList<E> registered;

	protected Registry() {
		registered = new ArrayList<E>();
	}
	
	public void registerOBJ(E obj) {
		registered.add(obj);
	}
	
	//For Conclusion:
	
	@SuppressWarnings("unchecked")
	public E[] toArray() {
		return (E[]) registered.toArray(new Object[registered.size()]);
	}
	
	public ArrayList<E> getRegistry() {
		return registered;
	}
	
	protected abstract void register(E e);
	protected abstract void registrtionConcluded();
}
