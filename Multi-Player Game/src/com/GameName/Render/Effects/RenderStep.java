package com.GameName.Render.Effects;

public enum RenderStep {
	Undeclared(1), Primary(1), Secondary(2), Transparent(3), Final(4), Unknown(4), Compleated(5);
	
	private int stepIndex;
	RenderStep(int stepIndex) {
		this.stepIndex = stepIndex;
	}

	public int index() { return stepIndex; }
	public int stepIndex() { return stepIndex; }
	
	/**
	 * @return Returns whichever step comes first
	 */
	public static RenderStep compare(RenderStep step1, RenderStep step2) {
		return step1.index() > step2.index() ? step1 : step2;
	}
	
	public RenderStep compare(RenderStep step1) {
		return step1.index() > this.index() ? step1 : this;
	}
	
	public RenderStep nextStep() {
		return getByStep(index() + 1);
	}
	
	public static RenderStep getByStep(int step) {
		switch(step) {
			case 1:  return Primary;
			case 2:  return Secondary;
			case 3:  return Transparent;
			case 4:  return Final;
			case 5:  return Compleated;
			
			default: return Unknown;
		}
	}
}
