package com.GameName.Render.Types_2;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import com.GameName.Engine.GameEngine;
import com.GameName.Render.Effects.RenderProperties;
import com.GameName.Render.Effects.RenderProperties.RenderPropertiesBuilder;
import com.GameName.Render.Effects.Shader;
import com.GameName.Render.Effects.Texture;
import com.GameName.Util.Vectors.Vector3f;

public abstract class Render implements IRenderable {
	private Texture texture;
	private Shader shader;
	
	private RenderProperties properties;
	private Transform transform;
	
	protected boolean isRenderRebuildRequested;
	protected boolean areBufferIdsGenerated;
	
	protected int[] bufferIds;
	protected final GameEngine ENGINE;
	
	public Render(GameEngine eng) {
		ENGINE = eng;
		properties = new RenderPropertiesBuilder().build();
		transform = new Transform();
	}
	
	public void draw() {
		preRender();
		preformRender();
		postRender();
	}
	
	protected void preRender() {
		glPushMatrix();	
		properties.apply();
		
		if(!areBufferIdsGenerated) {
			generateBufferIds();
			areBufferIdsGenerated = true;
		}
		
		if(isRenderRebuildRequested) {
			buildRender();
			isRenderRebuildRequested = false;
		}
		
		if(texture != null) texture.bind(); else Texture.unbind();
//		if(shader != null) shader.bind(); else Shader.unbind();
		
//		transform.apply();
	}
	
	protected abstract void preformRender();
	
	protected void postRender() {
		properties.reset();
		glPopMatrix();
	}
	
	public abstract void buildRender();
	public abstract void generateBufferIds();
	
	public void requestRenderRebuild() {
		isRenderRebuildRequested = true;
	}
	
	public boolean isRenderRebuildRequest() {
		return isRenderRebuildRequested;
	}
	
	public Shader getShader() { return shader; }
	public Texture getTexture() { return texture; }
	public Transform getTransform() { return transform; }
	public RenderProperties getRenderProperties() { return properties; } 
	
	public void setShader(Shader shader) { this.shader = shader; }	
	public void setTexture(Texture texture) { this.texture = texture; }
	public void setTransform(Transform transform) { this.transform = transform; }
	public void setRenderProperties(RenderProperties properties) { this.properties = properties; } 
	
	public static class Transform {
		private Vector3f center;
		
		private Vector3f translation;
		private Vector3f rotation;
		private Vector3f scale;
		
		public Transform() {
			center = new Vector3f();
			
			translation = new Vector3f();
			rotation = new Vector3f();
			scale = new Vector3f();
		}
		
		public void apply() {
			glTranslatef(center.x, center.y, center.z);			
			glScalef(scale.x, scale.y, scale.z);
			
			glRotatef(rotation.x, 1, 0, 0);
			glRotatef(rotation.y, 0, 1, 0);
			glRotatef(rotation.z, 0, 0, 1);
			
			glTranslatef(translation.x, translation.y, translation.z);
		}
		
		public Vector3f getCenter() { return center; }
		public Transform setCenter(Vector3f center) { this.center = center; return this; }

		public Vector3f getTranslation() { return translation; }
		public Vector3f getRotation() { return rotation; }
		public Vector3f getScale() { return scale; }

		public Transform setTranslation(Vector3f translation) { this.translation = translation; return this; }
		public Transform setRotation(Vector3f rotation) { this.rotation = rotation; return this; }
		public Transform setScale(Vector3f scale) { this.scale = scale; return this; }
	}
	
	public Render getRender() { return this; }	
}
