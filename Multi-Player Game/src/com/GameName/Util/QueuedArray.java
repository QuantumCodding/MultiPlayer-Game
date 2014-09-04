package com.GameName.Util;

import java.util.ArrayList;

public class QueuedArray<E> {
	
	private ArrayList<E> queue1;
	
	private ArrayList<E> elements;
	
	public QueuedArray() {
		reset();
	}
	
	public void addToQueue(E obj) {
		queue1.add(obj);
	}

	public void reset() {		
		queue1 = new ArrayList<E>();
		
		elements = new ArrayList<E>();
	}

	public void clearQueue() {
		 queue1 = new ArrayList<E>();
	}
	
	public void clearElements() {
		elements = new ArrayList<E>();
	}	

	public boolean queueContains(Object obj) {
		for(Object e : queue1) {
			if(e.equals(obj)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean elementsContains(Object obj) {
		for(Object e : elements) {
			if(e.equals(obj)) {
				return true;
			}
		}
		
		return false;
	}

	public boolean isQueueEmpty() {
		return queue1.isEmpty();
	}

	public boolean isElementsEmpty() {
		return elements.isEmpty();
	}

	public boolean removeFromQueue(Object obj) {
		return queue1.remove(obj);
	}
	
	public boolean removeFromElements(Object obj) {
		return elements.remove(obj);
	}

	public int queueSize() {
		return queue1.size();
	}
	
	public int elementsSize() {
		return elements.size();
	}
	
	public ArrayList<E> getElements() {
		return elements;
	}
	
	public E getFromElements(int index) {
		return elements.get(index);
	}
	
	public E get(int index) {
		return getFromElements(index);
	}
	
	public void pushQueueAndClear() {
		clearElements();
		
		elements = new ArrayList<E>(queue1);
		
		clearQueue();
	}
}
