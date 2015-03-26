package com.GameName.Render.Types_2;

public interface IRenderable {
	public Render getRender();
	
	public default void render() {
		getRender().draw();
	}
	
	public void cleanUp();
	
	public default void requestRenderRebuild() {
		getRender().requestRenderRebuild();
	}
}
