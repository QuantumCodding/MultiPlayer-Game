package com.GameName.Render.Types;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import com.GameName.Main.GameName;
import com.GameName.Render.Effects.Shader;
import com.GameName.Render.Effects.Texture;
import com.GameName.Util.Vectors.Vector2f;
import com.GameName.Util.Vectors.Vector3f;

public abstract class Renderable {
	private Shader shader;	
	private Texture texture;	
	private Vector3f color;	

	protected Vector2f texCoordsTop, texCoordsBottom;
	
	private boolean vboUpdateNeeded;
	private boolean needsVBOids;
	
	protected int vertexVBO;
	protected int textureVBO;
	protected int colorVBO;
	
	private Vector3f rotation;
	private Vector3f translation;
	private Vector3f scale;
	
	public Renderable() {
		vertexVBO = -1;
		textureVBO = -1;
		colorVBO = -1;
	}
	
	public void render() {
		preDraw();	
		
		applyProperties();
		draw();		
		
		postDraw();
	}
	
	private void preDraw() {
		if(shader != null) {shader.bind();}
		if(texture != null) {glEnable(GL_TEXTURE_2D); texture.bind();}
		
		if(needsVBOids) {genVBOids();}
		if(vboUpdateNeeded) {updateVBOs();}
	}
	
	private void applyProperties() {
		glTranslatef(translation.getX(), translation.getY(), translation.getZ());

		glRotatef(rotation.getX(), 1, 0, 0);
		glRotatef(rotation.getY(), 0, 1, 0);
		glRotatef(rotation.getZ(), 0, 0, 1);
		
		glScalef(scale.getX(), scale.getY(), scale.getZ());
	}
	
	public abstract void draw();
	
	public abstract void updateVBOs();
	public abstract void genVBOids();
	
	private void postDraw() {
		if(shader != null) {Shader.unbind();}
		if(texture != null) {glDisable(GL_TEXTURE_2D); Texture.unbind();}
	}
	
	protected abstract void cleanUp_();
	
	public void cleanUp() {
		cleanUp_();
		
		if(vertexVBO != -1)  {GameName.getGLContext().deleteBuffer(vertexVBO);}
		if(textureVBO != -1) {GameName.getGLContext().deleteBuffer(textureVBO);}
		if(colorVBO != -1) 	 {GameName.getGLContext().deleteBuffer(colorVBO);}
	}
	
	public Shader 	getShader()  {return shader;}	
	public Texture 	getTexture() {return texture;}
	public Vector3f getColor() 	 {return color;}

	public Vector3f getRotation() {
		return rotation;
	}

	public Vector3f getTranslation() {
		return translation;
	}

	public Vector3f getScale() {
		return scale;
	}
	
	public boolean isVboUpdateNeeded() {
		return vboUpdateNeeded;
	}
	
	public boolean needsVBOids() {
		return needsVBOids;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public void setTranslation(Vector3f translation) {
		this.translation = translation;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	public void forceVBOUpdate() {
		vboUpdateNeeded = true;
	}
	
	public void setShader(Shader shader) {
		this.shader = shader;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
		vboUpdateNeeded = true;
	}

	public void setColor(Vector3f color) {
		this.color = color;
		vboUpdateNeeded = true;
	}
}
