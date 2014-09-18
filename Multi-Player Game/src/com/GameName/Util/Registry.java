package com.GameName.Util;

import java.util.ArrayList;

public abstract class Registry<E> {
	private static ArrayList<Registry<?>> registries;
	protected static boolean isConcluded;
	
	static {
		registries = new ArrayList<Registry<?>>();
	}
	
	public static void addRegistry(Registry<?> registry) {
		registries.add(registry);
	}
		
	protected static ArrayList<Registry<?>> getRegistries() {
		return registries;
	}
	
	public static boolean isConcluded() {
		return isConcluded;
	}
	
	private ArrayList<E> registered;
	
	protected Registry() {
		registered = new ArrayList<E>();
	}
	
	public void register(E obj) {
		registered.add(obj);
	}
	
	@SuppressWarnings("unchecked")
	public E[] toArray() {
		return (E[]) registered.toArray(new Object[registered.size()]);
	}
	
	public ArrayList<E> getRegistry() {
		return registered;
	}
}
