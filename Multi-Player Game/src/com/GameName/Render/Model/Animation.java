package com.GameName.Render.Model;

import java.util.ArrayList;

import com.GameName.Util.Vectors.Vector3f;

public class Animation {
	private String name;
	private int[] affectedFaces;
	
	private int stepIndex;
	private ArrayList<AnimationStep> steps;

	private Vector3f translation, rotation, scale;
	private boolean resetOnCompletion, isRunning;
	
	public Animation(String name, boolean resetOnCompletion, int[] affectedFaces) {
		steps = new ArrayList<AnimationStep>();
		this.resetOnCompletion = resetOnCompletion;
		
		this.name = name;
		this.affectedFaces = affectedFaces;
	}
	
	public void start() {
		isRunning = true;
	}
	
	public boolean step() {
		if(!isRunning) return false;
		
		if(!(stepIndex < steps.size())) {
			if(resetOnCompletion) {
				reset();
			} else {
				return false;
			}
		}
		
		AnimationStep step = steps.get(stepIndex);
		
		switch(step.getType()) {		
			case Rotation: rotation.addAndSet(step.getStepValue()); break;				
			case Scale: scale.addAndSet(step.getStepValue()); break;
			case Translation: translation.addAndSet(step.getStepValue()); break;
				
			default: break;		
		}
		
		step.step();
		if(step.isDone()) {
			stepIndex ++;
		}
		
		return true;
	}
	
	public void reset() {
		stepIndex = 0;
		
		translation = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
		scale = new Vector3f(0, 0, 0);
	}
	
	public void addStep(AnimationStep step) {
		steps.add(step);
	}
	
	public String getName() {
		return name;
	}

	public int[] getAffectedFaces() {
		return affectedFaces;
	}

	protected Vector3f getTranslation() {
		return translation;
	}

	protected Vector3f getRotation() {
		return rotation;
	}

	protected Vector3f getScale() {
		return scale;
	}
	
	protected Vector3f[] getEffects() {
		return new Vector3f[] {translation, rotation, scale};
	}

	public void addFace(int face) {
		int[] newFaces = new int[affectedFaces.length + 1];
		System.arraycopy(affectedFaces, 0, newFaces, 0, affectedFaces.length);
		newFaces[affectedFaces.length] = face;
		
		affectedFaces = newFaces;
	}
	
	public void setAffectedFaces(int[] affectedFaces) {
		this.affectedFaces = affectedFaces;
	}

	public class AnimationStep {
		private Type type;
		
		private Vector3f current;
		private Vector3f endValue;
		private Vector3f stepValue;
	
		public AnimationStep(Type type, Vector3f value) {
			this(type, value, value);
		}
		
		public AnimationStep(Type type, Vector3f endValue, Vector3f stepValue) {
			this.type = type;
			this.endValue = endValue;
			this.stepValue = stepValue;
			
			current = new Vector3f(0, 0, 0);
		}

		public boolean isDone() {
			return current.abs().greaterThen(endValue.abs()) || current.equaleTo(endValue);
		}
		
		public void step() {
			current.add(stepValue);
		}

		public Type getType() {
			return type;
		}

		public Vector3f getEndValue() {
			return endValue;
		}

		public Vector3f getStepValue() {
			return stepValue;
		}
	}
	
	public enum Type {
		Rotation, Translation, Scale
	}
}
