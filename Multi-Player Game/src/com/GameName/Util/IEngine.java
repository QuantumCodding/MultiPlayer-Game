package com.GameName.Util;


public interface IEngine<E extends Object> {
		
	public void add(E obj);
	public void remove(E obj);
	
	public void step(float delta);
	
	public void cleanUp();
}
