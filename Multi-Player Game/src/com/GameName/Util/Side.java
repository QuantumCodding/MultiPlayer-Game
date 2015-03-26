package com.GameName.Util;

public enum Side {
	LeftFace(0), FrontFace(1), RightFace(2),
	BackFace(3), TopFace(4), BottomFace(5);
	
	private final int arrayIndex;
	Side(int arrayIndex) {
		this.arrayIndex = arrayIndex;
	}
	
	public int index() { 
		return arrayIndex;
	}
	
	public static Side getSide(int index) {
		return values()[index];
	}
	
	public Side getOpposite() {
		switch(this) {
			case BackFace: return FrontFace;
			case BottomFace: return TopFace;
			case FrontFace: return BackFace;
			case LeftFace: return RightFace;
			case RightFace: return LeftFace;
			case TopFace: return BottomFace;
			
			default: return null;
		}
	}
	
	public Side getNext() {
		return values()[(arrayIndex + 1) % values().length];
	}
	
	public Side getPrevious() {
		return values()[arrayIndex == 0 ? values().length : arrayIndex - 1];
	}
}
